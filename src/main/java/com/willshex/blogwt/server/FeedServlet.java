//
//  FeedServlet.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 22 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.markdown4j.server.MarkdownProcessor;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;
import com.willshex.blogwt.server.helper.ServletHelper;
import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.PostSortType;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.server.ContextAwareServlet;

/**
 * @author William Shakour (billy1380)
 *
 *	Based on https://rometools.github.io/rome/RssAndAtOMUtilitiEsROMEV0.5AndAboveTutorialsAndArticles/RssAndAtOMUtilitiEsROMEV0.5TutorialUsingROMEWithinAServletToCreateAndReturnAFeed.html
 */
@WebServlet(name = "Feed", urlPatterns = "/feed", initParams = {
		@WebInitParam(name = "default.feed.type", value = "rss_2.0") })
public class FeedServlet extends ContextAwareServlet {

	private static final long serialVersionUID = 3740371198321150900L;

	private static final String DEFAULT_FEED_TYPE = "default.feed.type";
	private static final String FEED_TYPE = "type";
	private static final String MIME_TYPE = "application/xml; charset=UTF-8";

	private String defaultFeedType;

	public void init () {
		defaultFeedType = getServletConfig()
				.getInitParameter(DEFAULT_FEED_TYPE);
		defaultFeedType = (defaultFeedType != null) ? defaultFeedType
				: "atom_0.3";
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.service.ContextAwareServlet#doGet() */
	@Override
	protected void doGet () throws ServletException, IOException {
		super.doGet();

		Property generateRss = PropertyServiceProvider.provide()
				.getNamedProperty(PropertyHelper.GENERATE_RSS_FEED);

		if (PropertyHelper.isEmpty(generateRss)
				|| Boolean.valueOf(generateRss.value).booleanValue()) {

			HttpServletRequest request = REQUEST.get();
			HttpServletResponse response = RESPONSE.get();

			try {
				SyndFeed feed = getFeed(request);

				String feedType = request.getParameter(FEED_TYPE);
				feedType = (feedType != null) ? feedType : defaultFeedType;
				feed.setFeedType(feedType);

				response.setContentType(MIME_TYPE);
				SyndFeedOutput output = new SyndFeedOutput();
				output.output(feed, response.getWriter());
			} catch (FeedException ex) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"Could not generate feed");
			}
		}
	}

	protected SyndFeed getFeed (HttpServletRequest request)
			throws IOException, FeedException {
		SyndFeed feed = new SyndFeedImpl();
		String url = ServletHelper.constructBaseUrl(request);

		feed.setTitle(PropertyServiceProvider.provide()
				.getNamedProperty(PropertyHelper.TITLE).value);
		feed.setLink(url + "/feed");
		feed.setDescription(PropertyServiceProvider.provide()
				.getNamedProperty(PropertyHelper.EXTENDED_TITLE).value);

		List<Post> posts;
		Pager pager = PagerHelper.createDefaultPager();

		List<SyndEntry> entries = new ArrayList<SyndEntry>();
		SyndEntry entry;
		SyndContent description;
		MarkdownProcessor processor = new MarkdownProcessor();

		do {
			posts = PostServiceProvider.provide().getPosts(Boolean.FALSE,
					Boolean.FALSE, pager.start, pager.count,
					PostSortType.PostSortTypePublished,
					SortDirectionType.SortDirectionTypeDescending);

			if (posts != null) {
				PagerHelper.moveForward(pager);

				for (Post post : posts) {
					entry = new SyndEntryImpl();
					entry.setTitle(post.title);
					entry.setLink(url + "/#" + PageType.PostDetailPageType
							.asTargetHistoryToken(post.slug));
					entry.setPublishedDate(post.published);
					description = new SyndContentImpl();
					description.setType("text/HTML");
					description.setValue(processor.process(post.summary));
					entry.setDescription(description);
					entries.add(entry);

				}
			}
		} while (posts != null && posts.size() >= pager.count.intValue());

		feed.setEntries(entries);

		return feed;
	}
}
