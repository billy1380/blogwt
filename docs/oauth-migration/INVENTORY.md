# Auth inventory (as of branch `oauth-migration`, cut from `master`)

Scan of every place blogwt, willshex and the Flutter/Jaspr clients identify the caller or decide what they may do.
Companion to [README.md](README.md), which holds the plan. Nothing here has been changed yet.

Repos scanned (all under `/Users/billy1380/git`):

| Repo | What it is |
| --- | --- |
| `blogwt` | GWT client + App Engine server + shared DTOs. The reference implementation |
| `willshex` | Fork of blogwt with the same module layout plus extras (counter admin, keep-alive, spacecloud, triangles). 73 server files differ, 14 shared, 40 client |
| `blogwt_flutter_client` | Dart port of the blogwt client: `client_common`, `client_flutter` (Flutter), `client_web` (Jaspr), `backend_local` (on-device API) |
| `flutter-willshex` | Flutter + Jaspr port of the willshex site. **Has no auth code at all** (no session, login, role or permission references). Only the Jaspr/Flutter UI shell |

---

## 1. Data model

All in `blogwt-shared/.../api/datatype/` (identical in willshex, which adds `counter`, `keepalive`, `spacecloud` types).

| Entity | Auth-relevant fields |
| --- | --- |
| `User` | `username` (indexed), `email` (indexed), `password` (sha1+salt), `roleKeys: List<Key<Role>>` + `@Ignore roles`, `permissionKeys: List<Key<Permission>>` + `@Ignore permissions`, `verified`, `actionCode` (indexed; used for both email verification and password reset), `suspendUntil`, `lastLoggedIn`, `group`, `forename`, `surname`, `summary`, `avatar` (@Ignore, computed), `added`, `expires` |
| `Role` | `code` (indexed, e.g. `ADM`), `name`, `description`, `permissionKeys` + `@Ignore permissions` |
| `Permission` | `code` (indexed), `name`, `description`, `type` (`PermissionTypeTypeUser` / `PermissionTypeTypeSpecial`) |
| `Session` | `expires`, `userKey` (indexed), `@Ignore user`, `longTerm`. The datastore `Long id` is the only token |
| `Relationship` | follow/block between users (`RelationshipTypeType`); see `FollowUsers`/`BlockUsers` |
| `PushToken`, `GeneratedDownload`, `Rating`, `Comment`, `Post`, `KeepAlive` (willshex) | carry a `user`/`userKey`/`by`/`owner` set from the session user |

Codes (`blogwt-shared/.../helper/PermissionHelper.java`, `RoleHelper.java`):

| Permission code | Constant | Meaning |
| --- | --- | --- |
| `MPR` | `MANAGE_PERMISSIONS` | add permissions to users; also gates `GetRoles`/`GetPermissions` |
| `MRO` | `MANAGE_ROLES` | page gate only; **no server handler checks it** |
| `MUS` | `MANAGE_USERS` | user admin, see section 3 |
| `MPO` | `MANAGE_POSTS` | create/update/delete/see unpublished posts |
| `MPA` | `MANAGE_PAGES` | create/update/delete pages |
| `MRE` | `MANAGE_RESOURCES` | page gate; server check only in willshex counter admin (see 3.5) |
| `MNO` | `MANAGE_NOTIFICATIONS` | notifications and meta notifications |

| Role code | Meaning |
| --- | --- |
| `ADM` | "System Administrator": passes every check, implicitly holds all permissions. `ADMIN_PERMISSIONS` is an empty list, the role carries no permission rows |

Seeded by `SetupBlogActionHandler` (admin role + `PermissionHelper.createAll()`), repaired by `dev/actions/FixRolesAction`, `FixPermissionsAction`, `AdminAction`.

## 2. How a call is authenticated today

### Wire format
- Every `Request` extends `blogwt-shared/.../api/Request.java` with `accessCode` and `session`. The body is JSON, dispatched by `*JsonServlet.processAction` on an action string.
- `Request.session` is a `Session` object, but clients only fill in `session.id` (`SessionController.sessionForApiCall`, Dart `ApiHelper.setSession`).
- `Response.session` is filled with the full session **including `user`, `roles`, `permissions`** on most calls (handlers do `output.session = input.session = ...`). `ActionHandler.clearSensitiveFields` only strips `user.password`.
- These DTOs are generated (handler headers say `xsdwsdl2code`), and `Session.id` is a `Long`, so a JWT cannot ride in `Request.session`.

### Per-call server path
`SessionValidator.lookupCheckAndExtend(input.session, name)`:

