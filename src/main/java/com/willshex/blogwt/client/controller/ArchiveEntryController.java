//
//  ArchiveEntryController.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 25 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gwt.view.client.ListDataProvider;
import com.willshex.blogwt.shared.api.datatype.ArchiveEntry;

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

	private Map<Integer, List<ArchiveEntry>> years = new HashMap<Integer, List<ArchiveEntry>>();
	private Map<Integer, Integer> yearPostCount = new HashMap<Integer, Integer>();

	public ArchiveEntryController () {
		String archiveEntriesJson = archiveEntries();

		if (archiveEntriesJson != null) {
			JsonArray jsonArchiveEntryArray = (new JsonParser()).parse(
					archiveEntriesJson).getAsJsonArray();
			getList().clear();

			ArchiveEntry item = null;
			List<ArchiveEntry> yearArchiveEntries;
			for (int i = 0; i < jsonArchiveEntryArray.size(); i++) {
				if (jsonArchiveEntryArray.get(i).isJsonObject()) {
					(item = new ArchiveEntry()).fromJson(jsonArchiveEntryArray
							.get(i).getAsJsonObject());
					getList().add(item);

					// manage the groups and the count
					yearArchiveEntries = years.get(item.year);

					if (yearArchiveEntries == null) {
						yearArchiveEntries = new ArrayList<ArchiveEntry>();
						years.put(item.year, yearArchiveEntries);
						yearPostCount.put(item.year, Integer.valueOf(0));
					}

					yearArchiveEntries.add(item);
					yearPostCount.put(
							item.year,
							Integer.valueOf(yearPostCount.get(item.year)
									.intValue() + item.posts.size()));

				}
			}
		}
	}

	private static native String archiveEntries ()
	/*-{
		return $wnd['archiveEntries'];
	}-*/;

	/**
	 * @return
	 */
	public Collection<Integer> getYears () {
		return years.keySet();
	}

	/**
	 * @param year
	 * @return
	 */
	public int getYearPostCount (Integer year) {
		return yearPostCount.get(year).intValue();
	}

	/**
	 * @param value
	 * @return
	 */
	public List<ArchiveEntry> getYearArchiveEntries (Integer value) {
		return years.get((Integer) value);
	}
}
