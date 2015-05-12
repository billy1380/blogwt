//  
//  ResourceService.java
//  blogwt
//
//  Created by William Shakour on May 4, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.server.service.resource;

import static com.willshex.blogwt.server.service.PersistenceService.ofy;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.willshex.blogwt.shared.api.datatype.Resource;

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
		return ofy().load().type(Resource.class).id(id.longValue()).now();
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

		Key<Resource> key = ofy().save().entity(resource).now();
		resource.id = Long.valueOf(key.getId());
		return resource;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.resource.IResourceService#updateResource
	 * (com.willshex.blogwt.shared.api.datatype.Resource) */
	@Override
	public Resource updateResource (Resource resource) {
		ofy().save().entity(resource).now();
		return resource;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.resource.IResourceService#deleteResource
	 * (com.willshex.blogwt.shared.api.datatype.Resource) */
	@Override
	public void deleteResource (Resource resource) {
		ofy().delete().entity(resource).now();
	}

}