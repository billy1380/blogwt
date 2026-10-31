# To do

## Load post contents in one batch when listing pages

`PageService.populatePostContents` (`blogwt-server/.../service/page/PageService.java`) loads each post's content with its own datastore read (see the `FIXME: just load in one go (batch)` there). `GetPages` with `includePosts` set does that for every part of every page.

`PostService` already batches the same lookup in `getUserViewablePosts` and a second method that lists posts: collect the content ids, load them with `loadContent().ids(...)`, assign each post its content.

- Add a public method on `IPostService` / `PostService` that fills in the content of a list of posts in one batch, and use it from `PageService`.
- Skip a post whose `contentKey` is `null`; the existing copies of the loop do not guard it.
- The two existing copies of the loop in `PostService` could use the new method too.

The Dart clients no longer ask for the parts to build the menu (`GetPages` sends the part ids instead), so this only matters for callers that really want the page text.

## Two levels of secret property

`PropertyHelper.isSecretProperty` is one level: a secret property is never sent to a client (`GetProperties` and the page script in `MainServlet` drop it), so nobody can read it back, and the Properties screens only let you type a new value over it. `setup.password.hash.salt`, `email.brevo.key` and `recaptcha.site.key` all get this.

Some values should be secret but still be sent to a client, for example the reCAPTCHA site key, which a form has to read to load the captcha.

- Level 1, hidden: set from a client, then never leaves the server (the salt, the Brevo key).
- Level 2, shown to admins: sent to the client so an admin can view and edit it, and shown as a password box that can be revealed, but never put in the public page script or sent to a signed-out caller.
- Replace the boolean `isSecretProperty` with a level (for example an enum), so each property says which one it is, and apply it in `GetPropertiesActionHandler`, `MainServlet` and the Dart `PropertyHelper`, `backend_local` and both Properties screens.
- Decide whether a form's site key could instead be public, which needs no change.
