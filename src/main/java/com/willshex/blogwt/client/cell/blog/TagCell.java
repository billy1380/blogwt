//
//  TagCell.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 16 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.cell.blog;

import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.shared.api.datatype.Tag;
import com.willshex.blogwt.shared.helper.PostHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class TagCell extends AbstractSafeHtmlCell<Tag> {

	public interface TagTemplate extends SafeHtmlTemplates {
		TagTemplate INSTANCE = GWT.create(TagTemplate.class);

		@Template("<a href=\"{0}\" alt=\"tag {1}\">{1}</a>")
		SafeHtml tag (SafeUri href, String name);

		@Template("<a href=\"{0}\" alt=\"tag {1}\">{1} <span class=\"badge\">{2}</span></a>")
		SafeHtml tagWithCount (SafeUri href, String name, int count);
	}

	private static class TagCellRenderer extends AbstractSafeHtmlRenderer<Tag> {

		private boolean displayLabel;
		private boolean displayCount;

		public TagCellRenderer (boolean displayLabel, boolean displayCount) {
			this.displayLabel = displayLabel;
			this.displayCount = displayCount;
		}

		/* (non-Javadoc)
		 * 
		 * @see
		 * com.google.gwt.text.shared.SafeHtmlRenderer#render(java.lang.Object) */
		@Override
		public SafeHtml render (Tag object) {
			SafeHtmlBuilder builder = new SafeHtmlBuilder();

			if (displayLabel) {
				builder.appendHtmlConstant("<span class=\"glyphicon glyphicon-tags\"></span> ");
			}

			SafeUri href = PageType.TagPagePageType
					.asHref(object.slug == null ? PostHelper
							.slugify(object.name) : object.slug);

			if (displayCount) {
				builder.append(TagTemplate.INSTANCE.tagWithCount(href,
						object.name,
						object.posts == null ? 0 : object.posts.size()));
			} else {
				builder.append(TagTemplate.INSTANCE.tag(href, object.name));
			}

			return builder.toSafeHtml();
		}
	}

	public TagCell (boolean displayLabel, boolean displayCount) {
		super(new TagCellRenderer(displayLabel, displayCount));
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.cell.client.AbstractSafeHtmlCell#render(com.google.gwt
	 * .cell.client.Cell.Context, com.google.gwt.safehtml.shared.SafeHtml,
	 * com.google.gwt.safehtml.shared.SafeHtmlBuilder) */
	@Override
	protected void render (com.google.gwt.cell.client.Cell.Context context,
			SafeHtml data, SafeHtmlBuilder sb) {
		sb.append(data);
	}
}
