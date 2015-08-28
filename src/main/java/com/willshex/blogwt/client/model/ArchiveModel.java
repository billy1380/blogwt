//
//  ArchiveModel.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 28 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		SafeHtml textWithBadge (String name, int count);
	}

	private static final String[] months = LocaleInfo.getCurrentLocale()
			.getDateTimeFormatInfo().monthsFull();

	private Map<Integer, List<ArchiveEntry>> years = new HashMap<Integer, List<ArchiveEntry>>();
	private Map<Integer, Integer> yearPostCount = new HashMap<Integer, Integer>();

	private static final PostSummaryCell POST_CELL = new PostSummaryCell();

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.view.client.TreeViewModel#getNodeInfo(java.lang.Object ) */
	@Override
	public <T> NodeInfo<?> getNodeInfo (final T value) {
		NodeInfo<?> info = null;

		if (value == null) {
			createYearCountLookup();

			info = new DefaultNodeInfo<Integer>(new ListDataProvider<Integer>(
					new ArrayList<Integer>(years.keySet())),
					new AbstractSafeHtmlCell<Integer>(
							new AbstractSafeHtmlRenderer<Integer>() {

								@Override
								public SafeHtml render (Integer value) {
									return ArchiveModelTemplate.INSTANCE
											.textWithBadge(value.toString(),
													yearPostCount.get(value)
															.intValue());
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
					(new AbstractSafeHtmlCell<ArchiveEntry>(
							new AbstractSafeHtmlRenderer<ArchiveEntry>() {

								@Override
								public SafeHtml render (ArchiveEntry value) {
									return ArchiveModelTemplate.INSTANCE
											.textWithBadge(months[value.month
													.intValue()], value.posts
													.size());
								}
							}) {

						@Override
						protected void render (
								com.google.gwt.cell.client.Cell.Context context,
								SafeHtml data, SafeHtmlBuilder sb) {
							sb.append(data);
						}
					}));
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

	/**
	 * 
	 */
	private void createYearCountLookup () {
		List<ArchiveEntry> yearArchiveEntries = null;
		for (ArchiveEntry archiveEntry : ArchiveEntryController.get().getList()) {
			yearArchiveEntries = years.get(archiveEntry.year);

			if (yearArchiveEntries == null) {
				yearArchiveEntries = new ArrayList<ArchiveEntry>();
				years.put(archiveEntry.year, yearArchiveEntries);
				yearPostCount.put(archiveEntry.year, Integer.valueOf(0));
			}

			yearArchiveEntries.add(archiveEntry);
			yearPostCount.put(
					archiveEntry.year,
					Integer.valueOf(yearPostCount.get(archiveEntry.year)
							.intValue() + archiveEntry.posts.size()));
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.view.client.TreeViewModel#isLeaf(java.lang.Object) */
	@Override
	public boolean isLeaf (Object value) {
		return value instanceof Post;
	}

}