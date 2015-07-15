//
//TagServiceFactory.java
//xsdwsdl2code
//
//Created by William Shakour on July 15, 2015.
//Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.tag;

final class TagServiceFactory {

	/**
	* @return
	*/
	public static ITagService createNewTagService () {
		return new TagService();
	}

}