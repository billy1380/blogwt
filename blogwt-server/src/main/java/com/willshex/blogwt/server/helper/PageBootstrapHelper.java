//
//  PageBootstrapHelper.java
//  blogwt
//
//  Copyright © 2026 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.markdown4j.server.IncludePlugin;
import org.markdown4j.server.MarkdownProcessor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.willshex.blogwt.server.api.blog.action.GetArchiveEntriesActionHandler;
import com.willshex.blogwt.server.api.blog.action.GetPostActionHandler;
import com.willshex.blogwt.server.api.blog.action.GetPostsActionHandler;
import com.willshex.blogwt.server.api.blog.action.GetPropertiesActionHandler;
import com.willshex.blogwt.server.api.blog.action.GetTagsActionHandler;
import com.willshex.blogwt.server.api.page.action.GetPageActionHandler;
import com.willshex.blogwt.server.api.page.action.GetPagesActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.blog.call.GetArchiveEntriesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetArchiveEntriesResponse;
import com.willshex.blogwt.shared.api.blog.call.GetPostRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostResponse;
import com.willshex.blogwt.shared.api.blog.call.GetPostsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostsResponse;
import com.willshex.blogwt.shared.api.blog.call.GetPropertiesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPropertiesResponse;
import com.willshex.blogwt.shared.api.blog.call.GetTagsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetTagsResponse;
import com.willshex.blogwt.shared.api.datatype.ArchiveEntry;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.PostSortType;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.Session;
import com.willshex.blogwt.shared.api.datatype.Tag;
import com.willshex.blogwt.shared.api.page.call.GetPageRequest;
import com.willshex.blogwt.shared.api.page.call.GetPageResponse;
import com.willshex.blogwt.shared.api.page.call.GetPagesRequest;
import com.willshex.blogwt.shared.api.page.call.GetPagesResponse;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.blogwt.shared.helper.AdminPageHelper;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.gson.web.service.shared.Error;
import com.willshex.gson.web.service.shared.StatusType;

/** Adds startup data, SEO tags and fallback HTML to the web app's index. */
public class PageBootstrapHelper {

	private static final Logger LOG = Logger
			.getLogger(PageBootstrapHelper.class.getName());

	public static final String BOOTSTRAP_SCRIPT_ID = "blogwt-bootstrap";
	public static final String SSR_CONTAINER_ID = "ssr-content";

	private static final Pattern TITLE_PATTERN = Pattern.compile(
			"<title>.*?</title>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	private static final Pattern HEAD_CLOSE_PATTERN = Pattern.compile(
			"</head>", Pattern.CASE_INSENSITIVE);
	private static final Pattern BODY_OPEN_PATTERN = Pattern.compile(
			"<body[^>]*>", Pattern.CASE_INSENSITIVE);
	private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<[^>]+>");
	private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE
			.withZone(ZoneOffset.UTC);

	/** First path segments of the web app's own screens; a site adds its own. */
	public static final Set<String> appRoutes = new HashSet<>(Arrays.asList(
			"404", "blog", "comments", "devtools", "downloads",
			"metanotification", "metanotifications", "page", "pageedit", "pages",
			"permissions", "post", "postcol", "postedit", "posts", "properties",
			"register", "resetpassword", "resourceedit", "resources", "roles",
			"search", "setup", "login", "logout", "tag", "user", "users",
			"verifyaccount", "widgettest"));

	private static volatile String cachedBuildVersion;

	public static class BootstrapData {
		public Session session;
		public JsonObject sessionJson;
		public List<Property> properties;
		public List<Page> pages;
		public List<Tag> tags;
		public List<ArchiveEntry> archiveEntries;
		public Page page;
		public Post post;
		public String postsPath;
		public List<Post> posts;
		public String tagSlug;
		public boolean notFound;
	}

	public static class RenderResult {
		public final int statusCode;
		public final String html;
		public final boolean hasSession;

		public RenderResult (int statusCode, String html, boolean hasSession) {
			this.statusCode = statusCode;
			this.html = html;
			this.hasSession = hasSession;
		}
	}

	public static RenderResult render (HttpServletRequest req,
			ServletContext ctx, String indexTemplate, String requestPath) {
		String normalizedPath = normalizePath(requestPath);
		String baseUrl = ServletHelper.canonicalBaseUrl(req);
		BootstrapData data = loadData(req, ctx, normalizedPath);
		return renderHtml(indexTemplate, baseUrl, normalizedPath, data);
	}

