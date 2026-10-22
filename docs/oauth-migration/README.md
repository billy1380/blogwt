# Migrating blogwt (and willshex) to OAuth 2.0 / OIDC with self-hosted Keycloak

Status: **planning only, no code changed.** Branch `oauth-migration` (blogwt repo) holds this folder.
Everything the plan relies on is catalogued in [INVENTORY.md](INVENTORY.md); read its sections 2 and 5 first.

## 0. Why, in one paragraph

Today every API call carries a datastore-ID "session" in the JSON body and the server re-loads the `Session` and `User` on each call, and on the default 20-minute session it **writes both back on every call** (INVENTORY 2). Passwords are `sha1(password + one global salt)`, the token is a guessable `Long`, all of a user's devices share one session, and there is no way to sign in with anyone else's identity. The goal is: users authenticate with a standard OIDC provider, the API only verifies a signed JWT (signature + `exp` + `aud`, no datastore read, no write), and **roles/permissions/profile ride in the token** so the user is loaded once at sign-in or refresh, not per call.

Decisions already taken by the project owner:
- Self-hosted **Keycloak** (not Firebase/Auth0).
- Roles, permissions and user attributes go **in the access token** (staleness until refresh is accepted; keep token lifetime short).
- Use existing Java libraries for verification, don't hand-roll JWT.

## 1. Target architecture

```
 GWT client ──┐                      ┌────────────────────────┐
 Flutter app ─┼─ auth code + PKCE ─▶ │ Keycloak (realm: willshex) │  users, passwords, email verify/reset,
 Jaspr web  ──┘                      └───────┬────────────────┘  remember-me, brute-force, later: social
        │  Authorization: Bearer <access JWT> │ JWKS (cached)         ▲ Admin API (service account)
        ▼                                     ▼                       │ write-through on role/profile change
 blogwt-server (resource server) ── verify sig/iss/aud/exp ──▶ request-scoped User built from claims
        └── Datastore keeps: User (profile, ownership ids), Role, Permission, Posts... (source of truth for access)
```

### 1.1 Who is the source of truth for roles/permissions?
Three ways to get claims into the token; **recommendation: A**.

| | How | Pros | Cons |
| --- | --- | --- | --- |
| **A (recommended for now)** | blogwt Datastore stays the source of truth. `ChangeUserAccess`, `Register`, `SetupBlog`, `ChangeUserDetails` **write through to Keycloak via the Admin API** (client roles + user attributes). Keycloak emits them with stock mappers | No Keycloak SPI to build. Existing admin UI (`UsersPage`, `ChangeAccessPage`) keeps working. Token carries claims natively | Two stores to keep consistent; needs a reconcile job; Admin API credentials live in blogwt |
| B | Keycloak calls blogwt at token time through a custom protocol-mapper SPI | One store | Java SPI deployed inside Keycloak; Keycloak depends on blogwt being up to log in |
| C | Keycloak only authenticates; blogwt exchanges the Keycloak token for its own signed JWT carrying roles/permissions | Full control of claims | Two token types, custom token endpoint, not "just OAuth" |

Later, optionally, move the source of truth to Keycloak entirely (admin UI then talks to the Admin API). A is a stepping stone to that.

### 1.2 Token contract (access token, RS256)
Short-lived (**5-10 min**), refresh-token rotation on. Required claims:

| Claim | Source | Used for |
| --- | --- | --- |
| `iss`, `aud` (= `blogwt-server`), `exp`, `iat`, `sub` | Keycloak | verification; `sub` also stored on `User.subject` |
| `blogwt_uid` | user attribute -> mapper | datastore `User.id`; needed because ~15 handlers store/compare `session.user` as an entity (INVENTORY 3): `rating.by`, `PushToken.user`, `GeneratedDownload.user`, `KeepAlive.owner`, `DataTypeHelper.same(...)`, `FollowUsers`/`BlockUsers` defaults |
| `roles` | Keycloak client roles of `blogwt-server`, flattened by an "User Client Role" mapper to a top-level array | `ADM` etc. -> `User.roles[].code` |
| `permissions` | same, second mapper (or a role-prefix scheme such as `perm:MPO`) | `MPO`, `MPA`, `MUS`, ... -> `User.permissions[].code` |
| `preferred_username`, `email`, `email_verified`, `given_name`, `family_name` | standard | `username`, `email`, `forename`, `surname` |

