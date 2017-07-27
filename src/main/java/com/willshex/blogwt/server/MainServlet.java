//
//  MainServlet.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 14 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.memcache.AsyncMemcacheService;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.server.helper.ServletHelper;
import com.willshex.blogwt.server.page.PageMarkup;
import com.willshex.blogwt.server.page.PageMarkupFactory;
import com.willshex.blogwt.server.service.archiveentry.ArchiveEntryServiceProvider;
import com.willshex.blogwt.server.service.page.PageServiceProvider;
import com.willshex.blogwt.server.service.property.IPropertyService;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.server.service.tag.TagServiceProvider;
import com.willshex.blogwt.shared.api.datatype.ArchiveEntry;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.PageSortType;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.Session;
import com.willshex.blogwt.shared.api.datatype.Tag;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.blogwt.shared.page.Stack;
import com.willshex.gson.shared.Jsonable;
import com.willshex.server.ContextAwareServlet;

/**
 * @author William Shakour (billy1380)
 *
 */
@WebServlet(name = "Site", urlPatterns = "/")
public class MainServlet extends ContextAwareServlet {

	private static final long serialVersionUID = 3007918530671674098L;

	private static String PAGE_FORMAT = null;
	private static final MemcacheService CACHE = MemcacheServiceFactory
			.getMemcacheService();
	private static final AsyncMemcacheService ASYNC_CACHE = MemcacheServiceFactory
			.getAsyncMemcacheService();

	//	private static final long TIMEOUT_MILLIS = 5000;
	//	private static final long JS_TIMEOUT_MILLIS = 2000;
	//	private static final long PAGE_WAIT_MILLIS = 100;
	//	private static final long MAX_LOOP_CHECKS = 2;
	private static final String CHAR_ENCODING = "UTF-8";

	private static final String RSS_LINK_FORMAT = "<link rel=\"alternate\" type=\"application/rss+xml\" title=\"%s\" href=\"/feed\" />";
	private static final String FAVICON_FORMAT = "<link rel=\"icon\" href=\"%s\" type=\"image/x-icon\">";