	private static BootstrapData loadData (HttpServletRequest req,
			ServletContext ctx, String path) {
		BootstrapData data = new BootstrapData();

		try {
			data.session = ServletHelper.session(req);
			// taken now, as later lookups strip this same cached user
			data.sessionJson = data.session == null ? null
					: data.session.toJson();
		} catch (Exception e) {
			LOG.log(Level.FINE, "Could not load session cookie", e);
		}

		try {
			GetPropertiesRequest propsReq = new GetPropertiesRequest();
			propsReq.accessCode = ApiValidator.STATIC_ACCESS_CODE;
			propsReq.session = data.session;
			propsReq.pager = PagerHelper.createInfinitePager();
			GetPropertiesResponse propsResp = new GetPropertiesActionHandler()
					.handle(propsReq);
			if (propsResp != null
					&& propsResp.status == StatusType.StatusTypeSuccess
					&& propsResp.properties != null) {
				data.properties = new ArrayList<>(propsResp.properties);
				GetPropertiesActionHandler.appendBuildVersion(data.properties,
						PagerHelper.DEFAULT_START, readBuildVersion(ctx));
			}
		} catch (Exception e) {
			LOG.log(Level.WARNING, "Could not load bootstrap properties", e);
		}

		try {
			GetPagesRequest pagesReq = new GetPagesRequest()
					.includePosts(Boolean.FALSE);
			pagesReq.accessCode = ApiValidator.STATIC_ACCESS_CODE;
			pagesReq.session = data.session;
			pagesReq.pager = PagerHelper.createInfinitePager();
			GetPagesResponse pagesResp = new GetPagesActionHandler()
					.handle(pagesReq);
			if (pagesResp != null
					&& pagesResp.status == StatusType.StatusTypeSuccess) {
				data.pages = pagesResp.pages;
			}
		} catch (Exception e) {
			LOG.log(Level.WARNING, "Could not load bootstrap pages", e);
		}

		try {
			GetTagsRequest tagsReq = new GetTagsRequest();
			tagsReq.accessCode = ApiValidator.STATIC_ACCESS_CODE;
			tagsReq.session = data.session;
			GetTagsResponse tagsResp = new GetTagsActionHandler()
					.handle(tagsReq);
			if (tagsResp != null
					&& tagsResp.status == StatusType.StatusTypeSuccess) {
				data.tags = tagsResp.tags;
			}
		} catch (Exception e) {
			LOG.log(Level.WARNING, "Could not load bootstrap tags", e);
		}

		try {
			GetArchiveEntriesRequest archiveReq = new GetArchiveEntriesRequest();
			archiveReq.accessCode = ApiValidator.STATIC_ACCESS_CODE;
			archiveReq.session = data.session;
			GetArchiveEntriesResponse archiveResp = new GetArchiveEntriesActionHandler()
					.handle(archiveReq);
			if (archiveResp != null
					&& archiveResp.status == StatusType.StatusTypeSuccess) {
				data.archiveEntries = archiveResp.archive;
			}
		} catch (Exception e) {
			LOG.log(Level.WARNING, "Could not load bootstrap archive entries",
					e);
		}

		loadRouteData(path, data);
		return data;
	}

	static void loadRouteData (String path, BootstrapData data) {
		try {
			if ("/".equals(path)) {
				Page home = findHomePage(data.pages);
				if (home != null && home.slug != null) {
					data.page = loadPage(home.slug, data.session);
				} else {
					data.postsPath = "/blog";
					data.posts = loadPosts(null, data.session);
				}
				return;
			}

			String trimmed = path.startsWith("/") ? path.substring(1) : path;
			String[] segments = trimmed.split("/");

			if (segments.length == 2 && "post".equals(segments[0])
					&& !segments[1].isEmpty()) {
				loadRoutePost(segments[1], data);
				return;
			}

			if (segments.length == 1 && "blog".equals(segments[0])) {
				data.postsPath = "/blog";
				data.posts = loadPosts(null, data.session);
				return;
			}

			if (segments.length == 2 && "tag".equals(segments[0])
					&& !segments[1].isEmpty()) {
				data.notFound = isUnknownTag(data.tags, segments[1]);
				if (data.notFound) {
					return;
				}
				data.tagSlug = segments[1];
				data.postsPath = "/tag/" + segments[1];
				data.posts = loadPosts(segments[1], data.session);
				return;
			}

			if (segments.length == 2 && "page".equals(segments[0])
					&& !segments[1].isEmpty()) {
				loadRoutePage(segments[1], data);
				return;
			}

			Page matched = segments.length == 1
					? findContentPage(data.pages, segments[0])
					: null;
			if (matched != null) {
				data.page = loadPage(matched.slug, data.session);
			} else {
				data.notFound = data.pages != null
						&& !isAppRoute(segments[0], data.pages);
			}
		} catch (Exception e) {
			LOG.log(Level.WARNING,
					"Could not load route bootstrap data for " + path, e);
		}
	}

