//
//TagServiceProvider.java
//xsdwsdl2code
//
//Created by William Shakour on July 15, 2015.
//Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.tag;

import com.spacehopperstudios.service.ServiceDiscovery;

final public class TagServiceProvider {

	/**
	* @return
	*/
	public static ITagService provide () {
		ITagService tagService = null;

		if ((tagService = (ITagService) ServiceDiscovery
				.getService(ITagService.NAME)) == null) {
			tagService = TagServiceFactory.createNewTagService();
			ServiceDiscovery.registerService(tagService);
		}

		return tagService;
	}

}