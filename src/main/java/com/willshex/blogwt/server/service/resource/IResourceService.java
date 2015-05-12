//  
//  IResourceService.java
//  blogwt
//
//  Created by William Shakour on May 4, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.resource;

import com.spacehopperstudios.service.IService;
import com.willshex.blogwt.shared.api.datatype.Resource;

public interface IResourceService extends IService {

	public static final String NAME = "blogwt.resource";

	/**
	 * @param id
	 * @return
	 */
	public Resource getResource (Long id);

	/**
	 * @param resource
	 * @return
	 */
	public Resource addResource (Resource resource);

	/**
	 * @param resource
	 * @return
	 */
	public Resource updateResource (Resource resource);

	/**
	 * @param resource
	 */
	public void deleteResource (Resource resource);

}