	private static GetPageResponse getPage (String slug, Session session) {
		GetPageRequest req = new GetPageRequest().includePosts(Boolean.TRUE)
				.page(new Page().slug(slug));
		req.accessCode = ApiValidator.STATIC_ACCESS_CODE;
		req.session = session;
		return new GetPageActionHandler().handle(req);
	}

	private static Page loadPage (String slug, Session session) {
		GetPageResponse resp = getPage(slug, session);
		return resp.status == StatusType.StatusTypeSuccess ? resp.page : null;
	}

	private static void loadRoutePage (String slug, BootstrapData data) {
		GetPageResponse resp = getPage(slug, data.session);
		if (resp.status != StatusType.StatusTypeSuccess) {
			data.notFound = isMissing(resp.error);
		} else if (AdminPageHelper.isAdminPage(resp.page)) {
			data.notFound = true;
		} else {
			data.page = resp.page;
		}
	}

	private static void loadRoutePost (String slug, BootstrapData data) {
		GetPostRequest req = new GetPostRequest().post(new Post().slug(slug));
		req.accessCode = ApiValidator.STATIC_ACCESS_CODE;
		req.session = data.session;
		GetPostResponse resp = new GetPostActionHandler().handle(req);
		if (resp.status == StatusType.StatusTypeSuccess) {
			data.post = resp.post;
		} else {
			data.notFound = isMissing(resp.error);
		}
	}

	// any other failure may pass, so it must not be reported as a 404
	static boolean isMissing (Error error) {
		return error != null && error.code != null
				&& (error.code.intValue() == ApiError.DataTypeNotFound.getCode()
						|| error.code.intValue() == ApiError.UnpublishedPost
								.getCode());
	}

	private static boolean isAppRoute (String segment, List<Page> pages) {
		String route = "/" + segment;
		return appRoutes.contains(segment) || pages.stream()
				.anyMatch(page -> page != null && page.route != null
						&& (page.route.equals(route)
								|| page.route.startsWith(route + "/")));
	}

	private static boolean isUnknownTag (List<Tag> tags, String slug) {
		return tags != null && tags.stream()
				.noneMatch(tag -> tag != null && slug.equals(tag.slug));
	}

	private static List<Post> loadPosts (String tag, Session session) {
		GetPostsRequest req = new GetPostsRequest()
				.includePostContents(Boolean.TRUE)
				.pager(PagerHelper.createDefaultPager()
						.count(Integer.valueOf(
								PagerHelper.DEFAULT_COUNT.intValue() + 1))
						.sortBy(PostSortType.PostSortTypePublished.toString())
						.sortDirection(
								SortDirectionType.SortDirectionTypeDescending));
		if (tag != null && !tag.isEmpty()) {
			req.tag(tag);
		}
		req.accessCode = ApiValidator.STATIC_ACCESS_CODE;
		req.session = session;
		GetPostsResponse resp = new GetPostsActionHandler().handle(req);
		return resp != null && resp.status == StatusType.StatusTypeSuccess
				? resp.posts
				: null;
	}

