// 
//  MetaNotificationService.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.metanotification;

import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.server.service.ISortable;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;
import com.willshex.blogwt.shared.api.datatype.MetaNotificationSortType;

final class MetaNotificationService implements IMetaNotificationService,
		ISortable<MetaNotificationSortType> {
	public String getName () {
		return NAME;
	}

	@Override
	public MetaNotification getMetaNotification (Long id) {
		return load().id(id.longValue()).now();
	}

	private LoadType<MetaNotification> load () {
		return provide().load().type(MetaNotification.class);
	}

	@Override
	public MetaNotification addMetaNotification (
			MetaNotification metaNotification) {
		if (metaNotification.created == null) {
			metaNotification.created = new Date();
		}

		Key<MetaNotification> key = provide().save().entity(metaNotification)
				.now();
		metaNotification.id = Long.valueOf(key.getId());

		return metaNotification;
	}

	@Override
	public MetaNotification updateMetaNotification (
			MetaNotification metaNotification) {
		provide().save().entity(metaNotification).now();
		return metaNotification;
	}

	@Override
	public void deleteMetaNotification (MetaNotification metaNotification) {
		provide().delete().entity(metaNotification).now();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.metanotification.
	 * IMetaNotificationService#getMetaNotifictions(java.lang.Integer,
	 * java.lang.Integer,
	 * com.willshex.blogwt.shared.api.datatype.MetaNotificationSortType,
	 * com.willshex.blogwt.shared.api.SortDirectionType) */
	@Override
	public List<MetaNotification> getMetaNotifications (Integer start,
			Integer count, MetaNotificationSortType sortBy,
			SortDirectionType sortDirection) {
		return PersistenceHelper.pagedAndSorted(load(), start, count, sortBy,
				this, sortDirection);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.ISortable#map(java.lang.Enum) */
	@Override
	public String map (MetaNotificationSortType sortBy) {
		return sortBy.toString();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.metanotification.
	 * IMetaNotificationService#getIdMetaNotificationBatch(java.util.
	 * Collection) */
	@Override
	public List<MetaNotification> getIdMetaNotificationBatch (
			Collection<Long> ids) {
		return new ArrayList<MetaNotification>(load().ids(ids).values());
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.metanotification.
	 * IMetaNotificationService#getCodeMetaNotification(java.lang.String) */
	@Override
	public MetaNotification getCodeMetaNotification (String code) {
		return PersistenceHelper.one(load().filter(
				map(MetaNotificationSortType.MetaNotificationSortTypeCode),
				code));
	}

}