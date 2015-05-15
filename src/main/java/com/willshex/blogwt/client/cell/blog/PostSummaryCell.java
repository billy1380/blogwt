//
//  PostSummaryCell.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 15 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.cell.blog;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiRenderer;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.helper.DateTimeHelper;
import com.willshex.blogwt.shared.api.helper.PostHelper;
import com.willshex.blogwt.shared.api.helper.UserHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostSummaryCell extends AbstractCell<Post> {

	interface DateTemplate extends SafeHtmlTemplates {
		DateTemplate INSTANCE = GWT.create(DateTemplate.class);

		@Template("<span class=\"label label-info\">NOT PUBLISHED</span>")
		SafeHtml notPublished ();

		@Template("<span><span class=\"glyphicon glyphicon-time\" aria-hidden=\"true\"></span>{0}</span>")
		SafeHtml publishedDate (String formattedDate);

		@Template("<span class=\"label label-warning\">NOT VISIBLE</span>")
		SafeHtml notVisible ();
	}

	interface PostSummaryCellRenderer extends UiRenderer {
		void render (SafeHtmlBuilder sb, SafeUri link, String title,
				SafeHtml description, String author, SafeHtml published,
				SafeHtml visible);
	}

	private static PostSummaryCellRenderer RENDERER = GWT
			.create(PostSummaryCellRenderer.class);

	@Override
	public void render (Context context, Post value, SafeHtmlBuilder builder) {
		SafeUri link = PageType.PostDetailPageType.asHref(PostHelper
				.getSlug(value));
		SafeHtml published = DateTemplate.INSTANCE.notPublished();

		if (value.published != null) {
			published = DateTemplate.INSTANCE.publishedDate(DateTimeHelper
					.ago(value.published));
		}

		RENDERER.render(builder, link, value.title, SafeHtmlUtils
				.fromTrustedString(value.summary), UserHelper
				.handle(value.author), published,
				value.visible ? SafeHtmlUtils.EMPTY_SAFE_HTML
						: DateTemplate.INSTANCE.notVisible());
	}
}
