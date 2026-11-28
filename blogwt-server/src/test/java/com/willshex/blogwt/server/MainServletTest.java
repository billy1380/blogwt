package com.willshex.blogwt.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.willshex.blogwt.server.helper.PageBootstrapHelper.RenderResult;

public class MainServletTest {

	private Map<String, String> resources;
	private MainServlet servlet;
	private RenderResult rendered;
	private boolean renderFails;

	@Before
	public void setUp () throws Exception {
		resources = new HashMap<>();
		resources.put("/index.html", "<html>spa</html>");
		resources.put("/first/index.html", "<html>first</html>");
		resources.put("/first/css/general.css", "body {}");
		resources.put("/apps/second/index.html",
				"<html>second</html>");
		resources.put("/apps/third/index.html", "<html>third</html>");

		ServletContext context = (ServletContext) Proxy.newProxyInstance(
				ServletContext.class.getClassLoader(),
				new Class<?>[] { ServletContext.class }, (proxy, method, args) -> {
					switch (method.getName()) {
					case "getResourceAsStream":
						String content = resources.get(args[0]);
						return content == null ? null
								: new ByteArrayInputStream(content
										.getBytes(StandardCharsets.UTF_8));
					case "getMimeType":
						String path = (String) args[0];
						return path.endsWith(".css") ? "text/css" : "text/html";
					default:
						return null;
					}
				});

		ServletConfig config = (ServletConfig) Proxy.newProxyInstance(
				ServletConfig.class.getClassLoader(),
				new Class<?>[] { ServletConfig.class }, (proxy, method, args) -> {
					if ("getServletContext".equals(method.getName())) {
						return context;
					}
					return null;
				});

		servlet = new MainServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			protected RenderResult render (HttpServletRequest req,
					HttpServletResponse resp, String template, String path) {
				if (renderFails) {
					throw new IllegalStateException("no datastore");
				}
				return rendered != null ? rendered
						: new RenderResult(200, template, false);
			}
		};
		servlet.init(config);
	}

	@Test
	public void testRenderedStatusAndPrivacyReachTheResponse ()
			throws Exception {
		rendered = new RenderResult(404, "<html>missing</html>", true);

		ResponseCapture missing = get("/post/nope", null);

		Assert.assertEquals("<html>missing</html>", missing.body());
		Assert.assertEquals(404, missing.status);
		Assert.assertEquals("private, no-store",
				missing.headers.get("Cache-Control"));
	}

	@Test
	public void testFailedRenderServesThePlainPage () throws Exception {
		renderFails = true;

		Assert.assertEquals("<html>spa</html>", get("/blog", null).body());
	}

	@Test
	public void testRootAndAppRoutesServeSpaIndex () throws Exception {
		ResponseCapture root = get("/", null);
		Assert.assertEquals("<html>spa</html>", root.body());
		Assert.assertEquals("text/html; charset=UTF-8", root.contentType);

		ResponseCapture contact = get("/contact", null);
		Assert.assertEquals("<html>spa</html>", contact.body());
		Assert.assertNull(contact.redirect);

		ResponseCapture tools = get("/tools/string", null);
		Assert.assertEquals("<html>spa</html>", tools.body());
	}

	@Test
	public void testStaticDirectoryWithTrailingSlashServesStaticIndex ()
			throws Exception {
		ResponseCapture first = get("/first/", null);
		Assert.assertEquals("<html>first</html>", first.body());
		Assert.assertEquals("text/html; charset=UTF-8",
				first.contentType);

		ResponseCapture second = get("/apps/second/", null);
		Assert.assertEquals("<html>second</html>", second.body());

		ResponseCapture third = get("/apps/third/", null);
		Assert.assertEquals("<html>third</html>", third.body());
	}

	@Test
	public void testStaticDirectoryWithoutTrailingSlashRedirects ()
			throws Exception {
		ResponseCapture first = get("/first", null);
		Assert.assertEquals("/first/", first.redirect);
		Assert.assertEquals("", first.body());

		ResponseCapture second = get("/apps/second", "from=home");
		Assert.assertEquals("/apps/second/?from=home",
				second.redirect);

		ResponseCapture third = get("/apps/third", null);
		Assert.assertEquals("/apps/third/", third.redirect);
	}

	@Test
	public void testStaticAssetsServedDirectly () throws Exception {
		ResponseCapture css = get("/first/css/general.css", null);
		Assert.assertEquals("body {}", css.body());
		Assert.assertEquals("text/css", css.contentType);
	}

	@Test
	public void testUnsafePathsReturnNotFound () throws Exception {
		Assert.assertEquals(HttpServletResponse.SC_NOT_FOUND,
				get("/WEB-INF/web.xml", null).errorStatus);
		Assert.assertEquals(HttpServletResponse.SC_NOT_FOUND,
				get("/WEB-INF/hidden", null).errorStatus);
		Assert.assertEquals(HttpServletResponse.SC_NOT_FOUND,
				get("/first/../WEB-INF/web.xml", null).errorStatus);
	}

	private ResponseCapture get (String uri, String query) throws Exception {
		ResponseCapture capture = new ResponseCapture();

		HttpServletRequest req = (HttpServletRequest) Proxy.newProxyInstance(
				HttpServletRequest.class.getClassLoader(),
				new Class<?>[] { HttpServletRequest.class },
				(proxy, method, args) -> {
					switch (method.getName()) {
					case "getRequestURI":
						return uri;
					case "getQueryString":
						return query;
					default:
						return null;
					}
				});

		HttpServletResponse resp = (HttpServletResponse) Proxy.newProxyInstance(
				HttpServletResponse.class.getClassLoader(),
				new Class<?>[] { HttpServletResponse.class },
				(proxy, method, args) -> {
					switch (method.getName()) {
					case "setContentType":
						capture.contentType = (String) args[0];
						return null;
					case "getOutputStream":
						return capture.stream;
					case "sendRedirect":
						capture.redirect = (String) args[0];
						return null;
					case "setStatus":
						capture.status = (Integer) args[0];
						return null;
					case "setHeader":
						capture.headers.put((String) args[0], (String) args[1]);
						return null;
					case "sendError":
						capture.errorStatus = (Integer) args[0];
						return null;
					default:
						return null;
					}
				});

		servlet.doGet(req, resp);
		return capture;
	}

	private static class ResponseCapture {
		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		final ServletOutputStream stream = new ServletOutputStream() {
			@Override
			public boolean isReady () {
				return true;
			}

			@Override
			public void setWriteListener (WriteListener writeListener) {
			}

			@Override
			public void write (int b) throws IOException {
				bytes.write(b);
			}
		};
		String contentType;
		String redirect;
		int errorStatus;
		int status;
		final Map<String, String> headers = new HashMap<>();

		String body () {
			return bytes.toString(StandardCharsets.UTF_8);
		}
	}
}