1. `SessionService.getSession(id)`: datastore/objectify-cache get.
2. Throws `InputValidationException(DataTypeNotFound)` if missing or `expires < now`.
3. Loads the user: `UserServiceProvider.getUser(keyToId(userKey))`.
4. `UserValidator.suspended(user)` throws `AuthorisationException` if `suspendUntil` is in the future.
5. **Extends if `expires - now < MILLIS_MINUTES`.** `MILLIS_MINUTES` is **20 minutes** (`ISessionService:17`) and a short-term session also lasts 20 minutes, so the condition is always true: every call on a non-"remember me" session writes the `Session` and the `User.lastLoggedIn`. Long-term is 30 days (`MILLIS_DAYS`).
6. Roles/permissions are **not** hydrated here. `UserValidator.isAdmin` / `authorisation` lazily call `PersistenceHelper.batchLookupKeys` on `roleKeys` / `permissionKeys` the first time they are needed. `LoginActionHandler` and `ServletHelper.session` hydrate eagerly for the client.

### Authorisation rule (`UserValidator.authorisation`, UserValidator.java:119-148)
- Passes if the user holds role `ADM`.
- Otherwise passes if the user holds **any one** of the required permissions (OR semantics).
- Permissions come **only from `user.permissionKeys`**. `Role.permissionKeys` is never consulted by the check, so roles other than `ADM` currently grant nothing on the server. (`GetRolesAndPermissions` does expand role permissions for display, lines 78-108.)
- `authorisation(user, null, name)` therefore means **admin only**.
- Failure throws `AuthorisationException`.

### Login (`LoginActionHandler`)
- Credentials path: `getLoginUser(username, pw)` or `getEmailLoginUser(email, pw)`. `sha1(password + salt)`, salt is the `PASSWORD_HASH_SALT` property with a source-constant fallback (`UserService.getSalt`).
- Session path: `input.session.id` set means "restore session", i.e. `lookupCheckAndExtend`. Used by `SessionController.restoreSession` and Dart `startup_service`.
- `getUserSession(user)` returns the user's one existing session if there is one, so **all devices share one session**; logging out on one logs out all.
- Response hydrates `user.roles` and `user.permissions` from keys.

### Other session consumers
| Where | What |
| --- | --- |
| `helper/ServletHelper.session(HttpServletRequest)` (ServletHelper.java:56) | Reads cookie `session.id`, `getSession`, `lookupCheckAndExtend`, strips password, hydrates roles/permissions |
| `MainServlet.appendSession` (MainServlet.java:287; willshex :292) | Server-renders `var session='<json>';` into the host page so the GWT client boots logged in |
| `api/download/DownloadJsonServlet.doGet` (blogwt :66, willshex :67) | **Browser GET, cookie only.** `?action=download&<id>` streams a generated file if `generatedDownload.userKey == session.userKey`. A bearer header cannot be sent from a plain link or `window.open` |
| `upload/ResourceUploadAction` | **No auth at all.** `doGet` hands out a Blobstore upload URL, `doPost` creates `Resource` rows. See 5 |
| `api/ActionHandler.clearSensitiveFields` | strips `session.user.password` from every response |
| `VerifyAccountActionHandler` | email-link verification **issues a session** (logs the user in) |
| `web.xml` | `security-constraint` forces HTTPS; `corsDomainsRegex` = `.*` (any origin) |

## 3. Server API surface: per handler

Legend. **R** = session required (any logged-in user), **O** = optional (invalid session silently becomes anonymous), **P(x)** = requires permission x or `ADM`, **A** = admin only, **self/P** = own record, or P(MUS) for someone else's, **owner** = row filtered/checked by user, **-** = none.

### 3.1 blog (`api/blog/action`)
| Handler | Auth | Notes |
| --- | --- | --- |
| CreatePost | P(MPO) | |
| UpdatePost | P(MPO) | |
| DeletePost | P(MPO) | |
| GetPosts | O; P(MPO) when `showAll` | line 56-73; also line 118 filters by `session.user` (relationship/blocked handling) |
| GetPost | O | `PostValidator.viewable`: unpublished post needs *any* session (not a permission). Gap: any logged-in user can read drafts |
| GetArchiveEntries, GetTags | O | |
| GetRatings | O | passes `session.user` to the service |
| SubmitRating | R | `rating.by = session.user` |
| GetRelatedPosts | R | |
| GetProperties | O (`catch (Exception)`) | |
| UpdateProperties | A | `authorisation(user, null, ...)` |
| GetResource, GetResources | R | |
| UpdateResource | P(MRE) | |
| **DeleteResource** | **R only** | **No `authorisation` call.** Any logged-in user can delete a resource and its blob. Existing gap, not introduced here |
| SetupBlog | - | self-guards with `ApiError.ExistingSetup`; creates ADM role, permissions, first users |
| willshex: AddResource | see willshex section | |

