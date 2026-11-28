//
//  MainServlet.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 15 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.willshex.blogwt.server.helper.PageBootstrapHelper;
import com.willshex.blogwt.server.helper.PageBootstrapHelper.RenderResult;
import com.willshex.server.ContextAwareServlet;

/**
 * Serves the client app: its index page, filled in for the route, and files as
 * they are.
 *
 * @author William Shakour (billy1380)
 *
 */
@WebServlet(name = "Site", urlPatterns = { MainServlet.URL,
		MainServlet.ALT_URL })
public class MainServlet extends HttpServlet {

	private static final long serialVersionUID = 9125652318292184721L;

	public static final String ALT_URL = "/";
	public static final String URL = "";

	private static final String INDEX = "/index.html";

	private static final Logger LOG = Logger
			.getLogger(MainServlet.class.getName());

	@Override
	protected void doGet (HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String path = req.getRequestURI();
		if (path == null || path.isEmpty()) {
			path = "/";
		}

		if (isUnsafePath(path)) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		String name = path.substring(path.lastIndexOf('/') + 1);

		if (INDEX.equals(path)) {
			sendApp(req, resp, path);
		} else if (name.indexOf('.') == -1) {
			if (!"/".equals(path)) {
				String dir = path.endsWith("/") ? path : path + "/";
				String staticIndex = dir + "index.html";

				try (InputStream in = getServletContext()
						.getResourceAsStream(staticIndex)) {
					if (in != null) {
						if (!path.endsWith("/")) {
							String query = req.getQueryString();
							resp.sendRedirect(query == null || query.isEmpty()
									? dir
									: dir + "?" + query);
						} else {
							sendStream(in, "text/html; charset=UTF-8", resp);
						}
						return;
					}
				}
			}

			sendApp(req, resp, path);
		} else {
			send(path, getServletContext().getMimeType(path), resp);
		}
	}

	private void sendApp (HttpServletRequest req, HttpServletResponse resp,
			String path) throws IOException {
		String template;
		try (InputStream in = getServletContext().getResourceAsStream(INDEX)) {
			if (in == null) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			copy(in, bytes);
			template = new String(bytes.toByteArray(), StandardCharsets.UTF_8);
		}

		String html = template;
		try {
			RenderResult result = render(req, resp, template, path);
			resp.setStatus(result.statusCode);
			if (result.hasSession) {
				resp.setHeader("Cache-Control", "private, no-store");
			}
			html = result.html;
		} catch (RuntimeException e) {
			// the app can still start itself from the plain page
			LOG.log(Level.SEVERE, "Could not render " + path, e);
		}

		resp.setContentType("text/html; charset=UTF-8");
		resp.getOutputStream().write(html.getBytes(StandardCharsets.UTF_8));
	}

	protected RenderResult render (HttpServletRequest req,
			HttpServletResponse resp, String template, String path) {
		// the data layer picks its namespace from the current request
		ContextAwareServlet.REQUEST.set(req);
		ContextAwareServlet.RESPONSE.set(resp);
		ContextAwareServlet.CONTEXT.set(getServletContext());
		try {
			return PageBootstrapHelper.render(req, getServletContext(),
					template, decode(path));
		} finally {
			ContextAwareServlet.REQUEST.remove();
			ContextAwareServlet.RESPONSE.remove();
			ContextAwareServlet.CONTEXT.remove();
		}
	}

	private static String decode (String path) {
		try {
			return URLDecoder.decode(path.replace("+", "%2B"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	private static boolean isUnsafePath (String path) {
		if (!path.startsWith("/") || path.contains("..")) {
			return true;
		}
		String upper = path.toUpperCase();
		return upper.equals("/WEB-INF") || upper.startsWith("/WEB-INF/");
	}

	private void send (String path, String type, HttpServletResponse resp)
			throws IOException {
		try (InputStream in = getServletContext().getResourceAsStream(path)) {
			if (in == null) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			sendStream(in, type, resp);
		}
	}

	private static void sendStream (InputStream in, String type,
			HttpServletResponse resp) throws IOException {
		if (type != null) {
			resp.setContentType(type);
		}

		copy(in, resp.getOutputStream());
	}

	private static void copy (InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[8192];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}
}
