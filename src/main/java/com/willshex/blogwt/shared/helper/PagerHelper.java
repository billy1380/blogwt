//
//  PagerHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 28 Apr 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.helper;

import java.util.List;

import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.SortDirectionType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PagerHelper {

	public static Integer DEFAULT_START = Integer.valueOf(0);
	public static Integer DEFAULT_COUNT = Integer.valueOf(10);
	public static String DEFAULT_SORT_BY = null;

	public static void updatePager (Pager pager, List<?> list) {
		updatePager(pager, list, null);
	}

	public static void updatePager (Pager pager, List<?> list, Integer total) {
		if (list != null) {
			pager.start = Integer.valueOf(pager.start.intValue()
					+ (list.size()));
		} else {
			// list is null so do nothing
		}

		if (total != null) {
			pager.totalCount = total;
		}
	}

	public static Pager createInfinitePager () {
		return new Pager().start(DEFAULT_START).count(
				Integer.valueOf(Integer.MAX_VALUE));
	}

	public static Pager createDefaultPager () {
		return new Pager().start(DEFAULT_START).count(DEFAULT_COUNT)
				.sortBy(DEFAULT_SORT_BY)
				.sortDirection(SortDirectionType.SortDirectionTypeDescending);
	}

	public static Pager moveForward (Pager pager) {
		if (pager != null) {
			if (pager.start == null) {
				pager.start = DEFAULT_START;
			}

			if (pager.count == null) {
				pager.count = DEFAULT_COUNT;
			}

			pager.start = Integer.valueOf(pager.start.intValue()
					+ pager.count.intValue());

			if (pager.totalCount != null
					&& pager.totalCount.intValue() < pager.start.intValue()) {
				pager.start = Integer.valueOf(pager.totalCount.intValue()
						- pager.count.intValue());
			}

			if (pager.start < 0) {
				pager.start = DEFAULT_START;
			}
		}

		return pager;
	}

	public static Pager moveBackward (Pager pager) {
		if (pager != null) {
			if (pager.start == null) {
				pager.start = DEFAULT_START;
			}

			if (pager.count == null) {
				pager.count = DEFAULT_COUNT;
			}

			if (pager.start.intValue() > pager.count.intValue()) {
				pager.start = Integer.valueOf(pager.start.intValue()
						- pager.count.intValue());
			} else {
				pager.start = DEFAULT_START;
			}
		}

		return pager;
	}
}
