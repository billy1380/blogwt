# To do

## Load post contents in one batch when listing pages

`PageService.populatePostContents` (`blogwt-server/.../service/page/PageService.java`) loads each post's content with its own datastore read (see the `FIXME: just load in one go (batch)` there). `GetPages` with `includePosts` set does that for every part of every page.

`PostService` already batches the same lookup in `getUserViewablePosts` and a second method that lists posts: collect the content ids, load them with `loadContent().ids(...)`, assign each post its content.

- Add a public method on `IPostService` / `PostService` that fills in the content of a list of posts in one batch, and use it from `PageService`.
- Skip a post whose `contentKey` is `null`; the existing copies of the loop do not guard it.
- The two existing copies of the loop in `PostService` could use the new method too.

The Dart clients no longer ask for the parts to build the menu (`GetPages` sends the part ids instead), so this only matters for callers that really want the page text.
