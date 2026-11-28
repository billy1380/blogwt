package com.willshex.blogwt.server;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.Tag;
import com.willshex.blogwt.shared.helper.AdminPageHelper;

public class SiteMapServletTest {

	@Test
	public void emitsCleanPathsAndExcludesAdminPagesAndHomeDuplicate () {
		Page home = new Page().slug("home").title("Home").priority(0.0f);
		Page about = new Page().slug("about").title("About").priority(1.0f);
		Page unlisted = new Page().slug("hidden").title("Hidden");
		Page admin = AdminPageHelper.create("admin-users", "Users", "face",
				"/users", AdminPageHelper.ADMIN, 3.4f);

		Post post = new Post().slug("hello-world").title("Hello");
		post.published = Date.from(Instant.parse("2026-11-27T12:00:00Z"));

		Tag tag = new Tag().slug("flutter").name("flutter");

		StringWriter sw = new StringWriter();
		SiteMapServlet.writeSiteMap(new PrintWriter(sw),
				"https://www.willshex.com",
				Arrays.asList(home, about, unlisted, admin),
				Collections.singletonList(post),
				Collections.singletonList(tag),
				List.of("/shop", "shop/product/42"));

		String xml = sw.toString();

		assertFalse("Must not emit legacy #! hashbang URLs",
				xml.contains("#!"));
		assertTrue(xml.contains("<loc>https://www.willshex.com/</loc>"));
		assertTrue(xml.contains("<loc>https://www.willshex.com/about</loc>"));
		assertTrue(xml.contains("<loc>https://www.willshex.com/blog</loc>"));
		assertTrue(xml.contains(
				"<loc>https://www.willshex.com/post/hello-world</loc><lastmod>2026-11-27</lastmod>"));
		assertTrue(
				xml.contains("<loc>https://www.willshex.com/tag/flutter</loc>"));
		assertTrue(xml.contains("<loc>https://www.willshex.com/shop</loc>"));
		assertTrue(xml.contains(
				"<loc>https://www.willshex.com/shop/product/42</loc>"));

		assertFalse("Must not duplicate home page slug",
				xml.contains("/home</loc>"));
		assertFalse("Must not include admin pages",
				xml.contains("admin-users"));
		assertFalse("Must not include pages without priority",
				xml.contains("/hidden</loc>"));
	}
}