	public static RenderResult renderHtml (String indexTemplate, String baseUrl,
			String requestPath, BootstrapData data) {
		String path = normalizePath(requestPath);
		String base = baseUrl.endsWith("/")
				? baseUrl.substring(0, baseUrl.length() - 1)
				: baseUrl;

		Map<String, Property> props = data.properties == null ? null
				: PropertyHelper.toLookup(data.properties);

		String siteTitle = propValue(props, PropertyHelper.TITLE, "Willshex");
		String extendedTitle = propValue(props, PropertyHelper.EXTENDED_TITLE,
				"");
		String rssProp = propValue(props, PropertyHelper.GENERATE_RSS_FEED,
				null);
		boolean rssEnabled = rssProp == null
				|| Boolean.TRUE.toString().equalsIgnoreCase(rssProp);

		String routeTitle = resolveRouteTitle(siteTitle, path, data);
		String fullTitle = routeTitle == null || routeTitle.isEmpty()
				? siteTitle
				: siteTitle + ": " + routeTitle;
		String description = resolveDescription(extendedTitle, data);
		String canonicalUrl = resolveCanonicalUrl(base, path, data);
		String ogType = data.post != null ? "article" : "website";

		StringBuilder headInject = new StringBuilder();
		if (description != null && !description.isEmpty()) {
			headInject.append("  <meta name=\"description\" content=\"")
					.append(escapeAttr(description)).append("\">\n");
		}
		headInject.append("  <link rel=\"canonical\" href=\"")
				.append(escapeAttr(canonicalUrl)).append("\">\n");
		headInject.append("  <meta property=\"og:site_name\" content=\"")
				.append(escapeAttr(siteTitle)).append("\">\n");
		headInject.append("  <meta property=\"og:title\" content=\"")
				.append(escapeAttr(fullTitle)).append("\">\n");
		if (description != null && !description.isEmpty()) {
			headInject.append("  <meta property=\"og:description\" content=\"")
					.append(escapeAttr(description)).append("\">\n");
		}
		headInject.append("  <meta property=\"og:url\" content=\"")
				.append(escapeAttr(canonicalUrl)).append("\">\n");
		headInject.append("  <meta property=\"og:type\" content=\"")
				.append(ogType).append("\">\n");
		headInject.append("  <meta name=\"twitter:card\" content=\"summary\">\n");
		headInject.append("  <meta name=\"twitter:title\" content=\"")
				.append(escapeAttr(fullTitle)).append("\">\n");
		if (description != null && !description.isEmpty()) {
			headInject.append("  <meta name=\"twitter:description\" content=\"")
					.append(escapeAttr(description)).append("\">\n");
		}
		if (rssEnabled) {
			headInject.append(
					"  <link rel=\"alternate\" type=\"application/rss+xml\" title=\"")
					.append(escapeAttr(siteTitle + " (RSS feed)"))
					.append("\" href=\"/feed\">\n");
		}

		if (isComplete(data)) {
			String bootstrapJson = escapeScriptJson(
					buildBootstrapJson(path, data).toString());
			headInject.append("  <script id=\"").append(BOOTSTRAP_SCRIPT_ID)
					.append("\" type=\"application/json\">")
					.append(bootstrapJson).append("</script>\n");
		}

		String ssrBody = buildSsrBody(siteTitle, extendedTitle, props, data);

		String html = replaceTitle(indexTemplate, fullTitle);
		html = injectBeforeHeadClose(html, headInject.toString());
		html = injectAfterBodyOpen(html, ssrBody);

		int status = data.notFound ? HttpServletResponse.SC_NOT_FOUND
				: HttpServletResponse.SC_OK;
		return new RenderResult(status, html, data.session != null);
	}

	// without all of it the app has to do its own start up
	private static boolean isComplete (BootstrapData data) {
		return data.properties != null && !data.properties.isEmpty()
				&& data.pages != null && data.tags != null
				&& data.archiveEntries != null;
	}

	static JsonObject buildBootstrapJson (String path, BootstrapData data) {
		JsonObject root = new JsonObject();
		root.addProperty("path", path);

		if (data.sessionJson != null) {
			root.add("session", data.sessionJson);
		}

		JsonArray propsArr = new JsonArray();
		if (data.properties != null) {
			for (Property p : data.properties) {
				if (p != null) {
					propsArr.add(p.toJson());
				}
			}
		}
		root.add("properties", propsArr);

		JsonArray pagesArr = new JsonArray();
		if (data.pages != null) {
			for (Page p : data.pages) {
				if (p != null) {
					pagesArr.add(p.toJson());
				}
			}
		}
		root.add("pages", pagesArr);

		JsonArray tagsArr = new JsonArray();
		if (data.tags != null) {
			for (Tag t : data.tags) {
				if (t != null) {
					tagsArr.add(t.toJson());
				}
			}
		}
		root.add("tags", tagsArr);

		JsonArray archiveArr = new JsonArray();
		if (data.archiveEntries != null) {
			for (ArchiveEntry a : data.archiveEntries) {
				if (a != null) {
					archiveArr.add(a.toJson());
				}
			}
		}
		root.add("archiveEntries", archiveArr);

		if (data.page != null) {
			root.add("page", data.page.toJson());
		}

		if (data.post != null) {
			root.add("post", data.post.toJson());
		}

		if (data.postsPath != null && data.posts != null) {
			root.addProperty("postsPath", data.postsPath);
			JsonArray postsArr = new JsonArray();
			for (Post p : data.posts) {
				if (p != null) {
					postsArr.add(p.toJson());
				}
			}
			root.add("posts", postsArr);
		}

		return root;
	}

