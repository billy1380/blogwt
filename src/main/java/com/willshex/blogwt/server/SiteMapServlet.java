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
import java.util.List;

import javax.servlet.ServletException;
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
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.service.ContextAwareServlet;

/**
 * @author William Shakour (billy1380)
 *
 */
public class SiteMapServlet extends ContextAwareServlet {

	private static final long serialVersionUID = 3133978953838954164L;
	private static final String MIME_TYPE = "application/xml; charset=UTF-8";
	private static final String LOC_FORMAT = "    <url><loc>%s/%s</loc></url>";

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.service.ContextAwareServlet#doGet() */
	@Override
	protected void doGet () throws ServletException, IOException {
		super.doGet();

		String url = ServletHelper.constructBaseUrl(REQUEST.get());

		HttpServletResponse response = RESPONSE.get();
		PrintWriter p = response.getWriter();

		response.setContentType(MIME_TYPE);

		p.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		p.println("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
		
		printPages(p, url);
		printBlog(p, url);
		printPosts(p, url);
		printTags(p, url);

		
		p.println("</urlset>");

		p.close();
	}

	protected void doPost () throws ServletException, IOException {
		doGet();
	}

	private void printBlog (PrintWriter p, String url) {
		p.println(String.format(LOC_FORMAT, url,
				"#" + PageType.PostsPageType.asTargetHistoryToken()));
	}

	private void printPosts (PrintWriter p, String url) {
		List<Post> posts;
		Pager pager = PagerHelper.createDefaultPager();

		do {
			posts = PostServiceProvider.provide().getPosts(Boolean.FALSE,
					Boolean.FALSE, pager.start, pager.count,
					PostSortType.PostSortTypePublished,
					SortDirectionType.SortDirectionTypeDescending);

			if (posts != null) {
				PagerHelper.moveForward(pager);

				for (Post post : posts) {
					p.println(String.format(
							LOC_FORMAT,
							url,
							"#"
									+ PageType.PostsPageType
											.asTargetHistoryToken(post.slug)));
				}
			}
		} while (posts != null && posts.size() >= pager.count.intValue());
	}

	private void printPages (PrintWriter p, String url) {
		List<Page> pages = PageServiceProvider.provide().getPages(
				Boolean.FALSE, Integer.valueOf(0), null,
				PageSortType.PageSortTypePriority, null);

		if (pages != null) {
			for (Page page : pages) {
				p.println(String.format(LOC_FORMAT, url, "#!" + page.slug));
			}
		}
	}

	private void printTags (PrintWriter p, String url) {
		List<Tag> tags = TagServiceProvider.provide().getTags();

		if (tags.size() >= 0) {
			for (Tag tag : tags) {
				p.println(String.format(
						LOC_FORMAT,
						url,
						"#"
								+ PageType.TagPostsPageType
										.asTargetHistoryToken(tag.slug)));
			}
		}

	}
}
