//  
//  ResourceService.java
//  blogwt
//
//  Created by William Shakour on May 4, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.server.service.resource;

import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;
import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.server.service.ISortable;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.api.datatype.ResourceSortType;

final class ResourceService
		implements IResourceService, ISortable<ResourceSortType> {

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.service.IService#getName() */
	@Override
	public String getName () {
		return NAME;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.resource.IResourceService#getResource
	 * (java.lang.Long) */
	@Override
	public Resource getResource (Long id) {
		return load().id(id.longValue()).now();
	}

	private LoadType<Resource> load () {
		return provide().load().type(Resource.class);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.resource.IResourceService#addResource
	 * (com.willshex.blogwt.shared.api.datatype.Resource) */
	@Override
	public Resource addResource (Resource resource) {
		if (resource.created == null) {
			resource.created = new Date();
		}

		Key<Resource> key = provide().save().entity(resource).now();
		resource.id = keyToId(key);

		return resource;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.resource.IResourceService#
	 * updateResource (com.willshex.blogwt.shared.api.datatype.Resource) */
	@Override
	public Resource updateResource (Resource resource) {
		provide().save().entity(resource).now();
		return resource;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.resource.IResourceService#
	 * deleteResource (com.willshex.blogwt.shared.api.datatype.Resource) */
	@Override
	public void deleteResource (Resource resource) {
		provide().delete().entity(resource).now();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.resource.IResourceService#getResrouces
	 * (java.lang.Integer, java.lang.Integer,
	 * com.willshex.blogwt.shared.api.datatype.ResourceSortType,
	 * com.willshex.blogwt.shared.api.SortDirectionType) */
	@Override
	public List<Resource> getResources (Integer start, Integer count,
			ResourceSortType sortBy, SortDirectionType sortDirection) {
		return PersistenceHelper.pagedAndSorted(load(), start, count, sortBy,
				this, sortDirection);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.resource.IResourceService#
	 * getIdsResourceBatch(java.util.Collection) */
	@Override
	public List<Resource> getIdResourceBatch (Collection<Long> ids) {
		return new ArrayList<Resource>(load().ids(ids).values());
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.ISortable#map(java.lang.Enum) */
	@Override
	public String map (ResourceSortType sortBy) {
		return sortBy == ResourceSortType.ResourceSortTypeId ? "__key__"
				: sortBy.toString();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.persistence.batch.Batcher.BatchGetter#
	 * get(java.util.Collection) */
	@Override
	public List<Resource> get (Collection<Long> ids) {
		return getIdResourceBatch(ids);
	}

}
