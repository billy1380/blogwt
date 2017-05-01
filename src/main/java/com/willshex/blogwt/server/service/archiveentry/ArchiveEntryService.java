//  
//  ArchiveEntryService.java
//  blogwt
//
//  Created by William Shakour on August 25, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.server.service.archiveentry;

import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;
import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.LoadType;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.ArchiveEntry;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.PostSortType;
import com.willshex.blogwt.shared.helper.PagerHelper;

final class ArchiveEntryService implements IArchiveEntryService {

	private ThreadLocal<Calendar> calendar = new ThreadLocal<Calendar>();

	public String getName () {
		return NAME;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.archiveentry.IArchiveEntryService
	 * #getArchiveEntry(java.lang.Long) */
	@Override
	public ArchiveEntry getArchiveEntry (Long id) {
		return load().id(id).now();
	}

	/**
	 * @return
	 */
	private LoadType<ArchiveEntry> load () {
		return provide().load().type(ArchiveEntry.class);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.archiveentry.IArchiveEntryService
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

		Key<ArchiveEntry> key = provide().save().entity(archiveEntry).now();
		archiveEntry.id = keyToId(key);

		return archiveEntry;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.archiveentry.IArchiveEntryService
	 * #updateArchiveEntry(com.willshex.blogwt.shared.api.datatype.
	 * ArchiveEntry) */
	@Override
	public ArchiveEntry updateArchiveEntry (ArchiveEntry archiveEntry) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.archiveentry.IArchiveEntryService
	 * #deleteArchiveEntry(com.willshex.blogwt.shared.api.datatype.
	 * ArchiveEntry) */
	@Override
	public void deleteArchiveEntry (ArchiveEntry archiveEntry) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.archiveentry.IArchiveEntryService
	 * #archivePost(com.willshex.blogwt.shared.api.datatype.Post) */
	@Override
	public void archivePost (Post post) {
		if (Boolean.TRUE.equals(post.listed) && post.published != null) {
			Calendar c = ensureCalendar();
			c.setTime(post.published);
			Integer month = Integer.valueOf(c.get(java.util.Calendar.MONTH));
			Integer year = Integer.valueOf(c.get(java.util.Calendar.YEAR));
			ArchiveEntry archiveEntry = getMonthArchiveEntry(month, year);

			if (archiveEntry == null) {
				archiveEntry = new ArchiveEntry().month(month).year(year)
						.posts(Arrays.asList(post));

				archiveEntry = addArchiveEntry(archiveEntry);
			} else {
				updateArchiveEntryPost(archiveEntry, post);
			}
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.archiveentry.IArchiveEntryService
	 * #getMonthArchiveEntry(java.lang.Integer, java.lang.Integer) */
	@Override
	public ArchiveEntry getMonthArchiveEntry (Integer month, Integer year) {
		return PersistenceHelper
				.one(load().filter("year", year).filter("month", month));
	}

	private Calendar ensureCalendar () {
		if (calendar.get() == null) {
			calendar.set(Calendar.getInstance());
			calendar.get().setTimeZone(TimeZone.getTimeZone("UTC"));
		}

		return calendar.get();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.archiveentry.IArchiveEntryService
	 * #getArchiveEntries() */
	@Override
	public List<ArchiveEntry> getArchiveEntries () {
		return load().list();
	}

	@Override
	public void generateArchive () {
		Map<String, ArchiveEntry> archiveEntryLookup = new HashMap<String, ArchiveEntry>();
		List<Post> posts;
		ArchiveEntry archiveEntry;

		Pager pager = PagerHelper.createDefaultPager();

		do {
			posts = PostServiceProvider.provide().getPosts(Boolean.FALSE,
					Boolean.FALSE, pager.start, pager.count,
					PostSortType.PostSortTypePublished,
					SortDirectionType.SortDirectionTypeDescending);

			if (posts != null) {
				PagerHelper.moveForward(pager);

				for (Post post : posts) {
					if (Boolean.TRUE.equals(post.listed)
							&& post.published != null) {
						Calendar c = ensureCalendar();
						c.setTime(post.published);
						Integer month = Integer
								.valueOf(c.get(java.util.Calendar.MONTH));
						Integer year = Integer
								.valueOf(c.get(java.util.Calendar.YEAR));

						String key = year + "/" + month;
						if ((archiveEntry = archiveEntryLookup
								.get(key)) == null) {
							archiveEntry = new ArchiveEntry().month(month)
									.year(year).posts(new ArrayList<Post>());
							archiveEntryLookup.put(key, archiveEntry);
						}

						archiveEntry.posts.add(post);
					}
				}
			}
		} while (posts != null && posts.size() >= pager.count.intValue());

		if (archiveEntryLookup.size() > 0) {
			addArchiveEntryBatch(archiveEntryLookup.values());
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.archiveentry.IArchiveEntryService
	 * #addArchiveEntryBatch(java.util.Collection) */
	@Override
	public void addArchiveEntryBatch (Collection<ArchiveEntry> archiveEntries) {
		for (ArchiveEntry archiveEntry : archiveEntries) {
			if (archiveEntry.created == null) {
				archiveEntry.created = new Date();
			}

			if (archiveEntry.posts != null) {
				for (Post post : archiveEntry.posts) {
					if (archiveEntry.postKeys == null) {
						archiveEntry.postKeys = new ArrayList<Key<Post>>();
					}

					archiveEntry.postKeys.add(Key.create(post));
				}
			}
		}

		provide().save().entities(archiveEntries).now();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.archiveentry.IArchiveEntryService
	 * #getDateArchiveEntry(java.util.Date) */
	@Override
	public ArchiveEntry getDateArchiveEntry (Date date) {
		Calendar c = ensureCalendar();
		c.setTime(date);
		Integer month = Integer.valueOf(c.get(java.util.Calendar.MONTH));
		Integer year = Integer.valueOf(c.get(java.util.Calendar.YEAR));

		return getMonthArchiveEntry(month, year);
	}

	@Override
	public ArchiveEntry updateArchiveEntryPost (final ArchiveEntry archiveEntry,
			final Post post) {
		ArchiveEntry updated = null;

		if (Boolean.TRUE.equals(post.listed) && post.published != null) {
			updated = provide().transact(new Work<ArchiveEntry>() {

				@Override
				public ArchiveEntry run () {
					ArchiveEntry latest = getArchiveEntry(archiveEntry.id);

					if (latest.postKeys == null) {
						latest.postKeys = new ArrayList<Key<Post>>();
					}

					boolean found = false;
					for (Key<Post> key : latest.postKeys) {
						if (post.id.longValue() == key.getId()) {
							found = true;
							break;
						}
					}

					if (!found) {
						latest.postKeys.add(Key.create(post));
					}

					provide().save().entity(latest).now();

					return latest;
				}
			});
		}

		return updated;
	}

	@Override
	public void deleteArchiveEntryPost (final ArchiveEntry archiveEntry,
			final Post post) {
		provide().transact(new Work<ArchiveEntry>() {

			@Override
			public ArchiveEntry run () {
				ArchiveEntry latest = getArchiveEntry(archiveEntry.id);

				latest.postKeys.remove(Key.create(post));

				if (latest.postKeys.size() == 0) {
					provide().delete().entities(latest).now();
				} else {
					provide().save().entities(latest).now();
				}

				return latest;
			}
		});
	}

}