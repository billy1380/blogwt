//
//  TagController.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 15 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gwt.view.client.ListDataProvider;
import com.willshex.blogwt.shared.api.datatype.Tag;

/**
 * @author William Shakour (billy1380)
 *
 */
public class TagController extends ListDataProvider<Tag> {
	private static TagController one = null;

	public static TagController get () {
		if (one == null) {
			one = new TagController();
		}

		return one;
	}

	private Map<String, Tag> tagLookup = new HashMap<String, Tag>();

	public TagController () {
		String tagsJson = tags();

		if (tagsJson != null) {
			JsonArray jsonTagArray = (new JsonParser()).parse(tagsJson)
					.getAsJsonArray();
			if (getList() == null) {
				setList(new ArrayList<Tag>());
			} else {
				getList().clear();
			}

			Tag item = null;
			for (int i = 0; i < jsonTagArray.size(); i++) {
				if (jsonTagArray.get(i).isJsonObject()) {
					(item = new Tag()).fromJson(jsonTagArray.get(i)
							.getAsJsonObject());
					tagLookup.put(item.slug, item);
					getList().add(item);
				}
			}
		}
	}

	private static native String tags ()
	/*-{
		return $wnd['tags'];
	}-*/;

}
