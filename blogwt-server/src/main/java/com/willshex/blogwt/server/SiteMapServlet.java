//
//  SiteMapServlet.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 22 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

import com.willshex.blogwt.server.helper.ServletHelper;
import com.willshex.blogwt.server.service.page.PageServiceProvider;
import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.server.service.tag.TagServiceProvider;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.PageSortType;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.PostSortType;
import com.willshex.blogwt.shared.api.datatype.Tag;
import com.willshex.blogwt.shared.helper.AdminPageHelper;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.server.ContextAwareServlet;

/**
 * @author William Shakour (billy1380)
 *
 */
@WebServlet(name = "Site Map", urlPatterns = { SiteMapServlet.URL,
		SiteMapServlet.URL + ".xml" })
public class SiteMapServlet extends ContextAwareServlet {
	private static final long serialVersionUID = -1809926195158756299L;

	public static final String URL = "/sitemap";

	private static final String MIME_TYPE = "application/xml; charset=UTF-8";
	private static final DateTimeFormatter LASTMOD_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE
			.withZone(ZoneOffset.UTC);

	/** A site's own paths, such as {@code /shop}. */
	public static final List<Supplier<Collection<String>>> extraPaths = new ArrayList<>();

	@Override
	protected void doGet () throws ServletException, IOException {
		super.doGet();

		String url = ServletHelper.canonicalBaseUrl(REQUEST.get());

		HttpServletResponse response = RESPONSE.get();
		response.setContentType(MIME_TYPE);
		PrintWriter p = response.getWriter();

		List<Page> pages = PageServiceProvider.provide().getPages(Boolean.FALSE,
				Integer.valueOf(0), null, PageSortType.PageSortTypePriority,
				null);
		List<Post> posts = loadAllListedPosts();
		List<Tag> tags = TagServiceProvider.provide().getTags();
		List<String> extras = new ArrayList<>();
		for (Supplier<Collection<String>> supplier : extraPaths) {
			Collection<String> paths = supplier.get();
			if (paths != null) {
				extras.addAll(paths);
			}
		}

		writeSiteMap(p, url, pages, posts, tags, extras);
		p.close();
	}

	@Override
	protected void doPost () throws ServletException, IOException {
		doGet();
	}

	private static List<Post> loadAllListedPosts () {
		List<Post> all = new ArrayList<>();
		List<Post> batch;
		Pager pager = PagerHelper.createDefaultPager();

		do {
			batch = PostServiceProvider.provide().getPosts(Boolean.FALSE,
					Boolean.FALSE, pager.start, pager.count,
					PostSortType.PostSortTypePublished,
					SortDirectionType.SortDirectionTypeDescending);

			if (batch != null) {
				PagerHelper.moveForward(pager);
				all.addAll(batch);
			}
		} while (batch != null && batch.size() >= pager.count.intValue());

		return all;
	}

	public static void writeSiteMap (PrintWriter p, String baseUrl,
			List<Page> pages, List<Post> posts, List<Tag> tags,
			Collection<String> extras) {
		String base = baseUrl.endsWith("/")
				? baseUrl.substring(0, baseUrl.length() - 1)
				: baseUrl;

		p.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		p.println(
				"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");

		Set<String> seen = new LinkedHashSet<>();

		printUrl(p, seen, base + "/", null);

		if (pages != null) {
			for (Page page : pages) {
				if (page == null || page.priority == null
						|| AdminPageHelper.isAdminPage(page)
						|| page.slug == null || page.slug.isEmpty()) {
					continue;
				}
				// Priority 0 is the home page, already listed at "/".
				if (page.priority.floatValue() == 0.0f) {
					continue;
				}
				printUrl(p, seen, base + "/" + page.slug, null);
			}
		}

		printUrl(p, seen, base + "/blog", null);

		if (posts != null) {
			for (Post post : posts) {
				if (post == null || post.slug == null || post.slug.isEmpty()) {
					continue;
				}
				printUrl(p, seen, base + "/post/" + post.slug, post.published);
			}
		}

		if (tags != null) {
			for (Tag tag : tags) {
				if (tag == null || tag.slug == null || tag.slug.isEmpty()) {
					continue;
				}
				printUrl(p, seen, base + "/tag/" + tag.slug, null);
			}
		}

		if (extras != null) {
			for (String path : extras) {
				if (path == null || path.isEmpty()) {
					continue;
				}
				String normalized = path.startsWith("/") ? path : "/" + path;
				printUrl(p, seen, base + normalized, null);
			}
		}

		p.println("</urlset>");
	}

	private static void printUrl (PrintWriter p, Set<String> seen, String loc,
			Date lastMod) {
		if (!seen.add(loc)) {
			return;
		}
		p.print("    <url><loc>");
		p.print(escapeXml(loc));
		p.print("</loc>");
		if (lastMod != null) {
			p.print("<lastmod>");
			p.print(LASTMOD_FORMAT.format(lastMod.toInstant()));
			p.print("</lastmod>");
		}
		p.println("</url>");
	}

	static String escapeXml (String value) {
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
			case '"':
				sb.append("&quot;");
				break;
			case '\'':
				sb.append("&apos;");
				break;
			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}
}