	private static String buildSsrBody (String siteTitle, String extendedTitle,
			Map<String, Property> props, BootstrapData data) {
		MarkdownProcessor md = new MarkdownProcessor();
		md.registerPlugins(new IncludePlugin());

		StringBuilder sb = new StringBuilder();
		sb.append("\n  <div id=\"").append(SSR_CONTAINER_ID)
				.append("\" class=\"container py-4\">\n");
		sb.append("    <header class=\"mb-4\">\n");
		sb.append("      <nav aria-label=\"Primary\">\n");
		sb.append("        <a href=\"/\"><strong>")
				.append(escapeHtml(siteTitle)).append("</strong></a>");

		if (data.pages != null) {
			for (Page page : data.pages) {
				if (page == null || page.priority == null
						|| AdminPageHelper.isAdminPage(page)
						|| page.slug == null || page.slug.isEmpty()
						|| page.priority.floatValue() == 0.0f
						|| page.parent != null) {
					continue;
				}
				sb.append(" · <a href=\"/").append(escapeAttr(page.slug))
						.append("\">")
						.append(escapeHtml(page.title == null ? page.slug
								: page.title))
						.append("</a>");
			}
		}
		sb.append(" · <a href=\"/blog\">Blog</a>\n");
		sb.append("      </nav>\n");
		sb.append("    </header>\n");

		sb.append("    <main>\n");
		if (data.post != null) {
			appendPostHtml(sb, md, data.post);
		} else if (data.page != null) {
			appendPageHtml(sb, md, data.page);
		} else if (data.posts != null) {
			String heading = data.tagSlug != null ? "Tag: " + data.tagSlug
					: "Blog";
			sb.append("      <h1>").append(escapeHtml(heading))
					.append("</h1>\n");
			for (Post post : data.posts) {
				if (post == null || post.slug == null) {
					continue;
				}
				sb.append("      <article class=\"mb-4\">\n");
				sb.append("        <h2><a href=\"/post/")
						.append(escapeAttr(post.slug)).append("\">")
						.append(escapeHtml(post.title == null ? post.slug
								: post.title))
						.append("</a></h2>\n");
				if (post.summary != null && !post.summary.isEmpty()) {
					sb.append("        <div>")
							.append(processMarkdown(md, post.summary))
							.append("</div>\n");
				}
				sb.append("      </article>\n");
			}
		} else if (data.notFound) {
			sb.append("      <h1>Page not found</h1>\n");
		} else {
			sb.append("      <h1>").append(escapeHtml(siteTitle))
					.append("</h1>\n");
			if (extendedTitle != null && !extendedTitle.isEmpty()) {
				sb.append("      <p>").append(escapeHtml(extendedTitle))
						.append("</p>\n");
			}
		}
		sb.append("    </main>\n");

		String holder = propValue(props, PropertyHelper.COPYRIGHT_HOLDER,
				siteTitle);
		String holderUrl = propValue(props, PropertyHelper.COPYRIGHT_URL, "/");
		sb.append("    <footer class=\"mt-5 pt-3 border-top\">\n");
		sb.append("      <small>Copyright &copy; <a href=\"")
				.append(escapeAttr(holderUrl)).append("\">")
				.append(escapeHtml(holder)).append("</a> ")
				.append(LocalDate.now(ZoneOffset.UTC).getYear())
				.append(".</small>\n");
		sb.append("    </footer>\n");
		sb.append("  </div>\n");

		return sb.toString();
	}

