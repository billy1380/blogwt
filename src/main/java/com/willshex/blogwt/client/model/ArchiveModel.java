//
//  ArchiveModel.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 28 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.TreeViewModel;
import com.willshex.blogwt.client.cell.blog.PostSummaryCell;
import com.willshex.blogwt.client.controller.ArchiveEntryController;
import com.willshex.blogwt.client.controller.PostController;
import com.willshex.blogwt.shared.api.datatype.ArchiveEntry;
import com.willshex.blogwt.shared.api.datatype.Post;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ArchiveModel implements TreeViewModel {

	public interface ArchiveModelTemplate extends SafeHtmlTemplates {
		ArchiveModelTemplate INSTANCE = GWT.create(ArchiveModelTemplate.class);

		@Template("{0} <span class=\"badge\">{1}</span>")
		SafeHtml textWithBadge (String text, int badge);
	}

	private static final class TextWithBadgeCell<T> extends
			AbstractSafeHtmlCell<T> {
		/**
		 * @param renderer
		 */
		public TextWithBadgeCell (SafeHtmlRenderer<T> renderer) {
			super(renderer);
		}

		/* (non-Javadoc)
		 * 
		 * @see
		 * com.google.gwt.cell.client.AbstractSafeHtmlCell#render(com.google
		 * .gwt.cell.client.Cell.Context,
		 * com.google.gwt.safehtml.shared.SafeHtml,
		 * com.google.gwt.safehtml.shared.SafeHtmlBuilder) */
		@Override
		protected void render (com.google.gwt.cell.client.Cell.Context context,
				SafeHtml data, SafeHtmlBuilder sb) {
			sb.append(data);
		}

	}

	private static final String[] months = LocaleInfo.getCurrentLocale()
			.getDateTimeFormatInfo().monthsFull();

	private static final PostSummaryCell POST_CELL = new PostSummaryCell();

	private static final Comparator<Integer> DESCENDING_COMPARATOR = Collections
			.<Integer> reverseOrder();
	private static final Comparator<ArchiveEntry> MONTH_COMPARATOR = new Comparator<ArchiveEntry>() {

		@Override
		public int compare (ArchiveEntry o1, ArchiveEntry o2) {
			return o1.month.compareTo(o2.month);
		}
	};

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.view.client.TreeViewModel#getNodeInfo(java.lang.Object ) */
	@Override
	public <T> NodeInfo<?> getNodeInfo (final T value) {
		NodeInfo<?> info = null;

		if (value == null) {
			List<Integer> sortedYears = new ArrayList<Integer>(
					ArchiveEntryController.get().getYears());
			Collections.sort(sortedYears, DESCENDING_COMPARATOR);

			info = new DefaultNodeInfo<Integer>(new ListDataProvider<Integer>(
					sortedYears), new TextWithBadgeCell<Integer>(
					new AbstractSafeHtmlRenderer<Integer>() {
						@Override
						public SafeHtml render (Integer value) {
							return ArchiveModelTemplate.INSTANCE.textWithBadge(
									value.toString(), ArchiveEntryController
											.get().getYearPostCount(value));
						}
					}));
		} else if (value instanceof Integer) {
			List<ArchiveEntry> sortedArchiveEntries = ArchiveEntryController
					.get().getYearArchiveEntries((Integer) value);
			Collections.sort(sortedArchiveEntries, MONTH_COMPARATOR);

			info = new DefaultNodeInfo<ArchiveEntry>(
					new ListDataProvider<ArchiveEntry>(sortedArchiveEntries),
					new TextWithBadgeCell<ArchiveEntry>(
							new AbstractSafeHtmlRenderer<ArchiveEntry>() {
								@Override
								public SafeHtml render (ArchiveEntry value) {
									return ArchiveModelTemplate.INSTANCE
											.textWithBadge(months[value.month
													.intValue()], value.posts
													.size());
								}
							}));
		} else if (value instanceof ArchiveEntry) {
			info = new DefaultNodeInfo<Post>(
					PostController.archived((ArchiveEntry) value), POST_CELL);
		}

		return info;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.view.client.TreeViewModel#isLeaf(java.lang.Object) */
	@Override
	public boolean isLeaf (Object value) {
		return value instanceof Post;
	}

}