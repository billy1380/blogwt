//  
//  ArchiveEntryServiceProvider.java
//  blogwt
//
//  Created by William Shakour on August 25, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.server.service.archiveentry;

import com.willshex.service.ServiceDiscovery;

final public class ArchiveEntryServiceProvider {

	/**
	* @return
	*/
	public static IArchiveEntryService provide () {
		IArchiveEntryService archiveEntryService = null;

		if ((archiveEntryService = (IArchiveEntryService) ServiceDiscovery
				.getService(IArchiveEntryService.NAME)) == null) {
			archiveEntryService = ArchiveEntryServiceFactory
					.createNewArchiveEntryService();
			ServiceDiscovery.registerService(archiveEntryService);
		}

		return archiveEntryService;
	}

}