	private static void appendPostHtml (StringBuilder sb, MarkdownProcessor md,
			Post post) {
		sb.append("      <article>\n");
		sb.append("        <h1>")
				.append(escapeHtml(post.title == null ? "" : post.title))
				.append("</h1>\n");
		if (post.published != null || post.author != null) {
			sb.append("        <p>");
			if (post.published != null) {
				sb.append("<time datetime=\"")
						.append(DATE_FORMAT.format(post.published.toInstant()))
						.append("\">")
						.append(DATE_FORMAT.format(post.published.toInstant()))
						.append("</time>");
			}
			if (post.author != null) {
				String handle = UserHelper.handle(post.author);
				if (handle != null && !handle.isEmpty()) {
					if (post.published != null) {
						sb.append(" · ");
					}
					sb.append("by ").append(escapeHtml(handle));
				}
			}
			sb.append("</p>\n");
		}
		if (post.content != null && post.content.body != null) {
			sb.append("        <div>")
					.append(processMarkdown(md, post.content.body))
					.append("</div>\n");
		} else if (post.summary != null) {
			sb.append("        <div>")
					.append(processMarkdown(md, post.summary))
					.append("</div>\n");
		}
		if (post.tags != null && !post.tags.isEmpty()) {
			sb.append("        <ul>");
			for (String tag : post.tags) {
				if (tag == null || tag.isEmpty()) {
					continue;
				}
				sb.append("<li><a href=\"/tag/").append(escapeAttr(tag))
						.append("\">").append(escapeHtml(tag))
						.append("</a></li>");
			}
			sb.append("</ul>\n");
		}
		sb.append("      </article>\n");
	}

	private static void appendPageHtml (StringBuilder sb, MarkdownProcessor md,
			Page page) {
		if (page.title != null && !page.title.isEmpty()) {
			sb.append("      <h1>").append(escapeHtml(page.title))
					.append("</h1>\n");
		}
		if (page.subtitle != null && !page.subtitle.isEmpty()) {
			sb.append("      <p>").append(escapeHtml(page.subtitle))
					.append("</p>\n");
		}
		if (page.posts != null) {
			for (Post part : page.posts) {
				if (part != null && part.content != null
						&& part.content.body != null) {
					sb.append("      <section>")
							.append(processMarkdown(md, part.content.body))
							.append("</section>\n");
				}
			}
		}
	}

	private static String processMarkdown (MarkdownProcessor md,
			String markdown) {
		if (markdown == null || markdown.isEmpty()) {
			return "";
		}
		try {
			return md.process(markdown);
		} catch (IOException e) {
			LOG.log(Level.FINE, "Markdown rendering failed for SSR fallback",
					e);
			return escapeHtml(markdown);
		}
	}

	private static String resolveRouteTitle (String siteTitle, String path,
			BootstrapData data) {
		if ("/".equals(path)) {
			return null;
		}
		if (data.post != null && data.post.title != null
				&& !data.post.title.isEmpty()) {
			return data.post.title;
		}
		if (data.page != null && data.page.title != null
				&& !data.page.title.isEmpty()) {
			return data.page.title;
		}
		if ("/blog".equals(path)) {
			return "Blog";
		}
		if (data.tagSlug != null && !data.tagSlug.isEmpty()) {
			return data.tagSlug;
		}
		if (data.notFound) {
			return "Not Found";
		}
		return null;
	}

	private static String resolveDescription (String extendedTitle,
			BootstrapData data) {
		String raw = null;
		if (data.post != null) {
			if (data.post.summary != null && !data.post.summary.isEmpty()) {
				raw = data.post.summary;
			} else if (data.post.content != null
					&& data.post.content.body != null) {
				raw = data.post.content.body;
			}
		} else if (data.page != null) {
			if (data.page.subtitle != null && !data.page.subtitle.isEmpty()) {
				raw = data.page.subtitle;
			} else if (data.page.posts != null) {
				for (Post part : data.page.posts) {
					if (part != null && part.summary != null
							&& !part.summary.isEmpty()) {
						raw = part.summary;
						break;
					}
					if (part != null && part.content != null
							&& part.content.body != null
							&& !part.content.body.isEmpty()) {
						raw = part.content.body;
						break;
					}
				}
			}
		}

		if (raw == null || raw.isEmpty()) {
			raw = extendedTitle;
		}
		return toPlainExcerpt(raw, 180);
	}