### 3.2 page (`api/page/action`)
CreatePage / UpdatePage / DeletePage: P(MPA). GetPage, GetPages: O. SubmitForm: O (session is validated when present).

### 3.3 notification (`api/notification/action`)
| Handler | Auth |
| --- | --- |
| AddMetaNotification, GetMetaNotification(s), UpdateMetaNotification, SendAdhocNotification, GetNotifications | P(MNO) |
| GetNotificationSettings | R; if `input.user` is someone else then P(MNO). Defaults to `session.user` |
| UpdateNotificationSettings | R |
| SetPushToken | R; `token.user = session.user` |

### 3.4 download (`api/download/action`)
GenerateDownload: R (`download.user = session.user`). GetGeneratedDownloads: R, owner. DeleteGeneratedDownloads: R, owner (`same(download.userKey, session.user)`, skips others).

### 3.5 search
SearchAll: O.

### 3.6 user (`api/user/action`)
| Handler | Auth | Notes |
| --- | --- | --- |
| Login | - / session restore | see section 2 |
| Logout | `SessionValidator.lookup` | deletes the session entity (so logs out every device) |
| IsAuthorised | R | pure "is my session still valid" probe; used by GWT `IsAuthorisedEventHandler` |
| Register | O; anonymous allowed only if property `ALLOW_USER_REGISTRATION`; if a session is present it must hold P(MUS) | applies `NEW_USER_ROLES` and `NEW_USER_PERMISSIONS` properties (comma list of codes), sends verification email |
| VerifyAccount | - | `actionCode` token; sets `verified`, returns a fresh session |
| ForgotPassword | R (!) | requires a valid session; effectively unreachable for a logged-out user. Likely dead |
| ResetPassword | O | emails an `actionCode` link (`#changepassword/reset/<code>`) |
| ChangePassword | O, but R when using the old password | two modes: `resetCode` (token) or old `password` (verified against `session.user`). Admin-reset branch is commented out (lines 45-56) |
| CheckUsername | - | |
| GetEmailAvatar | O | |
| ChangeUserDetails | self/P(MUS) | |
| GetUserDetails | self/P(MUS) | |
| GetUsers | P(MUS) | also line 119 uses `session.user` |
| ChangeUserAccess | P(MUS) | adds/removes roles + permissions, sets `suspendUntil`. **No escalation guard**: a MUS holder can grant `ADM` to themselves |
| GetRolesAndPermissions | P(MUS) | expands role->permission for display |
| GetRoles, GetPermissions | P(MPR) | |
| FollowUsers, BlockUsers | self/P(MUS) | defaults `user = session.user` |

**Roles and permissions are read-only through the API.** There is no Create/Update/Delete handler for `Role` or `Permission` (`UserJsonServlet` exposes only `GetRoles`, `GetPermissions`, `GetRolesAndPermissions`, `ChangeUserAccess`). Roles and permissions come from `SetupBlog` and the dev actions (`FixRolesAction`, `FixPermissionsAction`, `AdminAction`), and the only way to put a permission on a role is `FixRolesAction`. The only UI that changes access is assigning existing roles/permissions to a *user* (`ChangeAccessPage` in GWT, `user_access_screen.dart` in both Dart clients, which takes a free-text code). GWT `page/admin/RolesPage` and `PermissionsPage`, and the Dart `roles_screen.dart` / `permissions_screen.dart` (flutter and jaspr), are list-only; the Flutter "add" button calls `UiHelper.notAvailable`.

