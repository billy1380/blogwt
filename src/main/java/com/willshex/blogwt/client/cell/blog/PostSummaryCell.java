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
import com.willshex.blogwt.client.helper.PostHelper;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.helper.DateTimeHelper;
import com.willshex.blogwt.shared.api.helper.UserHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostSummaryCell extends AbstractCell<Post> {

	public interface DateTemplate extends SafeHtmlTemplates {
		DateTemplate INSTANCE = GWT.create(DateTemplate.class);

		@Template("<span class=\"label label-info\">Created {0} - Not published</span>")
		SafeHtml notPublished (String formattedDate);

		@Template("<span class=\"text-muted\"><span class=\"glyphicon glyphicon-time\" aria-hidden=\"true\"></span> {0}</span>")
		SafeHtml publishedDate (String formattedDate);

		@Template("<span class=\"label label-warning\">Not visible</span>")
		SafeHtml notVisible ();
	}

	interface PostSummaryCellRenderer extends UiRenderer {
		void render (SafeHtmlBuilder sb, SafeUri link, SafeHtml title,
				SafeHtml summary, String author, SafeHtml published,
				SafeHtml visible);
	}

	private static PostSummaryCellRenderer RENDERER = GWT
			.create(PostSummaryCellRenderer.class);

	@Override
	public void render (Context context, Post value, SafeHtmlBuilder builder) {
		SafeUri link = PageType.PostDetailPageType.asHref(PostHelper
				.getSlug(value));
		SafeHtml published = DateTemplate.INSTANCE.notPublished(DateTimeHelper
				.ago(value.created));

		if (value.published != null) {
			published = DateTemplate.INSTANCE.publishedDate(DateTimeHelper
					.ago(value.published));
		}

		RENDERER.render(builder, link, SafeHtmlUtils
				.fromTrustedString(PostHelper.makeHeading2(value.title)),
				SafeHtmlUtils.fromTrustedString(PostHelper
						.makeMarkup(value.summary)), UserHelper
						.handle(value.author), published, value.listed
						.booleanValue() ? SafeHtmlUtils.EMPTY_SAFE_HTML
						: DateTemplate.INSTANCE.notVisible());
	}
}