	private static String resolveCanonicalUrl (String base, String path,
			BootstrapData data) {
		if ("/".equals(path)) {
			return base + "/";
		}
		if (data.post != null && data.post.slug != null) {
			return base + "/post/" + data.post.slug;
		}
		if (data.page != null && data.page.slug != null) {
			if (data.page.priority != null
					&& data.page.priority.floatValue() == 0.0f) {
				return base + "/";
			}
			return base + "/" + data.page.slug;
		}
		return base + path;
	}

	private static Page findHomePage (List<Page> pages) {
		if (pages == null) {
			return null;
		}
		for (Page page : pages) {
			if (page != null && !AdminPageHelper.isAdminPage(page)
					&& page.priority != null
					&& page.priority.floatValue() == 0.0f) {
				return page;
			}
		}
		return null;
	}

	private static Page findContentPage (List<Page> pages, String slug) {
		if (pages == null || slug == null) {
			return null;
		}
		for (Page page : pages) {
			if (page != null && !AdminPageHelper.isAdminPage(page)
					&& slug.equals(page.slug)) {
				return page;
			}
		}
		return null;
	}

	private static String readBuildVersion (ServletContext ctx) {
		if (cachedBuildVersion == null && ctx != null) {
			try (InputStream in = ctx
					.getResourceAsStream("/META-INF/MANIFEST.MF")) {
				cachedBuildVersion = GetPropertiesActionHandler
						.readImplementationBuild(in);
			} catch (IOException e) {
				LOG.log(Level.FINE, "Could not read build version", e);
			}
		}
		return cachedBuildVersion;
	}

	private static String propValue (Map<String, Property> props, String key,
			String fallback) {
		if (props == null) {
			return fallback;
		}
		String val = PropertyHelper.value(props.get(key));
		if (val == null || val.isEmpty()
				|| PropertyHelper.NONE_VALUE.equals(val)) {
			return fallback;
		}
		return val;
	}

	private static String normalizePath (String path) {
		if (path == null || path.isEmpty()) {
			return "/";
		}
		int q = path.indexOf('?');
		String clean = q >= 0 ? path.substring(0, q) : path;
		if (!clean.startsWith("/")) {
			clean = "/" + clean;
		}
		while (clean.length() > 1 && clean.endsWith("/")) {
			clean = clean.substring(0, clean.length() - 1);
		}
		return "/index.html".equals(clean) ? "/" : clean;
	}

	private static String replaceTitle (String html, String title) {
		String replacement = "<title>" + escapeHtml(title) + "</title>";
		Matcher m = TITLE_PATTERN.matcher(html);
		if (m.find()) {
			return m.replaceFirst(Matcher.quoteReplacement(replacement));
		}
		return injectBeforeHeadClose(html, "  " + replacement + "\n");
	}

	private static String injectBeforeHeadClose (String html, String snippet) {
		Matcher m = HEAD_CLOSE_PATTERN.matcher(html);
		if (m.find()) {
			return m.replaceFirst(
					Matcher.quoteReplacement(snippet + m.group()));
		}
		return html + snippet;
	}

	private static String injectAfterBodyOpen (String html, String snippet) {
		Matcher m = BODY_OPEN_PATTERN.matcher(html);
		if (m.find()) {
			return m.replaceFirst(
					Matcher.quoteReplacement(m.group() + snippet));
		}
		return html + snippet;
	}

	// "<" only occurs inside strings, where this is valid JSON for it
	static String escapeScriptJson (String json) {
		return json.replace("<", "\\u003c");
	}

	static String toPlainExcerpt (String text, int maxLen) {
		if (text == null || text.isEmpty()) {
			return "";
		}
		// Strip HTML tags and common markdown syntax characters.
		String plain = HTML_TAG_PATTERN.matcher(text).replaceAll(" ");
		plain = plain.replaceAll("\\[([^\\]]+)\\]\\([^)]+\\)", "$1");
		plain = plain.replaceAll("[#*_`>~]+", " ");
		plain = WHITESPACE_PATTERN.matcher(plain).replaceAll(" ").trim();
		if (plain.length() <= maxLen) {
			return plain;
		}
		int cut = plain.lastIndexOf(' ', maxLen - 1);
		if (cut < maxLen / 2) {
			cut = maxLen - 1;
		}
		return plain.substring(0, cut).trim() + "…";
	}

	static String escapeHtml (String value) {
		if (value == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder(value.length());
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			switch (c) {
			case '&':
				sb.append("&amp;");
				break;
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

	static String escapeAttr (String value) {
		return escapeHtml(value).replace("\"", "&quot;");
	}
}
