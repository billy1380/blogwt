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
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.spacehopperstudios.utility.StringUtils;
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.server.service.PersistenceService;
import com.willshex.blogwt.server.service.page.PageServiceProvider;
import com.willshex.blogwt.server.service.property.IPropertyService;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.server.service.session.ISessionService;
import com.willshex.blogwt.server.service.session.SessionServiceProvider;
import com.willshex.blogwt.server.service.tag.TagServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.PageSortType;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.Session;
import com.willshex.blogwt.shared.api.datatype.Tag;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.service.ContextAwareServlet;

/**
 * @author William Shakour (billy1380)
 *
 */
public class MainServlet extends ContextAwareServlet {

	private static final long serialVersionUID = 3007918530671674098L;

	private static String PAGE_FORMAT = null;

	private static final long TIMEOUT_MILLIS = 5000;
	private static final long JS_TIMEOUT_MILLIS = 2000;
	private static final long PAGE_WAIT_MILLIS = 100;
	private static final long MAX_LOOP_CHECKS = 2;
	private static final String CHAR_ENCODING = "UTF-8";

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

		Property title = null;
		StringBuffer scriptVariables = new StringBuffer();
		title = propertyService.getNamedProperty(PropertyHelper.TITLE);

		if (title != null) {
			appendSession(scriptVariables);
			scriptVariables.append("\n");
			appendProperties(scriptVariables);
			scriptVariables.append("\n");
			appendPages(scriptVariables);
			scriptVariables.append("\n");
			appendTags(scriptVariables);
			scriptVariables.append("\n");
		}

		RESPONSE.get()
				.getOutputStream()
				.print(String.format(PAGE_FORMAT, (title == null ? "Blogwt"
						: title.value), scriptVariables.toString()));

	}

	/**
	 * @param scriptVariables
	 */
	private void appendPages (StringBuffer scriptVariables) {
		List<Page> pages = PageServiceProvider.provide().getPages(
				Boolean.FALSE, Integer.valueOf(0), null,
				PageSortType.PageSortTypePriority, null);

		if (pages != null) {
			scriptVariables.append("var pages='[");

			boolean first = true;
			for (Page page : pages) {
				if (first) {
					first = false;
				} else {
					scriptVariables.append(",");
				}
				scriptVariables.append(page.toString().replace("'", "\\'"));
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
				tag.posts = PersistenceService.dataTypeList(Post.class,
						tag.postKeys);

				if (first) {
					first = false;
				} else {
					scriptVariables.append(",");
				}

				scriptVariables.append(tag.toString().replace("'", "\\'"));
			}

			scriptVariables.append("]';");
		}
	}

	/**
	 * @param scriptVariables
	 * @param properties
	 */
	private void appendProperties (StringBuffer scriptVariables) {
		List<Property> properties = PropertyServiceProvider.provide()
				.getProperties();

		if (properties.size() >= 0) {
			scriptVariables.append("var properties='[");

			boolean first = true;
			for (Property property : properties) {
				if (first) {
					first = false;
				} else {
					scriptVariables.append(",");
				}

				scriptVariables.append(property.toString().replace("'", "\\'"));
			}

			scriptVariables.append("]';");
		}
	}

	/**
	 * @param scriptVariables 
	 * 
	 */
	private void appendSession (StringBuffer scriptVariables) {
		ISessionService sessionService = SessionServiceProvider.provide();
		Session userSession = null;
		Cookie[] cookies = REQUEST.get().getCookies();
		String sessionId = null;

		if (cookies != null) {
			for (Cookie currentCookie : cookies) {
				if ("session.id".equals(currentCookie.getName())) {
					sessionId = currentCookie.getValue();
					break;
				}
			}

			if (sessionId != null) {
				userSession = sessionService
						.getSession(Long.valueOf(sessionId));

				if (userSession != null) {
					if (userSession.expires.getTime() > new Date().getTime()) {
						userSession = sessionService.extendSession(userSession,
								Long.valueOf(ISessionService.MILLIS_MINUTES));
						userSession.user = UserServiceProvider.provide()
								.getUser(userSession.userKey.getId());
						UserHelper.stripPassword(userSession.user);
						UserHelper.addRolesAndPermissions(userSession.user);
					} else {
						sessionService.deleteSession(userSession);
						userSession = null;
					}
				}
			}
		}

		if (userSession != null) {
			scriptVariables.append("var session='"
					+ userSession.toString().replace("'", "\\'") + "';");
		}
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
	private void processStaticRequest () throws FailingHttpStatusCodeException,
			IOException {
		HttpServletRequest request = REQUEST.get();
		String fragmentParameter = request.getParameter("_escaped_fragment_");

		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		String uri = request.getRequestURI();
		String url = scheme + "://" + serverName + ":" + serverPort + uri
				+ "#!" + StringUtils.urldecode(fragmentParameter);

		HttpServletResponse response = RESPONSE.get();

		response.setCharacterEncoding(CHAR_ENCODING);
		response.setHeader("Content-Type", "text/plain; charset="
				+ CHAR_ENCODING);
		response.getOutputStream().print(staticContent(url));
	}

	/**
	 * This method as far as I can tell does not work with the gwt version of htmlunit 
	 * will probably either need to use GWTP crawler service or restructure this project
	 * to use modules and roll my own again either with GWTP or just the method below
	 * @return
	 * @throws IOException 
	 * @throws FailingHttpStatusCodeException 
	 */
	private String staticContent (String url)
			throws FailingHttpStatusCodeException, IOException {
		// code based on https://github.com/ArcBees/GWTP/blob/master/gwtp-crawler-service/src/main/java/com/gwtplatform/crawlerservice/server/CrawlServiceServlet.java
		WebClient webClient = new WebClient();

		webClient.getCache().clear();
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController() {
			private static final long serialVersionUID = 2875888832992558703L;

			/* (non-Javadoc)
			 * 
			 * @see com.gargoylesoftware.htmlunit.
			 * NicelyResynchronizingAjaxController #processSynchron(com
			 * .gargoylesoftware.htmlunit.html.HtmlPage,
			 * com.gargoylesoftware.htmlunit.WebRequest, boolean) */
			@Override
			public boolean processSynchron (HtmlPage page, WebRequest settings,
					boolean async) {
				return true;
			}
		});
		webClient.setCssErrorHandler(new SilentCssErrorHandler());

		WebRequest webRequest = new WebRequest(new URL(url), "text/html");
		HtmlPage page = webClient.getPage(webRequest);
		webClient.getJavaScriptEngine().pumpEventLoop(TIMEOUT_MILLIS);

		int waitForBackgroundJavaScript = webClient
				.waitForBackgroundJavaScript(JS_TIMEOUT_MILLIS);
		int loopCount = 0;

		while (waitForBackgroundJavaScript > 0 && loopCount < MAX_LOOP_CHECKS) {
			++loopCount;
			waitForBackgroundJavaScript = webClient
					.waitForBackgroundJavaScript(JS_TIMEOUT_MILLIS);

			if (waitForBackgroundJavaScript == 0) {
				break;
			}

			synchronized (page) {
				try {
					page.wait(PAGE_WAIT_MILLIS);
				} catch (InterruptedException e) {}
			}
		}

		webClient.closeAllWindows();

		return Pattern
				.compile("<style>.*?</style>", Pattern.DOTALL)
				.matcher(
						page.asXml().replace(
								"<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
								"")).replaceAll("");
	}
}
