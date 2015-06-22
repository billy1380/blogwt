//  
//  IPageService.java
//  xsdwsdl2code
//
//  Created by William Shakour on June 22, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.page;

import com.spacehopperstudios.service.IService;
import com.willshex.blogwt.shared.api.datatype.Page;

public interface IPageService extends IService {
	public static final String NAME = "blogwt.page";

	/**
	* @param id
	* @return
	*/
	public Page getPage (Long id);

	/**
	* @param page
	* @return
	*/
	public Page addPage (Page page);

	/**
	* @param page
	* @return
	*/
	public Page updatePage (Page page);

	/**
	* @param page
	*/
	public void deletePage (Page page);

}