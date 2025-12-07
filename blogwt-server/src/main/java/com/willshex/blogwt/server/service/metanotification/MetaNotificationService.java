// 
//  MetaNotificationService.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.metanotification;

import static com.willshex.blogwt.server.helper.PersistenceHelper.id;
import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;
import com.willshex.blogwt.shared.api.datatype.MetaNotificationSortType;

final class MetaNotificationService implements IMetaNotificationService {
	public String getName () {
		return NAME;
	}

	@Override
	public MetaNotification getMetaNotification (Long id) {
		return id(load(), id);
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
	@Override
	public List<MetaNotification> getMetaNotifications (Integer start,
			Integer count, MetaNotificationSortType sortBy,
			SortDirectionType sortDirection) {
		return PersistenceHelper.pagedAndSorted(load(), start, count, sortBy,
				this, sortDirection);
	}
	@Override
	public List<MetaNotification> getIdMetaNotificationBatch (
			Iterable<Long> ids) {
		return new ArrayList<MetaNotification>(load().ids(ids).values());
	}
	@Override
	public MetaNotification getCodeMetaNotification (String code) {
		return PersistenceHelper.one(load().filter(
				map(MetaNotificationSortType.MetaNotificationSortTypeCode),
				code));
	}
	@Override
	public List<MetaNotification> get (Iterable<Long> ids) {
		return getIdMetaNotificationBatch(ids);
	}

}
