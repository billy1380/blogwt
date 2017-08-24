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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.willshex.blogwt.server.helper.InlineHelper;
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

	//	private static final long TIMEOUT_MILLIS = 5000;
	//	private static final long JS_TIMEOUT_MILLIS = 2000;
	//	private static final long PAGE_WAIT_MILLIS = 100;
	//	private static final long MAX_LOOP_CHECKS = 2;

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

		String css = InlineHelper
				.css("https://fonts.googleapis.com/css?family=Noto+Sans") + "\n"
				+ InlineHelper.css(
						"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css");

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
		if (pages.size() >= 0) {
			scriptVariables.append("var pages='[")
					.append(String.join(",", pages.stream().map( (page) -> {
						page.parent = PersistenceHelper.type(Page.class,
								page.parentKey);
						return jsonForJsVar(slim(page));
					}).collect(Collectors.toList()))).append("]';");
		}
	}

	/**
	 * @param scriptVariables
	 */
	private void appendTags (StringBuffer scriptVariables) {
		List<Tag> tags = TagServiceProvider.provide().getTags();

		if (tags.size() >= 0) {
			scriptVariables.append("var tags='[")
					.append(String.join(",", tags.stream().map( (tag) -> {
						PersistenceHelper.typeList(Post.class, tag.postKeys);
						return jsonForJsVar(slim(tag));
					}).collect(Collectors.toList()))).append("]';");
		}

	}

	/**
	 * @param scriptVariables
	 */
	private void appendArchiveEntries (StringBuffer scriptVariables) {
		List<ArchiveEntry> archiveEntries = ArchiveEntryServiceProvider
				.provide().getArchiveEntries();

		if (archiveEntries.size() >= 0) {
			scriptVariables.append("var archiveEntries='[").append(String
					.join(",", archiveEntries.stream().map( (archiveEntry) -> {
						archiveEntry.posts = PersistenceHelper
								.typeList(Post.class, archiveEntry.postKeys);
						return jsonForJsVar(slim(archiveEntry));
					}).collect(Collectors.toList()))).append("]';");
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
			scriptVariables.append("var properties='[")
					.append(String.join(", ", properties.stream()
							.filter( (property) -> !PropertyHelper
									.isSecretProperty(property))
							.map( (property) -> jsonForJsVar(slim(property)))
							.collect(Collectors.toList())))
					.append("]';");
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

		response.setCharacterEncoding(ServletHelper.UTF8);
		response.setHeader("Content-Type",
				"text/html; charset=" + ServletHelper.UTF8);

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

}
