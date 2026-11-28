package com.willshex.blogwt.server.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.google.gson.JsonParser;

import com.willshex.blogwt.server.helper.PageBootstrapHelper.BootstrapData;
import com.willshex.blogwt.server.helper.PageBootstrapHelper.RenderResult;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.PostContent;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.helper.AdminPageHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;

public class PageBootstrapHelperTest {

	private static final String TEMPLATE = "<!DOCTYPE html><html><head><title>Willshex</title></head><body></body></html>";

	private static BootstrapData baseData () {
		BootstrapData data = new BootstrapData();
		data.properties = Arrays.asList(
				new Property().name(PropertyHelper.TITLE).value("WillShex"),
				new Property().name(PropertyHelper.EXTENDED_TITLE)
						.value("Software and 3D printing studio"));
		data.pages = Arrays.asList(
				new Page().slug("home").title("Home").priority(0.0f),
				new Page().slug("about").title("About").priority(1.0f),
				AdminPageHelper.create("admin-posts", "Posts", "edit", "/posts",
						AdminPageHelper.ADMIN, 3.2f));
		data.tags = Collections.emptyList();
		data.archiveEntries = Collections.emptyList();
		return data;
	}

	@Test
	public void rendersPostSeoHeadAndBootstrapJson () {
		BootstrapData data = baseData();
		Post post = new Post().slug("hello-world").title("Say Hello")
				.summary("Celebrating the *new* WillShex.")
				.tags(Collections.singletonList("jaspr"));
		post.published = Date.from(Instant.parse("2026-11-27T00:00:00Z"));
		post.author = new User().username("billy1380").forename("William")
				.surname("Shakour");
		post.content = new PostContent();
		post.content.body = "## Intro\n\n<!-- note --> and </script> inside";
		data.post = post;

		RenderResult res = PageBootstrapHelper.renderHtml(TEMPLATE,
				"https://www.willshex.com", "/post/hello-world", data);

		assertEquals(HttpServletResponse.SC_OK, res.statusCode);
		assertTrue(res.html.contains("<title>WillShex: Say Hello</title>"));
		assertTrue(res.html.contains(
				"<link rel=\"canonical\" href=\"https://www.willshex.com/post/hello-world\">"));
		assertTrue(res.html.contains(
				"<meta name=\"description\" content=\"Celebrating the new WillShex.\">"));
		assertTrue(res.html.contains(
				"<meta property=\"og:type\" content=\"article\">"));
		String open = "<script id=\"blogwt-bootstrap\" type=\"application/json\">";
		int start = res.html.indexOf(open) + open.length();
		String json = res.html.substring(start,
				res.html.indexOf("</script>", start));
		assertFalse(json.contains("<"));
		assertEquals(post.content.body,
				JsonParser.parseString(json).getAsJsonObject()
						.getAsJsonObject("post").getAsJsonObject("content")
						.get("body").getAsString());
		assertTrue("The app draws the page, not the server",
				res.html.contains("<body></body>"));
	}

	@Test
	public void onlyUnknownAddressesAreNotFound () {
		assertTrue(notFound("/nothing-here"));
		assertTrue(notFound("/nothing/here"));
		assertFalse(notFound("/login"));
		assertFalse(notFound("/user/change_password"));
		assertFalse("A site's admin page carries its own route",
				notFound("/stock"));
	}

	private static boolean notFound (String path) {
		BootstrapData data = baseData();
		data.pages = Collections.singletonList(AdminPageHelper.create(
				"admin-stock", "Stock", "edit", "/stock", AdminPageHelper.ADMIN,
				3.95f));
		PageBootstrapHelper.loadRouteData(path, data);
		return data.notFound;
	}

	@Test
	public void leavesStartUpToTheAppWhenDataIsMissing () {
		BootstrapData data = baseData();
		data.properties = null;

		RenderResult res = PageBootstrapHelper.renderHtml(TEMPLATE,
				"https://www.willshex.com", "/blog", data);

		assertFalse(res.html.contains("blogwt-bootstrap"));
	}

	@Test
	public void returns404WhenPostNotFoundAndCanonicalizesPageRoute () {
		BootstrapData missing = baseData();
		missing.notFound = true;
		RenderResult missingRes = PageBootstrapHelper.renderHtml(TEMPLATE,
				"https://www.willshex.com", "/post/does-not-exist", missing);
		assertEquals(HttpServletResponse.SC_NOT_FOUND, missingRes.statusCode);

		BootstrapData pageData = baseData();
		Page about = new Page().slug("about").title("About Us")
				.subtitle("Who we are").priority(1.0f);
		Post part = new Post();
		part.content = new PostContent();
		part.content.body = "Welcome to WillShex.";
		about.posts = Collections.singletonList(part);
		pageData.page = about;

		RenderResult pageRes = PageBootstrapHelper.renderHtml(TEMPLATE,
				"https://www.willshex.com", "/page/about", pageData);
		assertEquals(HttpServletResponse.SC_OK, pageRes.statusCode);
		assertTrue(pageRes.html.contains("<title>WillShex: About Us</title>"));
		assertTrue(pageRes.html.contains(
				"<link rel=\"canonical\" href=\"https://www.willshex.com/about\">"));
		assertTrue(pageRes.html.contains("Welcome to WillShex."));
	}
}
