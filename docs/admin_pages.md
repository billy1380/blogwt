# Admin menu pages

The admin menu is data: `Page` entities with a `route`, created when the blog is set up, instead of a list hard-coded in each client.

## The model

- `Page.route` (new, in `api_defintion.json`, the Java `Page` and the Dart one): when set, the page is an admin menu item, not content. It is the client route the item opens. An **empty** route is a group that only folds its children.
- `parent` makes sub items and groups. `priority` orders siblings. `title`, `subtitle` and `icon` are what the menu shows.
- The server does **not** decide who sees an item. The clients already know which permission or role each route needs (`PageType`), so they show an item when the signed in user may open its route, and a group or item when any part of it is permitted.
- Admin pages are not listed with content pages (`PagesLoaded.showablePages` leaves them out; `adminPages` returns them).

## Where they come from

- `AdminPageHelper` (`blogwt-shared`) lists the pages. Pages have slugs starting `admin-`.
- `SetupBlogActionHandler` adds them at the end of setup, owned by the first user.
- Dev servlet: `/dev?action=fixadminpages` adds any that are missing. It matches by slug and never overwrites, so it is safe to run again and edits made afterwards are kept. Run it once on a site that was set up before this existed.
- A site built on blogwt adds items by putting a `Supplier<Collection<Page>>` in `AdminPageHelper.extensions` (parents before children, a parent is named by slug).

## Changing a menu item

Edit the page (title, icon, order, parent) like any other page. Deleting an admin page removes the item until `fixadminpages` is run again.

## Not done

- The page editor still lists admin pages with the content pages on the Pages screen; there is nothing to stop one being edited as if it had content.
- The old GWT client has its own menu code and does not read these pages.
