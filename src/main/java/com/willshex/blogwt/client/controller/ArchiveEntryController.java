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
import com.willshex.blogwt.shared.api.datatype.ArchiveEntry;

/**
 * @author billy1380
 *
 */
public class ArchiveEntryController {
	private static ArchiveEntryController one = null;

	public static ArchiveEntryController get () {
		if (one == null) {
			one = new ArchiveEntryController();
		}

		return one;
	}

	private Map<String, ArchiveEntry> tagLookup = new HashMap<String, ArchiveEntry>();
	private List<ArchiveEntry> archiveEntries;

	public ArchiveEntryController () {
		String archiveEntriesJson = archiveEntries();

		if (archiveEntriesJson != null) {
			JsonArray jsonArchiveEntryArray = (new JsonParser()).parse(
					archiveEntriesJson).getAsJsonArray();
			if (archiveEntries == null) {
				archiveEntries = new ArrayList<ArchiveEntry>();
			} else {
				archiveEntries.clear();
			}

			ArchiveEntry item = null;
			for (int i = 0; i < jsonArchiveEntryArray.size(); i++) {
				if (jsonArchiveEntryArray.get(i).isJsonObject()) {
					(item = new ArchiveEntry()).fromJson(jsonArchiveEntryArray
							.get(i).getAsJsonObject());
					tagLookup.put(item.year + "/" + item.month, item);
					archiveEntries.add(item);
				}
			}
		}
	}

	private static native String archiveEntries ()
	/*-{
		return $wnd['archiveEntries'];
	}-*/;
}
