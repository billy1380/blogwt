package com.willshex.blogwt.shared.api.helper;

import java.util.ArrayList;
import java.util.List;

public class TagHelper {
	/**
	 * Converts a list of comma delimited tags into a tag list
	 * 
	 * @param tags
	 * @return
	 */
	public static List<String> convertToTagList (String tags) {
		List<String> tagList = null;

		if (tags != null && tags.length() >= 0) {
			String[] splitTags = tags.split(",");

			for (String item : splitTags) {
				String tag = item.trim().toLowerCase();

				if (tag.length() > 0) {
					if (tagList == null) {
						tagList = new ArrayList<String>();
					}

					if (!tagList.contains(tag)) {
						tagList.add(tag);
					}
				}
			}
		}

		return tagList;
	}
}