Permission codes are 3 characters, so token size is not a concern at the current scale; if it ever is, ship role codes only and expand server-side from a cached role table.

**Intended model (owner's original plan, recorded verbatim in spirit):** permissions cover the CRUD scenarios for each entity, and a user who holds the permission for an action may perform it (any one of the permissions an action accepts is enough). Roles bundle permissions. `ADM` is the role that covers the permissions of every role.

**What the code does today** (INVENTORY 2): `ADM` passes everything; otherwise *any one* required permission passes; permissions are only the user's *direct* ones, so `Role.permissionKeys` is never consulted and roles other than `ADM` grant nothing on the server. Only the "any one of" rule and the `ADM` override match the intent; **role-to-permission expansion is missing** (open question Q4, now resolved as "expand"; see Q11 for granularity).

**Consequence for the token:** the `permissions` claim is the *effective* set: direct permissions plus those of every role the user holds. Keycloak does this natively with composite client roles (role -> its permissions; `ADM` -> all), so with write-through (1.1 option A) blogwt keeps a role's permission list in sync as a composite role and the stock mapper emits the union. The legacy path (`UserValidator.authorisation`) needs the same expansion, otherwise dual mode would disagree. **Before enabling expansion, query production for `Role` rows with `permissionKeys`: any that exist will begin granting access the moment this ships.**

### 1.3 What stays out of the token
`avatar`, `summary`, `lastLoggedIn`, `added`, `group`, `verified`, `actionCode`, `password`. Clients that need the full profile call the bootstrap endpoint (3.3).

## 2. Libraries

| Need | Choice |
| --- | --- |
| JWT verification (server) | `com.nimbusds:nimbus-jose-jwt` (`RemoteJWKSet` with caching + rate limit, `DefaultJWTProcessor`, claims verifier). Alternative: `com.auth0:java-jwt` + `jwks-rsa`. Runtime is `java17` on App Engine with plain servlets, so no Spring. **Server module only**: must never reach `blogwt-shared` or `blogwt-client` (GWT cannot compile it) |
| Keycloak Admin API from blogwt | `org.keycloak:keycloak-admin-client` (match the server major) or plain HTTPS + the same nimbus lib for the service-account token. Confirm it runs under App Engine standard's URLFetch/HTTP restrictions before committing |
| GWT client | No maintained GWT OIDC library. Small JsInterop wrapper: PKCE (WebCrypto `SHA-256`), redirect, code exchange. See 4.1 |
| Flutter mobile/desktop | `flutter_appauth` |
| Jaspr web / Flutter web | `oidc` (pub.dev) or a thin PKCE implementation over `package:http`. **Verify package versions and web support in a spike before choosing** |
| Tests | nimbus `RSAKeyGenerator` to mint tokens against a fake JWKS; Keycloak Testcontainers for integration |

Per project preference, do **not** pin to old versions to dodge issues.

## 3. Server work (blogwt-server)

### 3.1 The compatibility shim (the key trick)
There are 54 handlers calling `SessionValidator.lookupCheckAndExtend(input.session, ...)` and then reading `input.session.user` (INVENTORY 3.8). Rewriting them all is unnecessary and risky. Instead:

1. Add `com.willshex.blogwt.server.auth` (new package): `TokenVerifier` (nimbus), `AuthConfig` (issuer, audience, JWKS URI, clock skew; read from properties/env), `Principal`.
2. A servlet-level hook (in `JsonServlet`'s request path, or a `javax.servlet.Filter` registered in `web.xml`) reads `Authorization: Bearer <jwt>` into a request thread-local (the code already uses `ContextAwareServlet.REQUEST`).
3. **Re-implement `SessionValidator.lookupCheckAndExtend` with the same signature**: if a verified principal exists, return a *transient* `Session` (`id == null`, `expires` = token `exp`, `user` = a `User` built from claims with `id = blogwt_uid`, `roles = [Role{code}...]`, `permissions = [Permission{code}...]`, plus username/email/names). Because `UserValidator.isAdmin` and `authorisation` use `user.roles` / `user.permissions` when non-null (UserValidator.java:98-148), **no datastore read happens** and every existing `authorisation(...)` call works unchanged.
4. Failure behaviour must match today's two patterns:
   - required-session handlers: invalid/expired/missing token -> throw (today `InputValidationException`).
   - optional-session handlers (about 15, `catch (InputValidationException)` -> anonymous): the shim must throw **`InputValidationException`** for an invalid token so these keep degrading to anonymous. Revisit (Q6): an *expired* token on an optional endpoint silently turning anonymous is how a logged-in user suddenly sees public content; clients must refresh before calling, and consider returning 401 for expired tokens.
   **Hazard: the transient user must never be persisted.** Handlers read `session.user` and only use `id` when passing it to services (`Key.create(user)`; confirmed for `PostService.getUserViewablePartialSlugPosts`, which only checks `user.id`), and the only `session.user.<field>` reads outside login hydration are `roles`, `permissions`, `id`. But `ChangePasswordActionHandler` (line 90-101) does `user = input.session.user; user.password = ...; updateUser(user)`, which in OAuth mode would write a claims-built partial user over the stored one (and `UserService` rebuilds `roleKeys` from `roles` via `Key.create`, which fails for id-less roles). In OAuth mode that handler must be rejected (Keycloak owns passwords) or reload the user by `blogwt_uid` first. Audit every `updateUser(` caller (only `ChangePassword`, `VerifyAccount`, `ChangeUserDetails`, `ChangeUserAccess` today) and add a test that fails if a transient user reaches `updateUser`/`addUser`.
5. Dual-mode flag (property `OAUTH_ENABLED`, default off): if a bearer is present use it; else fall back to the legacy `Session` path. This lets the clients migrate one at a time. Remove the fallback at cutover.
6. Keep populating `output.session` (transient, `id == null`) while the GWT client still has its implicit-logout-on-`session == null` rule (INVENTORY 4.1). Remove with the client change.
7. `ActionHandler.clearSensitiveFields` stays.

### 3.2 Non-API entry points
| Entry | Change |
| --- | --- |
| `ServletHelper.session(HttpServletRequest)` (cookie `session.id`) | read a bearer (or an HttpOnly cookie, see 4.1) instead; keep the return type |
| `MainServlet.appendSession` (MainServlet.java:287; willshex :292) | embeds `var session=` for the GWT boot. Either embed the claims-derived transient session, or stop and let the client bootstrap. Interacts with the BFF choice in 4.1 |
| `DownloadJsonServlet.doGet` (cookie auth, file stream) | a link/`window.open` cannot send a bearer. Replace with a **short-lived HMAC-signed download token** (`GenerateDownload` / `GetGeneratedDownloads` returns a URL with `?token=`; GET verifies token + owner). Do not accept a bearer in a query string |
| `ResourceUploadAction` (`/upload`) | currently unauthenticated. Require a bearer with `MRE` in `doPost`; the Blobstore upload callback only sees `blobstoreService.getUploads` so mint the upload URL (`doGet`) behind auth |
| CORS | `web.xml` has `corsDomainsRegex=.*`. A bearer header triggers preflight; confirm the gson-web-service `JsonServlet` allows the `Authorization` header (library source is under `deps`/`nonmavendeps`; verify and patch if needed). Tighten the regex to known origins at cutover |

### 3.3 Identity linking and bootstrap
- Add `@Index String subject` to `User` (Keycloak `sub`). Datastore index needed: add to `datastore-indexes.xml` if composite; single-property needs none.
- Repurpose `LoginActionHandler` into a **bootstrap** call (`GetMe` / keep the name `Login` for DTO compatibility): requires a valid bearer, loads the full `User` once (hydrates roles/permissions, avatar), updates `lastLoggedIn` (the only write, once per sign-in not per call), returns `Session`-shaped response for old clients.
- Linking order on first bootstrap with an unknown `sub`: (1) existing `User` with same **verified** email -> set `subject`; (2) else create a `User` applying `NEW_USER_ROLES`/`NEW_USER_PERMISSIONS` (as `RegisterUserActionHandler` does today) and write `blogwt_uid` back to Keycloak.
- **Registration**: disable Keycloak self-registration for now; keep `RegisterUserActionHandler` as the front door (it already enforces `ALLOW_USER_REGISTRATION` and the new-user defaults) and have it create the Keycloak user through the Admin API with the `blogwt_uid` attribute set. This avoids the chicken-and-egg where a token has no `blogwt_uid`. Social login later requires the JIT path above plus a token refresh.

### 3.4 Write-through to Keycloak (decision 1.1 option A)
Touch points (all in `api/user/action` and `api/blog/action`): `RegisterUser`, `ChangeUserAccess` (roles, permissions, `suspendUntil`), `ChangeUserDetails` (email, names), `SetupBlog` (initial admin), the new role/permission Create/Update/Delete handlers (4.2b), `FixRolesAction`/`FixPermissionsAction`/`AdminAction` (dev tools; must also reconcile). Add a `KeycloakAdminService` behind a provider like the existing `*ServiceProvider` pattern. Write-through should fail loudly (surface the error) rather than leave the two stores silently diverged; also build a one-shot **reconcile** action.

Suspension: Keycloak has no timed disable. On suspend set the Keycloak user `enabled=false` and revoke sessions; re-enable at `suspendUntil` via the cron already available in `WEB-INF/cron.xml` (willshex has it; blogwt would need one). Access tokens stay valid until `exp`: accepted.

### 3.5 Code to retire at cutover
`LoginActionHandler` credential branch, `LogoutActionHandler`, `VerifyAccountActionHandler`, `ResetPasswordActionHandler`, `ChangePasswordActionHandler`, `ForgotPasswordActionHandler`, `CheckUsernameActionHandler` (Keycloak does these: email verification, forgot/reset, change password via `kc_action=UPDATE_PASSWORD`, username uniqueness), `SessionService*`, `ISessionService`, the `Session` entity and its datastore kind, `UserService.getLoginUser/getEmailLoginUser/verifyPassword/generatePassword/getSalt`, `sendActionEmail` (unless Keycloak SMTP is not set up), `User.password`/`actionCode`, `PropertyHelper.PASSWORD_HASH_SALT`. Leave `RegisterUser` (changed), `ChangeUserDetails`, `GetUsers`, etc.

## 4. Client work

### 4.1 GWT (blogwt-client; willshex copy is near-identical, 40 files differ)
Single hub: `controller/SessionController`. Everything else calls `SessionController.get().setSession(input)` / `sessionForApiCall()` (about 40 call sites, INVENTORY 4.1), so keep those method names and change what they do.

Two options for the token flow; **decision Q2**:
- **BFF-lite (recommended for GWT)**. The GWT app is served by blogwt itself, same origin. Add `/oauth/login` (redirect, PKCE + state) and `/oauth/callback` (code exchange server-side, confidential client); store the **refresh token in an HttpOnly, Secure, SameSite=Lax cookie** (encrypted); `/oauth/token` returns a fresh access token to JS (kept in memory only). `MainServlet.appendSession` can embed the claims for instant boot. The refresh cookie is the only server-visible "session", and it is not readable from JS or the datastore.
- **Pure SPA**: public client, PKCE in the browser, tokens in memory + `prompt=none` iframe/refresh-token rotation. Simpler server, weaker against XSS and third-party-cookie blocking.

Changes either way:
- `SessionController`: remove the `session.id` cookie (currently set from JS for **20 years**, line 115), the credential `login`, the `restoreSession` session-login path, and the **implicit logout** `CallSuccessEvent` handler (a bearer response need not carry `session`).
- `isAdmin()`, `isAuthorised(...)`, `roles()`, `permissions()`, `user()` read from the bootstrap/claims; keep their signatures so `HeaderPart`, `NavigationController`, pages, `PostsPage`, etc. need no change.
- Replace `LoginPage`, `RegisterPage` (see 3.3), `VerifyAccountPage`, `ResetPasswordPage`, `ChangePasswordPage` with redirects/links to Keycloak (`kc_action=UPDATE_PASSWORD` for change-password).
- `GeneratedDownloadController` / downloads: use the signed token URL (3.2).
- Keep `isValidSession()` (it compares `session.expires` to the clock) but base it on token `exp` and on whether a refresh succeeds.

### 4.2 Dart: `blogwt_flutter_client/packages` (Flutter `client_flutter` + Jaspr `client_web`, shared `client_common`)
Single hub: `client_common/lib/helper/session_helper.dart` + `features/auth/bloc/auth_bloc.dart` + `service/startup_service.dart`. ~60 files read `isAdmin`/`isAuthorised`/`roles` through these, so **keep `SessionHelper`'s public API** and swap the internals.

- `ApiHelper.setSession(request, session)` (api_helper.dart:55) becomes a no-op; add an `http.BaseClient` wrapper that attaches `Authorization: Bearer`, refreshes on near-expiry and on 401, and is injected where the generated `*_client.dart` get their `http.Client`.
- Token storage: mobile/desktop `flutter_secure_storage`; web in memory with refresh via the OIDC library. Replace `store.load.session` (currently persists the whole `Session`+`User`) with a token store.
- `AuthBloc`: `AuthLogin(username, password)` -> OIDC sign-in; `AuthCheckSession` -> silent refresh then bootstrap call; `AuthLogout` -> end-session redirect.
- `ApiHelper.uploadFile` (sets a `Cookie: session=` header, which the server ignores anyway) -> bearer.
- `accessCode` stays hard-coded for now (it is an API key, not auth).
- **Fix while here**: `posts_bloc.dart:52` uses `managePages` for `showAll` where the server checks `MANAGE_POSTS`; and `isAuthorised`'s "empty requirement returns true" quirk (INVENTORY 4.2/5).
- **`backend_local`** (`local_user_service.dart`): an on-device clone of the user API with its own sha1+salt login and local sessions, selected by `useLocalApi` (defaults `true` on Flutter, `false` on web). OIDC does not apply. Decide (Q5): keep as an offline/demo mode that mints a synthetic local principal, or drop.
- Flutter UI gates mostly on `isAdmin` only (`start_side_nav.dart:280`, `ui_helper.dart:294`, `page_screen.dart:42`, `post_screen.dart:50`); permission-gated UI is a separate improvement, not part of this migration.
- `flutter-willshex` has **no auth**; nothing to migrate. If it gains login later it should reuse `client_common`'s helper.

### 4.2b Role and permission management belongs in the Dart clients (owner decision)

Roles and permissions cannot be managed anywhere today (INVENTORY 3.6). The owner does **not** want more GWT pages, so this is built in `blogwt_flutter_client`, in **both** `client_flutter` and the Jaspr `client_web`; the GWT `RolesPage`/`PermissionsPage` stay read-only and GWT gets no new UI. It is independent of the OAuth work, and it is a prerequisite for the intended role model (1.2): without it nobody can put a permission on a role except by running a dev action.

Server (blogwt, then willshex), new actions on `UserJsonServlet`, same handler pattern as the rest (`accessCode`, `lookupCheckAndExtend`, `UserValidator.authorisation`):

| New handler | Required | Rules |
| --- | --- | --- |
| `CreateRole`, `UpdateRole`, `DeleteRole` | P(`MRO`) | finally enforces `MANAGE_ROLES`. `UpdateRole` sets name, description and the role's permission list (`Role.permissionKeys`). Reject changes to / deletion of `ADM`. Deleting a role must also remove it from users' `roleKeys` (or refuse while assigned) |
| `CreatePermission`, `UpdatePermission`, `DeletePermission` | P(`MPR`) | the seven built-in codes (`MPR`, `MRO`, `MUS`, `MPO`, `MPA`, `MRE`, `MNO`) are referenced by `PageType` and the handlers: no rename, no delete (name/description editable). Deleting a custom permission must strip it from roles and users. `code` immutable and unique (indexed, validate like `UserValidator` does for usernames) |
| `ChangeUserAccess` (existing) | P(`MUS`) | add the escalation guard from INVENTORY 5.5: only an `ADM` may grant or remove `ADM` |

Plumbing that must change with it:
- DTOs: new `*Request`/`*Response` types in `blogwt-shared` (Java; the handler headers say these are generated by xsdwsdl2code, **find the generator/source schema before hand-writing**) and the matching Dart classes under `client_common/lib/api/blogwt/user/call/` plus `UserService` client methods.
- `UserServiceProvider`-side: `RoleService`/`PermissionService` already have `addRole`/`updateRole`-style methods (`RoleService.java:57-61` maps `permissions` to `permissionKeys`); confirm and extend, keeping Objectify keys consistent.
- `backend_local` (`local_user_service.dart`) must implement the new calls so `useLocalApi` mode keeps working.
- Token consequence (1.2, 3.4): creating/updating/deleting a role or permission becomes a Keycloak write-through (composite client role + its children), so these handlers are new touch points for `KeycloakAdminService`.
- Granularity (Q11): if the owner moves to CRUD permissions per entity, this UI is how they get created; the seed list in `PermissionHelper.createAll` and the handler->permission map would grow accordingly.

Dart UI, identical behaviour in Flutter and Jaspr, state in `client_common` blocs so the two stay in step (`features/roles_permissions/bloc` already exists with Fetch/Refresh/Reset; add Create/Update/Delete events):
- `roles_screen.dart`: create / edit / delete a role, with a checklist of all permissions to bundle into it; `ADM` shown but locked. The Flutter FAB that currently calls `UiHelper.notAvailable` becomes "new role".
- `permissions_screen.dart`: create / edit / delete a permission; built-ins locked as above.
- `user_access_screen.dart` (both): replace the free-text permission/role code field (web: placeholder "Add permission code (e.g. MPO)") with pickers fed by `GetRoles`/`GetPermissions`, and show *effective* permissions (role-expanded) next to direct ones.
- Gate the screens with the same permission codes as the new handlers (`MRO`, `MPR`) via `SessionHelper.isAuthorised`, not the admin-only `isAdmin` shortcut the Flutter screens use today.

### 4.3 willshex repo
Fork, same module layout, diverges in: `MainServlet`, `UserService`, `SessionService`, `BlogJsonServlet`, `UserJsonServlet`, `web.xml`, `appengine-web.xml`, `PageType`, plus its own `counteradmin`, `keepalive`, `spacecloud`, `triangles` packages (INVENTORY 3.7). Plan:
1. Implement and prove in blogwt first.
2. Port by diffing each touched file against willshex's copy (`diff -u blogwt/<f> willshex/<f>`); the new `auth` package copies verbatim.
3. The extras need no handler edits thanks to the shim (3.1), but fix `keepalive` owner checks (INVENTORY 5.4) and decide whether `counteradmin` keeps borrowing `MRE` (Q7).
4. Add a branch `oauth-migration` in `willshex` when work begins (not created yet).

## 5. Keycloak setup (to be captured as a committed realm export)

- Hosting: Keycloak cannot run on App Engine standard. Options: Cloud Run + Cloud SQL (Postgres), or a small VM with Docker. **Decision Q1**. Needs a stable HTTPS hostname (for example `auth.willshex.com`) and SMTP for verify/reset emails.
- Realm `willshex` (or one realm per blogwt deployment: **Q3**). Clients:
  - `blogwt-server`: bearer-only/audience resource; client roles = the 7 permission codes + `ADM`.
  - `blogwt-web`: confidential (BFF-lite) or public (SPA), PKCE S256, exact redirect URIs.
  - `blogwt-mobile`: public, PKCE, custom-scheme redirect, for Flutter.
  - `blogwt-jaspr`: public, PKCE, web redirect URI(s).
  - `blogwt-admin-sync`: service account with only the Admin API roles `manage-users` / `view-users` / `query-users` (used for write-through).
- Realm settings: SSO session idle ~20 min to mirror today's short session, "Remember me" to mirror `longTerm` (30 days), access token 5-10 min, refresh rotation, brute-force detection on, email verification required, self-registration off (3.3), password policy on.
- Mappers: `blogwt_uid` attribute; client-role -> `roles` and `permissions` top-level arrays; add `aud=blogwt-server`.
- Local dev: add a `docker-compose.yml` (Keycloak + Postgres or `start-dev`) and `realm-export.json` under `docs/oauth-migration/keycloak/` (not created yet). The README currently documents running the Datastore emulator; extend it with Keycloak.

## 6. Migrating existing users

Source: `UserService`, datastore `User`; `tool/Exporter.java` already exports users with roles/permissions hydrated.

1. Export users (username, email, names, `roles`, `permissions`, `verified`, `suspendUntil`, id).
2. Generate a Keycloak realm user import with attribute `blogwt_uid`, client-role mappings, `emailVerified`, `enabled = !suspended`.
3. **Passwords** (decision Q8; spike first):
   - *Preferred*: import the existing hash through a small custom Keycloak `PasswordHashProvider` ("sha1 + suffix salt") so users keep their password; Keycloak rehashes to the realm's algorithm on first successful login. Need the exact format of `StringUtils.sha1Hash` (hex vs base64; it lives in the `willshex-*` utility library, not in this repo) and the effective salt (`PASSWORD_HASH_SALT` property, falling back to a source constant).
   - *Fallback*: import users without credentials and send everyone a reset email (`UPDATE_PASSWORD` required action). Simple, one-time friction.
4. Link: set `User.subject` for every imported user from the import result so no first-login linking is needed.
5. Dry run on a datastore export, compare counts, spot-check admins.
6. Keep `User.password` until cutover is verified, then null it out and drop the field.

## 7. Phased plan (each phase ships on its own)

| # | Phase | Deliverable | Exit check |
| --- | --- | --- | --- |
| 0 | Decide + spike | Answers to Q1-Q8; local Keycloak + realm export; password-hash import spike; CORS `Authorization` check; `oidc`/`flutter_appauth` web check | Written decisions in this file |
| 1 | Server dual-mode | `auth` package, shim in `SessionValidator`, filter, `OAUTH_ENABLED`, `User.subject` | Existing handler tests pass in both modes; a minted test JWT hits an `authorisation`-gated handler (MPO) and `ADM` bypass, and an invalid token behaves as anonymous on an optional handler |
| 2 | Provisioning | bootstrap endpoint, JIT link, Admin API write-through, reconcile action, user migration tooling | Existing users sign in via Keycloak with roles intact; `ChangeUserAccess` reflected in the next token |
| 3a | GWT client | redirect/BFF flow, `SessionController` rewrite, downloads via signed URL | **Manual test by the project owner** (see section 8) |
| 3b | Dart clients | token store, bearer client, `AuthBloc`, Flutter + Jaspr screens | manual test by the project owner |
| 4 | willshex port | same changes ported, own Keycloak client/realm | manual test |
| 5 | Cutover + cleanup | flip default, remove legacy login/session/password code (3.5), delete `Session` kind, tighten CORS | legacy paths gone, `Session` kind empty |
| 1b | Role/permission management (independent of OAuth, can go first) | new server handlers, DTOs, Dart screens in Flutter + Jaspr, `backend_local` support (4.2b) | an admin can create a role, give it permissions, assign it to a user and see the effective permissions, in both clients |
| 6 | Gap fixes | INVENTORY 5: delete-resource, upload auth, draft visibility, keepalive owner checks, escalation guard, `ForgotPassword`, role expansion | separate PRs |

## 8. Working agreements for whoever picks this up

- **No automated browser testing** (`browser_subagent` etc.) in this workspace (`/Users/billy1380/git/AGENTS.md`). For every UI-visible change give the owner concrete local steps and ask for a screenshot or feedback.
- Local server needs the Datastore emulator and four env vars (see `README.md` "Development"); add Keycloak to that recipe.
- GWT code must stay free of server-only libraries; the shared module (`blogwt-shared`) is compiled by GWT.
- The owner makes all commits; leave changes in the working tree.

## 9. Open questions

| # | Question | Default if not answered |
| --- | --- | --- |
| Q1 | Where does Keycloak run (Cloud Run + Cloud SQL / VM)? domain? SMTP? | Cloud Run + Cloud SQL, `auth.<domain>` |
| Q2 | GWT flow: BFF-lite (HttpOnly refresh cookie) or pure SPA? | BFF-lite |
| Q3 | One realm for blogwt + willshex + future sites, or one per deployment? | One realm, one client set per site |
| Q4 | Should non-`ADM` roles grant their `Role.permissionKeys`? | **Resolved by owner: yes**, roles bundle permissions and `ADM` covers all. Check production `Role` data first (see 1.2), implement in the legacy check and in the token |
| Q5 | Keep `backend_local` offline mode with a synthetic identity, or drop it? | Keep, synthetic principal |
| Q6 | Expired/invalid token on an *optional* endpoint: silently anonymous (today) or 401? | Keep silent for compatibility, make clients refresh first, revisit |
| Q7 | Should willshex `counteradmin` get its own permission instead of borrowing `MRE`? | Leave as is |
| Q8 | Passwords: custom hash provider import or force-reset everyone? | Spike the provider; fall back to reset |
| Q9 | Suspension: write-through disable + cron re-enable, or drop timed suspension? | Write-through + cron |
| Q11 | Permission granularity: the code has 7 coarse `MANAGE_*` permissions, the original plan was CRUD per entity. Migrate to per-entity create/read/update/delete permissions (and map each handler to one), or keep `MANAGE_*`? Needs the owner's answer on the exact reading, see the open point in the chat that produced this edit | Keep `MANAGE_*` for the migration, which does not block a later split because claims are plain code strings |
| Q10 | Self-registration stays in blogwt (Admin API) or moves to Keycloak's page? | Stays in blogwt until social login is wanted |

## 10. Risk list

- Dual stores (Datastore roles vs Keycloak role mappings) drifting. Mitigate with fail-loud write-through and the reconcile action.
- Stale access: a demoted or suspended user keeps access until the token expires (<= 10 min). Accepted.
- The 54-handler shim relies on handlers using `session.user` only for `id` (via services), `roles`, `permissions` and (client side) display fields. Re-run `grep -rhoE "session\.user\.[a-zA-Z]+"` over `blogwt-server` and `willshex/blogwt-server` at the start of phase 1 (at the time of writing: `roleKeys`/`permissionKeys` in login hydration only, plus `roles`, `permissions`, `id`), and see the persistence hazard in 3.1.
- GWT `NavigationController` and the Dart router gate pages on session validity at navigation time; a token that expires mid-page should refresh transparently, not bounce to login.
- Browser downloads and the Blobstore upload callback cannot carry a bearer header (3.2).
- A mis-set `aud`/`iss` check silently accepting tokens from another realm: verify both, and test the negative cases.
