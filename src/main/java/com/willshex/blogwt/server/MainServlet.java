//
//  MainServlet.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 14 Jun 2015.
//  Copyright © 2015 WillShex Limited Ltd. All rights reserved.
//
package com.willshex.blogwt.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
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
import com.willshex.blogwt.server.service.PersistenceService;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.property.IPropertyService;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.server.service.session.ISessionService;
import com.willshex.blogwt.server.service.session.SessionServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.Session;
import com.willshex.blogwt.shared.api.helper.PropertyHelper;
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

		HttpServletRequest request = REQUEST.get();
		String fragmentParameter = request.getParameter("_escaped_fragment_");
		boolean isStatic = fragmentParameter != null;

		if (isStatic) {
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
		} else {
			IPropertyService propertyService = PropertyServiceProvider
					.provide();
			ISessionService sessionService = SessionServiceProvider.provide();

			Property title = null, extendedTitle = null, copyrightHolder = null, copyrightUrl = null;
			StringBuffer scriptVariables = new StringBuffer();
			List<Property> properties = null;
			title = propertyService.getNamedProperty(PropertyHelper.TITLE);
			Session userSession = null;

			if (title != null) {
				extendedTitle = propertyService
						.getNamedProperty(PropertyHelper.EXTENDED_TITLE);
				copyrightHolder = propertyService
						.getNamedProperty(PropertyHelper.COPYRIGHT_HOLDER);
				copyrightUrl = propertyService
						.getNamedProperty(PropertyHelper.COPYRIGHT_URL);

				properties = Arrays.asList(new Property[] { title,
						extendedTitle, copyrightHolder, copyrightUrl });

				Cookie[] cookies = request.getCookies();
				String sessionId = null;

				if (cookies != null) {
					for (Cookie currentCookie : cookies) {
						if ("session.id".equals(currentCookie.getName())) {
							sessionId = currentCookie.getValue();
							break;
						}
					}

					if (sessionId != null) {
						userSession = sessionService.getSession(Long
								.valueOf(sessionId));

						if (userSession != null) {
							if (userSession.expires.getTime() > new Date()
									.getTime()) {
								userSession = sessionService
										.extendSession(
												userSession,
												Long.valueOf(ISessionService.MILLIS_MINUTES));
								userSession.user = UserServiceProvider
										.provide().getUser(
												userSession.userKey.getId());
								userSession.user.password = null;

								if (userSession.user.roleKeys != null) {
									userSession.user.roles = RoleServiceProvider
											.provide()
											.getIdRolesBatch(
													PersistenceService
															.keysToIds(userSession.user.roleKeys));
								}

								if (userSession.user.permissionKeys != null) {
									userSession.user.permissions = PermissionServiceProvider
											.provide()
											.getIdPermissionsBatch(
													PersistenceService
															.keysToIds(userSession.user.permissionKeys));
								}
							} else {
								sessionService.deleteSession(userSession);
								userSession = null;
							}
						}
					}
				}

				if (properties != null) {
					scriptVariables.append("\nvar properties='[");
					boolean first = true;
					for (Property property : properties) {
						if (first) {
							first = false;
						} else {
							scriptVariables.append(",");
						}

						scriptVariables.append(property.toString().replace("'",
								"\\'"));
					}

					scriptVariables.append("]';\n");
				}

				if (userSession != null) {
					scriptVariables.append("var session='"
							+ userSession.toString().replace("'", "\\'")
							+ "';\n");
				}

			}

			RESPONSE.get()
					.getOutputStream()
					.print(String.format(PAGE_FORMAT, (title == null ? "Blogwt"
							: title.value), scriptVariables.toString()));
		}
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
