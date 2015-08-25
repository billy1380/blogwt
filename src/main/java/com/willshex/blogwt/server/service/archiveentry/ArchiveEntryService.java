//  
//  ArchiveEntryService.java
//  xsdwsdl2code
//
//  Created by William Shakour on August 25, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.server.service.archiveentry;

import static com.willshex.blogwt.server.service.PersistenceService.ofy;

import java.util.ArrayList;
import java.util.Date;

import com.googlecode.objectify.Key;
import com.willshex.blogwt.shared.api.datatype.ArchiveEntry;
import com.willshex.blogwt.shared.api.datatype.Post;

final class ArchiveEntryService implements IArchiveEntryService {
	public String getName () {
		return NAME;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.archiveentry.IArchiveEntryService
	 * #getArchiveEntry(java.lang.Long) */
	@Override
	public ArchiveEntry getArchiveEntry (Long id) {
		return ofy().load().type(ArchiveEntry.class).id(id).now();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.archiveentry.IArchiveEntryService
	 * #addArchiveEntry(com.willshex.blogwt.shared.api.datatype.ArchiveEntry) */
	@Override
	public ArchiveEntry addArchiveEntry (ArchiveEntry archiveEntry) {
		if (archiveEntry.created == null) {
			archiveEntry.created = new Date();
		}

		archiveEntry.postKeys = new ArrayList<Key<Post>>();
		for (Post post : archiveEntry.posts) {
			archiveEntry.postKeys.add(Key.create(post));
		}

		Key<ArchiveEntry> pageKey = ofy().save().entity(archiveEntry).now();
		archiveEntry.id = Long.valueOf(pageKey.getId());

		return archiveEntry;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.archiveentry.IArchiveEntryService
	 * #updateArchiveEntry(com.willshex.blogwt.shared.api.datatype.ArchiveEntry) */
	@Override
	public ArchiveEntry updateArchiveEntry (ArchiveEntry archiveEntry) {
		archiveEntry.postKeys = new ArrayList<Key<Post>>();
		for (Post post : archiveEntry.posts) {
			archiveEntry.postKeys.add(Key.create(post));
		}

		ofy().save().entity(archiveEntry).now();
		return archiveEntry;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.archiveentry.IArchiveEntryService
	 * #deleteArchiveEntry(com.willshex.blogwt.shared.api.datatype.ArchiveEntry) */
	@Override
	public void deleteArchiveEntry (ArchiveEntry archiveEntry) {
		ofy().delete().entity(archiveEntry);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.archiveentry.IArchiveEntryService
	 * #archivePost(com.willshex.blogwt.shared.api.datatype.Post) */
	@Override
	public void archivePost (Post post) {

	}

}