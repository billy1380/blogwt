//  
//  ResourceService.java
//  blogwt
//
//  Created by William Shakour on May 4, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.server.service.resource;

import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.api.datatype.ResourceSortType;

final class ResourceService implements IResourceService {

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
		return provide().load().type(Resource.class).id(id.longValue()).now();
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
		resource.id = Long.valueOf(key.getId());
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
		Query<Resource> query = provide().load().type(Resource.class);

		if (start != null) {
			query = query.offset(start.intValue());
		}

		if (count != null) {
			query = query.limit(count.intValue());
		}

		if (sortBy != null) {
			String condition = sortBy == ResourceSortType.ResourceSortTypeId
					? "__key__" : sortBy.toString();

			if (sortDirection != null) {
				switch (sortDirection) {
				case SortDirectionTypeDescending:
					condition = "-" + condition;
					break;
				default:
					break;
				}
			}

			query = query.order(condition);
		}

		return query.list();
	}

}