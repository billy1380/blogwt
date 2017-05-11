// 
//  IMetaNotificationService.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.metanotification;

import java.util.Collection;
import java.util.List;

import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;
import com.willshex.blogwt.shared.api.datatype.MetaNotificationSortType;
import com.willshex.service.IService;

public interface IMetaNotificationService extends IService {

	public static final String NAME = "blogwt.metanotification";

	/**
	* @param id
	* @return
	*/
	public MetaNotification getMetaNotification (Long id);

	/**
	* @param metaNotification
	* @return
	*/
	public MetaNotification addMetaNotification (
			MetaNotification metaNotification);

	/**
	* @param metaNotification
	* @return
	*/
	public MetaNotification updateMetaNotification (
			MetaNotification metaNotification);

	/**
	* @param metaNotification
	*/
	public void deleteMetaNotification (MetaNotification metaNotification);

	/**
	 * Get meta notifications
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public List<MetaNotification> getMetaNotifications (Integer start,
			Integer count, MetaNotificationSortType sortBy,
			SortDirectionType sortDirection);

	/**
	 * Get id meta notification batch
	 * @param ids
	 * @return
	 */
	public List<MetaNotification> getIdMetaNotificationBatch (
			Collection<Long> ids);

}