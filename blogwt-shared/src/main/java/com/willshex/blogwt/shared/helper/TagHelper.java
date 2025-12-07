package com.willshex.blogwt.shared.helper;

import java.util.ArrayList;
import java.util.List;

public class TagHelper {

	/**
	 * Converts a list of comma delimited tags into a tag list. Tags are all made lowercase.
	 * 
	 * @param tags
	 * @return
	 */
	public static List<String> convertToTagList (String tags) {
		return convertToTagList(tags, false);
	}

	/**
	 * Converts a list of comma delimited tags into a tag list
	 * 
	 * @param tags
	 * @param perserveCase
	 * @return
	 */
	public static List<String> convertToTagList (String tags,
			boolean perserveCase) {
		List<String> tagList = null;

		if (tags != null && tags.length() >= 0) {
			String[] splitTags = tags.split(",");

			for (String item : splitTags) {
				String tag = item.trim();

				if (!perserveCase) {
					tag = tag.toLowerCase();
				}

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
