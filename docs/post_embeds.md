# Post embeds

A post body can embed another post's body on the server using a processing instruction outside markdown:

```xml
<?post id="123" ?>
```

The directive is expanded at read and search-index time (`PostEmbedHelper` in `blogwt-server`); nothing extra is stored in the data model.

## Same-author rule

- **Same author only:** a referenced post is only expanded when its author matches the enclosing post's author (`authorKey` / `author.id`).
- Any directive referencing a post by a **different author** is stripped (replaced with an empty string), with no error or placeholder.
- Because the author must match, drafts and unlisted posts written by the same author expand normally — this is the intended way to keep shared sections or reusable snippets without publishing them separately.

## Expansion rules

- **Syntax:** `<?post id="123" ?>` (single or double quotes, optional whitespace around attributes and before `?>`, case-insensitive tag/attribute names).
- **Invalid or missing targets:** missing posts, non-numeric or missing `id` attributes, or empty bodies are stripped silently.
- **Nesting, cycles and depth:** embedded posts may themselves embed other posts by the same author. Self-references, indirect cycles, and nesting beyond a depth of `5` (`PostEmbedHelper.MAX_DEPTH`) are stripped.
- **Code blocks and inline code:** directives inside fenced code blocks (`` ``` `` or `~~~`) or inline backtick spans (`` `...` ``) are left untouched so examples can be documented verbatim.

## Where expansion runs

- `GetPostActionHandler` — expands unless `GetPostRequest.processTags` is `false` (defaults to `true` when omitted).
- `GetPostsActionHandler` — expands when `includePostContents` is `true`.
- `PageService.populatePostContents` — expands posts attached to pages (covering `GetPage`, `GetPages`, `StaticPage`, and page search indexing).
- `PostService.toDocument` — indexes the expanded body into the searchable `body` field.

## Editing (`GetPostRequest.processTags`)

Pass `processTags: false` on `GetPostRequest` when loading a post for editing so the server skips server-side tag processing and returns the original body with `<?post id="..." ?>` directives intact rather than overwriting them with the expanded text on save.
