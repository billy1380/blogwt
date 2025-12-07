//
//  PostsPlugin.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 4 Sep 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.markdown.plugin;

import java.util.List;
import java.util.Map;

import org.markdown4j.client.AbstractAsyncPlugin;
import org.markdown4j.client.event.PluginContentReadyEventHandler.PluginContentReadyEvent;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.willshex.blogwt.client.cell.blog.PostSummaryCell;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.shared.api.blog.call.GetPostsRequest;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.PostSortType;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostsPlugin extends AbstractAsyncPlugin {

	private static final PostSummaryCell RENDERER = new PostSummaryCell();

	public PostsPlugin (HandlerManager manager) {
		super("posts", manager);
	}
	@Override
	public void emit (StringBuilder out, List<String> lines,
			Map<String, String> params) {
		String count = params.get("count");
		final String id = HTMLPanel.createUniqueId();

		out.append("<div id=\"");
		out.append(id);
		out.append("\">Loading...</div>");

		ApiHelper.createBlogClient().getPosts(SessionController.get()
				.setSession(ApiHelper.setAccessCode(new GetPostsRequest()))
				.pager(PagerHelper.createDefaultPager()
						.count(Integer.valueOf(count))
						.sortBy(PostSortType.PostSortTypeCreated.toString())),
				(i, o) -> {
					if (manager != null) {
						String content = "";

						if (o.status == StatusType.StatusTypeSuccess
								&& o.posts != null && !o.posts.isEmpty()) {
							SafeHtmlBuilder b = new SafeHtmlBuilder();
							for (Post object : o.posts) {
								RENDERER.render(null, object, b);
							}

							content = b.toSafeHtml().asString();
						}

						manager.fireEvent(new PluginContentReadyEvent(this,
								lines, params, id, content));
					}

				}, (i, c) -> {
					if (manager != null) {
						String content = "";
						manager.fireEvent(new PluginContentReadyEvent(this,
								lines, params, id, content));
					}
				});

	}

}
