//  
//  PageServiceProvider.java
//  blogwt
//
//  Created by William Shakour on June 22, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.page;

import com.willshex.service.ServiceDiscovery;

final public class PageServiceProvider {

	/**
	* @return
	*/
	public static IPageService provide () {
		IPageService pageService = null;

		if ((pageService = (IPageService) ServiceDiscovery
				.getService(IPageService.NAME)) == null) {
			pageService = PageServiceFactory.createNewPageService();
			ServiceDiscovery.registerService(pageService);
		}

		return pageService;
	}

}