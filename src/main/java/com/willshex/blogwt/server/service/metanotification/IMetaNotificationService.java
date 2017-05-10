// 
//  IMetaNotificationService.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.metanotification;

import com.willshex.blogwt.shared.api.datatype.MetaNotification;
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

}