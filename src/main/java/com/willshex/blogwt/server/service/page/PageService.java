//  
//  PageService.java
//  xsdwsdl2code
//
//  Created by William Shakour on June 22, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.page;

import static com.willshex.blogwt.server.service.PersistenceService.ofy;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.PageSortType;

final class PageService implements IPageService {
	public String getName () {
		return NAME;
	}

	@Override
	public Page getPage (Long id) {
		return ofy().load().type(Page.class).id(id).now();
	}

	@Override
	public Page addPage (Page page) {
		if (page.created == null) {
			page.created = new Date();
		}

		Key<Page> pageKey = ofy().save().entity(page).now();
		page.id = Long.valueOf(pageKey.getId());

		return page;
	}

	@Override
	public Page updatePage (Page page) {
		ofy().save().entity(page).now();
		return page;
	}

	@Override
	public void deletePage (Page page) {
		ofy().delete().entity(page).now();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.page.IPageService#getSlugPage(java
	 * .lang.String) */
	@Override
	public Page getSlugPage (String slug) {
		return ofy().load().type(Page.class)
				.filter(PageSortType.PageSortTypeSlug.toString(), slug).first()
				.now();
	}

}