### 3.7 willshex extras
| Handler(s) | Auth |
| --- | --- |
| `keepalive`: AddKeepAlive | R; `owner = session.user` |
| `keepalive`: DeleteKeepAlives | R, owner-filtered |
| `keepalive`: GetKeepAlive, GetKeepAlives, UpdateKeepAlives | **R only, no owner check** (any logged-in user can read/update anyone's) |
| `counteradmin`: CreateCounter, UpdateCounter, CreateStyle, UpdateStyle | P(**MRE**), borrowing the resources permission |
| `counteradmin`: GetCounter(s), GetStyle(s) | O |
| `counteradmin`: HitCounter | - (public) |
| `blog`: AddResource | O, **no `authorisation` call**: anonymous callers can add `Resource` rows |
| Spacecloud / triangles servlets | public endpoints, not session-based |

### 3.8 Count
54 server files reference `SessionValidator` in blogwt; every one of them goes through `lookupCheckAndExtend` except `LogoutActionHandler` (`lookup`) and `ServletHelper`.

## 4. Client usage

### 4.1 GWT (`blogwt-client`, and the willshex copy)
`controller/SessionController.java` is the single hub.

| Behaviour | Detail |
| --- | --- |
| Boot | `Window.get().getSession()` reads the server-injected `var session` JSON. Otherwise `removeSession()` |
| Token storage | `session.id` cookie set from JS (not HttpOnly, **expires in 20 years**: `MILLIS_PER_DAY * 365 * 20`, SessionController.java:115), plus in-memory `Session`. The server-side expiry, not the cookie, ends the session |
| Request signing | `setSession(input)` / `sessionForApiCall()` used in 11x `UserController`, 5x `PostController`, 5x `PageController`, 4x `ResourceController`, 4x `MetaNotificationController`, 3x `GeneratedDownloadController`, 2x `PropertyController`, `SearchController`, `RoleController`, `PermissionController`, the five `*Oracle` suggest oracles, `PostsPlugin`, `HeaderPart` |
| **Implicit logout** | A `CallSuccessEvent` handler calls `logout()` whenever a response arrives with `session == null` while the client believes it is logged in. Bearer-only responses that omit `session` would log everyone out. Must be rewritten |
| Client-side checks | `isAdmin()`, `isAuthorised(PageType)`, `isAuthorised(Collection<Permission>[, skip])`, `isValidSession()` (compares `session.expires` to the clock) |
| Quirk | `isAuthorised(required, skip)` returns **true when `required` is empty and the user has a non-null permissions list**; the server has no such rule |
| Page gating | `PageType` (`blogwt-shared/.../page/PageType.java`) carries `requiresAuthentication`, `requiresSystemAdmin`, `requiredPermissions`; `NavigationController` (line ~140) redirects to login or denies when `isValidSession`/`isAuthorised(p)` fail |
| Menus/buttons | `HeaderPart` (lines 454-525: admin menu items per permission), `PostsPage` (new-post link, MPO), `PostDetailPage` + `PageDetailPage` (edit link), `SectionPart` (edit), `ChangeAccessPage` (MUS or admin), `ChangeDetailsPage` (admin sees extra, `UserHelper.isAdmin`), `DetailsPart` (is-self check), `EditPostPage` (author = session user) |
| Downloads | `GeneratedDownloadController` sets session on API calls; the file GET relies on the cookie |

PageType permission map:

| PageType | Requirement |
| --- | --- |
| `EditPostPageType`, `AllPostsPageType` | P(MPO) |
| `EditPagePageType`, `PagesPageType` | P(MPA) |
| `UsersPageType` | P(MUS) |
| `RolesPageType` | P(MRO) |
| `PermissionsPageType` | P(MPR) |
| `ResourcesPageType`, `EditResourcePageType` | P(MRE) |
| `MetaNotificationsPageType`, `MetaNotificationDetailPageType` | P(MNO) |
| `PropertiesPageType`, `ChangeAccessPageType`, `WidgetTestPageType` | login + system admin (the constructor's 2nd/3rd boolean) |
| `ChangeDetailsPageType`, `LogoutPageType` | login |
| `DownloadsPageType` | property `DOWNLOAD_ENABLED` |
| willshex-only `KeepAlivesPageType`, `EditKeepAlivePageType`, `PublishersPageType`, `EditPublisherPageType`, `ApplicationsPageType`, `EditApplicationPageType`, `MetaPropertiesPageType`, `EditMetaPropertyPageType` | login |
| everything else (blog, post, page, login, register, verify, reset, search, tag, setup, willshex tools/games) | public |

### 4.2 Flutter + Jaspr (`blogwt_flutter_client`)
Shared logic lives in `packages/client_common` (used by both `client_flutter` and the Jaspr `client_web`). It is a direct port of the GWT hub.

| Area | Detail |
| --- | --- |
| Hub | `client_common/lib/helper/session_helper.dart`: `login`, `restoreSession`, `register`, `logout`, `isAdmin`, `roles`, `permissions`, `isPageAuthorised(session, PageType)`, `isAuthorised(session, perms, {skip})`. Same empty-required quirk as GWT |
| State | `features/auth/bloc/auth_bloc.dart` + `auth_state.dart` (`AuthAuthenticated.session`, `isAdmin`); events `AuthCheckSession/Login/Register/Logout/SetSession` |
| Persistence | `StoreHelper.store` saves the whole `Session` and `User` entities locally (`session_helper._onLoginSuccess`, `service/startup_service.dart:32-51` restores via the session-login path) |
| Signing | `ApiHelper.setSession(request, session)` (`helper/api_helper.dart:55`) copies only `session.id`. `accessCode` hard-coded to the WEB code (line 25). `ApiHelper.uploadFile` sends `Cookie: session=<id>` (line 83), which doesn't even match the server's `session.id` cookie name and is ignored since upload has no auth |
| Endpoints | `ApiHelper.baseUrl = liveUrl`; on web goes through a CORS proxy (`corsProxy`, line 21) |
| Page gating | `client_common/lib/page_type.dart` mirrors the GWT `PageType` (`requiresAuthentication`, `requiresSystemAdmin`, `permissionCodes`). Same table as 4.1 for the shared pages. Routes: `client_web/lib/routes.dart`, `client_flutter/lib/routes.dart` |
| UI gating | Jaspr: `client_web/lib/ui/parts/layout.dart:185` (`authState.isAdmin` shows the admin menu). Flutter: `ui/parts/start_side_nav.dart:280`, `ui/data/admin_menu.dart`, `ui/parts/admin_view_user_tile.dart`, `helper/ui_helper.dart:294`, `screen/page_screen.dart:42`, `screen/post_screen.dart:50` all use **`isAdmin` only**, never fine-grained permissions |
| Mismatch | `features/posts/bloc/posts_bloc.dart:52` decides `showAll` using `managePages`, the server checks `MANAGE_POSTS` (MPO) |
| Access admin UI | `client_web/.../user_access_screen.dart` and `client_flutter/.../user_access_screen.dart` edit `user.roles` / `user.permissions` by code and call `ChangeUserAccess` |
| `backend_local` | A **second, on-device implementation** of the user API (`local_user_service.dart`: `login`, `changePassword`, `getRolesAndPermissions`, `isAuthorised`, `_getSession`). Re-implements sha1+salt and local sessions. `startup_screen.dart:53` and `web_configuration_service` `use_local_api` choose it over the remote API (`useLocalApi` defaults to `true` on Flutter, `false` on web). OIDC does not apply to it; decide whether it keeps a stub identity |
| Hosted `flutter-willshex` | No auth. Out of scope unless willshex pages gain login |

### 4.3 What the clients read out of the user
Fields read from the session user anywhere on the client side: `roles[].code`, `permissions[].code`, `forename`, `surname`, `username`, `email`, `avatar`, `lastLoggedIn`, `id` (identity compare), `suspendUntil` (admin view), `verified`. These are the claims/`/userinfo` the new flow must keep providing.

## 5. Existing gaps found during the scan (not fixed here)
These exist today; the migration is a good moment to fix them, but they are separate decisions.

1. `DeleteResource` has no permission check (any logged-in user).
2. `ResourceUploadAction` (`/upload`) is unauthenticated for both `doGet` (mints upload URLs) and `doPost` (creates `Resource`s), and willshex `AddResource` has no permission check either.
3. `GetPost` shows unpublished posts to any logged-in user (`PostValidator.viewable` checks `session != null`, not MPO).
4. willshex `GetKeepAlive(s)`/`UpdateKeepAlives` have no owner check.
5. `ChangeUserAccess` lets a MUS holder grant `ADM` to themselves.
6. `ForgotPassword` requires a session (effectively dead).
7. `Role.permissionKeys` is ignored by `UserValidator.authorisation`; only `ADM` is meaningful. **Owner's original intent: roles bundle permissions, permissions cover CRUD per entity, `ADM` covers everything**, so this is a bug, not a design choice (README 1.2, Q4, Q11).
8. `MANAGE_ROLES` is never enforced on the server.
9. Client/server mismatch for `showAll` posts (MPA vs MPO) in the Dart client, and the empty-requirement quirk in both client `isAuthorised` implementations.
10. Passwords are sha1 + a single global salt (`PASSWORD_HASH_SALT` property or a source constant); `User.password` is returned in `toJson`, relying on `stripPassword` at each exit.
11. `Session.id` is a datastore `Long` (guessable), one session per user shared by all devices, 20-minute sliding expiry that writes twice per call.
12. Three static `accessCode` UUIDs (`ApiValidator.ALLOWED_ACCESS_CODES`) live in source and in the Dart client; they are API keys, not user auth.
