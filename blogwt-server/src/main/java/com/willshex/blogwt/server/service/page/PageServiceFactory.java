//  
//  PageServiceFactory.java
//  blogwt
//
//  Created by William Shakour on June 22, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.server.service.page;

final class PageServiceFactory {

	/**
	* @return
	*/
	public static IPageService createNewPageService () {
		return new PageService();
	}

}