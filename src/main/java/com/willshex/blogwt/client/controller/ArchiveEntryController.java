//
//  ArchiveEntryController.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 25 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.TreeViewModel;
import com.willshex.blogwt.client.cell.blog.PostSummaryCell;
import com.willshex.blogwt.shared.api.datatype.ArchiveEntry;
import com.willshex.blogwt.shared.api.datatype.Post;

/**
 * @author billy1380
 *
 */
public class ArchiveEntryController extends ListDataProvider<ArchiveEntry> {
	private static ArchiveEntryController one = null;

	public static ArchiveEntryController get () {
		if (one == null) {
			one = new ArchiveEntryController();
		}

		return one;
	}

	public interface TextBadgeTemplate extends SafeHtmlTemplates {
		TextBadgeTemplate INSTANCE = GWT.create(TextBadgeTemplate.class);

		@Template("{0} <span class=\"badge\">{1}</span>")
		SafeHtml textWithBadge (String name, int count);
	}

	public static final class ArchiveModelItem {
		private Integer year;
		private Integer month;
		private Integer count;
		private Post post;

		/**
		 * @param post
		 */
		public ArchiveModelItem (Post post) {
			this.post = post;
		}

		/**
		 * @param year
		 * @param count
		 */
		public ArchiveModelItem (Integer value, Integer count, boolean isYear) {
			if (isYear) {
				year = value;
			} else {
				month = value;
			}

			this.count = count;
		}

		public boolean isYear () {
			return year != null;
		}

		public boolean isMonth () {
			return month != null;
		}

		public boolean isPost () {
			return post != null;
		}

		/**
		 * @return the year
		 */
		public Integer getYear () {
			return year;
		}

		/**
		 * @return the month
		 */
		public Integer getMonth () {
			return month;
		}

		/**
		 * @return the count
		 */
		public Integer getCount () {
			return count;
		}

		/**
		 * @return the post
		 */
		public Post getPost () {
			return post;
		}

	}

	public static final class ArchiveModel implements TreeViewModel {

		private Map<Integer, List<ArchiveEntry>> years = new HashMap<Integer, List<ArchiveEntry>>();
		private Map<Integer, Integer> yearPostCount = new HashMap<Integer, Integer>();
		private static final String[] months = LocaleInfo.getCurrentLocale()
				.getDateTimeFormatInfo().monthsFull();

		private static final PostSummaryCell POST_CELL = new PostSummaryCell();

		/* (non-Javadoc)
		 * 
		 * @see
		 * com.google.gwt.view.client.TreeViewModel#getNodeInfo(java.lang.Object
		 * ) */
		@Override
		public <T> NodeInfo<?> getNodeInfo (final T value) {
			NodeInfo<?> info = null;

			if (value == null) {
				List<ArchiveEntry> yearArchiveEntries = null;
				for (ArchiveEntry archiveEntry : ArchiveEntryController.get()
						.getList()) {
					yearArchiveEntries = years.get(archiveEntry.year);

					if (yearArchiveEntries == null) {
						yearArchiveEntries = new ArrayList<ArchiveEntry>();
						years.put(archiveEntry.year, yearArchiveEntries);
						yearPostCount
								.put(archiveEntry.year, Integer.valueOf(0));
					}

					yearArchiveEntries.add(archiveEntry);
					yearPostCount.put(archiveEntry.year, Integer
							.valueOf(yearPostCount.get(archiveEntry.year)
									.intValue() + archiveEntry.posts.size()));
				}

				info = new DefaultNodeInfo<Integer>(
						new ListDataProvider<Integer>(new ArrayList<Integer>(
								years.keySet())),
						new AbstractSafeHtmlCell<Integer>(
								new AbstractSafeHtmlRenderer<Integer>() {

									@Override
									public SafeHtml render (Integer value) {
										SafeHtmlBuilder builder = new SafeHtmlBuilder();
										builder.append(TextBadgeTemplate.INSTANCE
												.textWithBadge(
														value.toString(),
														yearPostCount
																.get(value)
																.intValue()));
										return builder.toSafeHtml();
									}
								}) {

							@Override
							protected void render (
									com.google.gwt.cell.client.Cell.Context context,
									SafeHtml data, SafeHtmlBuilder sb) {
								sb.append(data);
							}
						});
			} else if (value instanceof Integer) {
				info = new DefaultNodeInfo<ArchiveEntry>(
						new ListDataProvider<ArchiveEntry>(
								years.get((Integer) value)),
						new AbstractSafeHtmlCell<ArchiveEntry>(
								new AbstractSafeHtmlRenderer<ArchiveEntry>() {

									@Override
									public SafeHtml render (ArchiveEntry value) {
										SafeHtmlBuilder builder = new SafeHtmlBuilder();
										builder.append(TextBadgeTemplate.INSTANCE
												.textWithBadge(
														months[value.month
																.intValue()],
														value.posts.size()));
										return builder.toSafeHtml();
									}
								}) {

							@Override
							protected void render (
									com.google.gwt.cell.client.Cell.Context context,
									SafeHtml data, SafeHtmlBuilder sb) {
								sb.append(data);
							}
						});
			} else if (value instanceof ArchiveEntry) {
				info = new DefaultNodeInfo<Post>(
						PostController.archived((ArchiveEntry) value),
						new AbstractCell<Post>() {
							@Override
							public void render (
									com.google.gwt.cell.client.Cell.Context context,
									Post value, SafeHtmlBuilder sb) {
								POST_CELL.render(context, value, sb);
							}
						});
			}

			return info;
		}

		/* (non-Javadoc)
		 * 
		 * @see
		 * com.google.gwt.view.client.TreeViewModel#isLeaf(java.lang.Object) */
		@Override
		public boolean isLeaf (Object value) {
			return value instanceof Post;
		}

	}

	private Map<String, ArchiveEntry> tagLookup = new HashMap<String, ArchiveEntry>();

	public ArchiveEntryController () {
		String archiveEntriesJson = archiveEntries();

		if (archiveEntriesJson != null) {
			JsonArray jsonArchiveEntryArray = (new JsonParser()).parse(
					archiveEntriesJson).getAsJsonArray();
			getList().clear();

			ArchiveEntry item = null;
			for (int i = 0; i < jsonArchiveEntryArray.size(); i++) {
				if (jsonArchiveEntryArray.get(i).isJsonObject()) {
					(item = new ArchiveEntry()).fromJson(jsonArchiveEntryArray
							.get(i).getAsJsonObject());
					tagLookup.put(item.year + "/" + item.month, item);
					getList().add(item);
				}
			}
		}
	}

	private static native String archiveEntries ()
	/*-{
		return $wnd['archiveEntries'];
	}-*/;
}
