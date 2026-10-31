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
- Level 2, masked: sent to the client like any property, shown as a password box in the Properties screens. `recaptcha.site.key` is this (the contact form needs it in the browser).
- Done so far: `isSecretProperty` is level 1 and `isMaskedProperty` (level 1 plus the site key) drives the password boxes, in Java and Dart.
- Still to do: a real level (for example an enum) on the property itself instead of a list of names; a way to reveal a masked value; and a level between the two that sends the value only to signed-in admins.
