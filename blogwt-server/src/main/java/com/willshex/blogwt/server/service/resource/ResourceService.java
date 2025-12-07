//  
//  ResourceService.java
//  blogwt
//
//  Created by William Shakour on May 4, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.server.service.resource;

import static com.willshex.blogwt.server.helper.PersistenceHelper.id;
import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;
import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.api.datatype.ResourceSortType;

final class ResourceService implements IResourceService {
	@Override
	public String getName () {
		return NAME;
	}
	@Override
	public Resource getResource (Long id) {
		return id(load(), id);
	}

	private LoadType<Resource> load () {
		return provide().load().type(Resource.class);
	}
	@Override
	public Resource addResource (Resource resource) {
		if (resource.created == null) {
			resource.created = new Date();
		}

		Key<Resource> key = provide().save().entity(resource).now();
		resource.id = keyToId(key);

		return resource;
	}
	@Override
	public Resource updateResource (Resource resource) {
		provide().save().entity(resource).now();
		return resource;
	}
	@Override
	public void deleteResource (Resource resource) {
		provide().delete().entity(resource).now();
	}
	@Override
	public List<Resource> getResources (Integer start, Integer count,
			ResourceSortType sortBy, SortDirectionType sortDirection) {
		return PersistenceHelper.pagedAndSorted(load(), start, count, sortBy,
				this, sortDirection);
	}
	@Override
	public List<Resource> getIdResourceBatch (Iterable<Long> ids) {
		return new ArrayList<Resource>(load().ids(ids).values());
	}
	@Override
	public List<Resource> get (Iterable<Long> ids) {
		return getIdResourceBatch(ids);
	}

}
