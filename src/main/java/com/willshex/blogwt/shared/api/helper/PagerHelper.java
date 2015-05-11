//
//  PagerHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 28 Apr 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.helper;

import java.util.List;

import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.SortDirectionType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PagerHelper {

	public static Long DEFAULT_START = Long.valueOf(0);
	public static Long DEFAULT_COUNT = Long.valueOf(10);
	public static String DEFAULT_SORT_BY = null;

	public static void updatePager(Pager pager, List<?> list) {
		updatePager(pager, list, null);
	}

	public static void updatePager(Pager pager, List<?> list, Long total) {
		if (list != null) {
			pager.start = Long.valueOf(pager.start.longValue() + (list.size()));
		} else {
			// list is null so do nothing
		}

		if (total != null) {
			pager.totalCount = total;
		}
	}

	public static Pager createInfinitePager() {
		return new Pager().start(DEFAULT_START).count(Long.valueOf(Long.MAX_VALUE));
	}

	public static Pager createDefaultPager() {
		return new Pager().start(DEFAULT_START).count(DEFAULT_COUNT).sortBy(DEFAULT_SORT_BY).sortDirection(SortDirectionType.SortDirectionTypeDescending);
	}

	public static Pager moveForward(Pager pager) {
		if (pager != null) {
			if (pager.start == null) {
				pager.start = DEFAULT_START;
			}

			if (pager.count == null) {
				pager.count = DEFAULT_COUNT;
			}

			pager.start = Long.valueOf(pager.start.longValue() + pager.count.longValue());

			if (pager.totalCount != null && pager.totalCount.longValue() < pager.start.longValue()) {
				pager.start = Long.valueOf(pager.totalCount.longValue() - pager.count.longValue());
			}

			if (pager.start < 0) {
				pager.start = DEFAULT_START;
			}
		}

		return pager;
	}

	public static Pager moveBackward(Pager pager) {
		if (pager != null) {
			if (pager.start == null) {
				pager.start = DEFAULT_START;
			}

			if (pager.count == null) {
				pager.count = DEFAULT_COUNT;
			}

			if (pager.start.longValue() > pager.count.longValue()) {
				pager.start = Long.valueOf(pager.start.longValue() - pager.count.longValue());
			} else {
				pager.start = DEFAULT_START;
			}
		}

		return pager;
	}
}
