//  
//  IResourceService.java
//  blogwt
//
//  Created by William Shakour on May 4, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.resource;

import java.util.List;

import com.willshex.service.IService;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.api.datatype.ResourceSortType;

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

	/**
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public List<Resource> getResources (Integer start, Integer count,
			ResourceSortType sortBy, SortDirectionType sortDirection);

}