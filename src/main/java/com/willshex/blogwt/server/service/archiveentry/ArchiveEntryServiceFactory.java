//  
//  ArchiveEntryServiceFactory.java
//  xsdwsdl2code
//
//  Created by William Shakour on August 25, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.archiveentry;

final class ArchiveEntryServiceFactory {

	/**
	* @return
	*/
	public static IArchiveEntryService createNewArchiveEntryService () {
		return new ArchiveEntryService();
	}

}