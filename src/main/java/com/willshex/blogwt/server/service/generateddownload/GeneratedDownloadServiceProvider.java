// 
//  GeneratedDownloadServiceProvider.java
//  blogwt
// 
//  Created by William Shakour on June 26, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.generateddownload;

import com.willshex.service.ServiceDiscovery;

final public class GeneratedDownloadServiceProvider {

	/**
	* @return
	*/
	public static IGeneratedDownloadService provide () {
		IGeneratedDownloadService generatedDownloadService = null;

		if ((generatedDownloadService = (IGeneratedDownloadService) ServiceDiscovery
				.getService(IGeneratedDownloadService.NAME)) == null) {
			generatedDownloadService = GeneratedDownloadServiceFactory
					.createNewGeneratedDownloadService();
			ServiceDiscovery.registerService(generatedDownloadService);
		}

		return generatedDownloadService;
	}

}