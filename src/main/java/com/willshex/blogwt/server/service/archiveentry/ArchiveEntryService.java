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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.TimeZone;

import com.googlecode.objectify.Key;
import com.willshex.blogwt.server.service.PersistenceService;
import com.willshex.blogwt.shared.api.datatype.ArchiveEntry;
import com.willshex.blogwt.shared.api.datatype.Post;

final class ArchiveEntryService implements IArchiveEntryService {

	private ThreadLocal<Calendar> calendar = new ThreadLocal<Calendar>();

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

		Key<ArchiveEntry> archiveEntryKey = ofy().save().entity(archiveEntry)
				.now();
		archiveEntry.id = Long.valueOf(archiveEntryKey.getId());

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
		if (post.published != null) {
			Calendar c = ensureCalendar();
			c.setTime(post.published);
			Integer month = Integer.valueOf(c.get(java.util.Calendar.MONTH));
			Integer year = Integer.valueOf(c.get(java.util.Calendar.YEAR));
			ArchiveEntry archiveEntry = getMonthArchiveEntry(month, year);

			if (archiveEntry == null) {
				archiveEntry = new ArchiveEntry().month(month).year(year)
						.posts(Arrays.asList(post));
				addArchiveEntry(archiveEntry);
			} else {
				HashSet<Long> set = new HashSet<Long>();
				for (Key<Post> key : archiveEntry.postKeys) {
					set.add(Long.valueOf(key.getId()));
				}
				set.add(post.id);

				archiveEntry.posts = PersistenceService.dataTypeList(
						Post.class, set);

				updateArchiveEntry(archiveEntry);
			}
		}
	}

	@Override
	public ArchiveEntry getMonthArchiveEntry (Integer month, Integer year) {
		return ofy().load().type(ArchiveEntry.class).filter("year", year)
				.filter("month", month).first().now();
	}

	private Calendar ensureCalendar () {
		if (calendar.get() == null) {
			calendar.set(Calendar.getInstance());
			calendar.get().setTimeZone(TimeZone.getTimeZone("UTC"));
		}

		return calendar.get();
	}

}