	static {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
				MainServlet.class.getResourceAsStream("res/index.html")))) {
			StringBuffer contents = new StringBuffer();
			int length;
			char string[] = new char[1024];

			while ((length = reader.read(string)) != -1) {
				contents.append(string, 0, length);
			}

			PAGE_FORMAT = contents.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.service.ContextAwareServlet#doGet() */
	@Override
	protected void doGet () throws ServletException, IOException {
		super.doGet();

		if (isStatic()) {
			processStaticRequest();
		} else {
			processRequest();
		}
	}

	/**
	 * @throws IOException 
	 * 
	 */
	private void processRequest () throws IOException {
		IPropertyService propertyService = PropertyServiceProvider.provide();

		StringBuffer scriptVariables = new StringBuffer();
		Property title = propertyService.getNamedProperty(PropertyHelper.TITLE);
		List<Property> properties = null;
		Map<String, Property> propertyLookup = null;

		if (title != null) {
			appendSession(scriptVariables);
			properties = appendProperties(scriptVariables);
			appendPages(scriptVariables);
			appendTags(scriptVariables);
			appendArchiveEntries(scriptVariables);
		}

		String pageTitle = (title == null ? "Blogwt" : title.value);
		String rssLink = "", faviconLink = null, googleAnalyticsSnippet = "";
		String rssPropertyValue = null, faviconPropertyValue = null,
				googleAnalyticsPropertyValue = null;

		if (properties != null) {
			propertyLookup = PropertyHelper.toLookup(properties);

			rssPropertyValue = PropertyHelper.value(
					propertyLookup.get(PropertyHelper.GENERATE_RSS_FEED));

			faviconPropertyValue = PropertyHelper
					.value(propertyLookup.get(PropertyHelper.FAVICON_URL));

			googleAnalyticsPropertyValue = PropertyHelper.value(
					propertyLookup.get(PropertyHelper.GOOGLE_ANALYTICS_KEY));
		}

		if (rssPropertyValue == null
				|| Boolean.TRUE.toString().equals(rssPropertyValue)) {
			rssLink = String.format(RSS_LINK_FORMAT, pageTitle + " (RSS feed)");
		}

		if (faviconPropertyValue == null
				|| PropertyHelper.NONE_VALUE.equals(faviconPropertyValue)) {
			faviconLink = String.format(FAVICON_FORMAT, "favicon.ico");
		} else {
			faviconLink = String.format(FAVICON_FORMAT, faviconPropertyValue);
		}

		if (googleAnalyticsPropertyValue != null && !PropertyHelper.NONE_VALUE
				.equals(googleAnalyticsPropertyValue)) {
			googleAnalyticsSnippet = "<!-- Google Analytics -->\n"
					+ "<script>\n"
					+ "window.ga=window.ga||function(){(ga.q=ga.q||[]).push(arguments)};ga.l=+new Date;\n"
					+ "ga('create', '" + googleAnalyticsPropertyValue
					+ "', 'auto');\n" + "</script>\n"
					+ "<script async src='https://www.google-analytics.com/analytics.js'></script>\n"
					+ "<!-- End Google Analytics -->";
		}

		String css = css("https://fonts.googleapis.com/css?family=Noto+Sans")
				+ "\n"
				+ css("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css");

		RESPONSE.get().getOutputStream()
				.print(String.format(PAGE_FORMAT, googleAnalyticsSnippet, css,
						rssLink, faviconLink, pageTitle,
						scriptVariables.toString()));

	}

	/**
	 * @param scriptVariables
	 */
	private void appendPages (StringBuffer scriptVariables) {
		List<Page> pages = PageServiceProvider.provide().getPages(Boolean.FALSE,
				Integer.valueOf(0), null, PageSortType.PageSortTypePriority,
				null);
		if (pages != null) {
			scriptVariables.append("var pages='[");

			boolean first = true;
			for (Page page : pages) {
				if (first) {
					first = false;
				} else {
					scriptVariables.append(",");
				}

				if (page.parentKey != null) {
					page.parent = PersistenceHelper.type(Page.class,
							page.parentKey);
				}

				scriptVariables.append(jsonForJsVar(slim(page)));
			}
		}

		scriptVariables.append("]';");
	}

	/**
	 * @param scriptVariables
	 */
	private void appendTags (StringBuffer scriptVariables) {
		List<Tag> tags = TagServiceProvider.provide().getTags();

		if (tags.size() >= 0) {
			scriptVariables.append("var tags='[");

			boolean first = true;
			for (Tag tag : tags) {
				tag.posts = PersistenceHelper.typeList(Post.class,
						tag.postKeys);

				if (first) {
					first = false;
				} else {
					scriptVariables.append(",");
				}

				scriptVariables.append(jsonForJsVar(slim(tag)));
			}

			scriptVariables.append("]';");
		}
	}

	/**
	 * @param scriptVariables
	 */
	private void appendArchiveEntries (StringBuffer scriptVariables) {
		List<ArchiveEntry> archiveEntries = ArchiveEntryServiceProvider
				.provide().getArchiveEntries();

		if (archiveEntries.size() >= 0) {
			scriptVariables.append("var archiveEntries='[");

			boolean first = true;
			for (ArchiveEntry archiveEntry : archiveEntries) {
				archiveEntry.posts = PersistenceHelper.typeList(Post.class,
						archiveEntry.postKeys);

				if (first) {
					first = false;
				} else {
					scriptVariables.append(",");
				}

				scriptVariables.append(jsonForJsVar(slim(archiveEntry)));
			}

			scriptVariables.append("]';");
		}
	}

	/**
	 * @param scriptVariables
	 * @return 
	 */
	private List<Property> appendProperties (StringBuffer scriptVariables) {
		List<Property> properties = PropertyServiceProvider.provide()
				.getProperties(0, 10000, null, null);

		if (properties.size() >= 0) {
			scriptVariables.append("var properties='[");

			boolean first = true;
			for (Property property : properties) {
				if (PropertyHelper.isSecretProperty(property)) continue;

				if (first) {
					first = false;
				} else {
					scriptVariables.append(",");
				}

				scriptVariables.append(jsonForJsVar(slim(property)));
			}

			scriptVariables.append("]';");
		}

		return properties;
	}

	/**
	 * @param scriptVariables 
	 * 
	 */
	private Session appendSession (StringBuffer scriptVariables) {
		Session userSession = ServletHelper.session(REQUEST.get());

		if (userSession != null) {
			scriptVariables.append(
					"var session='" + jsonForJsVar(slim(userSession)) + "';");
		}

		return userSession;
	}

	/**
	 * @return
	 */
	private boolean isStatic () {
		return REQUEST.get().getParameter("_escaped_fragment_") != null;
	}

	/**
	 * @param fragmentParameter 
	 * @param request 
	 * @throws IOException 
	 * @throws FailingHttpStatusCodeException 
	 * 
	 */
	private void processStaticRequest () throws IOException {
		HttpServletRequest request = REQUEST.get();
		String fragmentParameter = request.getParameter("_escaped_fragment_");

		Stack s = Stack.parse(fragmentParameter);
		PageMarkup p = PageMarkupFactory.createFromStack(s);

		HttpServletResponse response = RESPONSE.get();

		response.setCharacterEncoding(CHAR_ENCODING);
		response.setHeader("Content-Type",
				"text/html; charset=" + CHAR_ENCODING);

		if (p != null) {
			response.getWriter().print(p.asString());
		}
	}

	private String jsonForJsVar (Jsonable jsonable) {
		return null == jsonable ? null
				: jsonable.toString().replace("'", "\\'")
						.replace("\\n", "\\\\n").replace("\\\"", "\\\\\"");
	}

	private Property slim (Property property) {
		return (Property) (new Property().name(property.name)
				.value(property.value)
				.created(PropertyHelper.TITLE.equals(property.name)
						? property.created
						: null));
	}

	private Tag slim (Tag tag) {
		return new Tag().name(tag.name).slug(tag.slug).posts(tag.posts);
	}

	private ArchiveEntry slim (ArchiveEntry archiveEntry) {
		return new ArchiveEntry().month(archiveEntry.month)
				.year(archiveEntry.year).posts(archiveEntry.posts);
	}

	private Page slim (Page page) {
		return (Page) (new Page().owner(page.owner)
				.hasChildren(page.hasChildren).parent(page.parent)
				.priority(page.priority).slug(page.slug).title(page.title)
				.id(page.id));
	}

	private Session slim (Session session) {
		return new Session().expires(session.expires).user(session.user);
	}

	public static String css (String url) {
		String css = (String) CACHE.get(url);

		if (css == null) {
			css = "";

			try {
				HTTPRequest request = new HTTPRequest(new URL(url),
						HTTPMethod.GET);
				URLFetchService client = URLFetchServiceFactory
						.getURLFetchService();
				HTTPResponse response = client.fetch(request);

				byte[] responseBytes;
				String responseText = null;

				if ((responseBytes = response.getContent()) != null) {
					responseText = new String(responseBytes, CHAR_ENCODING);
				}

				if (response.getResponseCode() >= 200
						&& response.getResponseCode() < 300
						&& responseText != null) {
					css = "<style>" + fixRelativeUrls(responseText, url)
							+ "</style>";
					ASYNC_CACHE.put(url, css);
				}
			} catch (IOException ex) {
				// could not get it on the server, let the browser sort it out
				css = "<link rel=\"stylesheet\" href=\"" + url + "\">";
			}
		}

		return css;
	}

	private static String fixRelativeUrls (String content, String url) {
		java.util.Stack<String> p = pathStack(url);
		p.pop();
		return content.replace("url(.", "url(" + String.join("/", p) + "/.");
	}

	private static java.util.Stack<String> pathStack (String path) {
		java.util.Stack<String> p = new java.util.Stack<>();
		p.addAll(Arrays.asList(path.split("/")));
		return p;
	}

}
