// 
//  GeneratedDownloadServiceFactory.java
//  blogwt
// 
//  Created by William Shakour on June 26, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.generateddownload;

final class GeneratedDownloadServiceFactory {

	/**
	* @return
	*/
	public static IGeneratedDownloadService createNewGeneratedDownloadService () {
		return new GeneratedDownloadService();
	}

}