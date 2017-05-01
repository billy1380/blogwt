//  
//  TagService.java
//  blogwt
//
//  Created by William Shakour on July 15, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.tag;

import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;
import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.LoadType;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.PostSortType;
import com.willshex.blogwt.shared.api.datatype.Tag;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PostHelper;

final class TagService implements ITagService {
	public String getName () {
		return NAME;
	}

	@Override
	public Tag getTag (Long id) {
		return load().id(id.longValue()).now();
	}

	private LoadType<Tag> load () {
		return provide().load().type(Tag.class);
	}

	@Override
	public Tag addTag (Tag tag) {
		if (tag.created == null) {
			tag.created = new Date();
		}

		tag.name = tag.name.toLowerCase();
		tag.slug = PostHelper.slugify(tag.name);

		if (tag.posts != null) {
			for (Post post : tag.posts) {
				if (tag.postKeys == null) {
					tag.postKeys = new ArrayList<Key<Post>>();
				}

				tag.postKeys.add(Key.create(post));
			}
		}

		Key<Tag> key = provide().save().entity(tag).now();
		tag.id = keyToId(key);

		return tag;
	}

	@Override
	public void deleteTag (Tag tag) {
		provide().delete().entity(tag).now();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.tag.ITagService#getTags() */
	@Override
	public List<Tag> getTags () {
		return load().list();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.tag.ITagService#generateTags() */
	@Override
	public void generateTags () {
		Map<String, Tag> tagLookup = new HashMap<String, Tag>();
		List<Post> posts;
		Tag tag;

		Pager pager = PagerHelper.createDefaultPager();

		do {
			posts = PostServiceProvider.provide().getPosts(Boolean.FALSE,
					Boolean.FALSE, pager.start, pager.count,
					PostSortType.PostSortTypePublished,
					SortDirectionType.SortDirectionTypeDescending);

			if (posts != null) {
				PagerHelper.moveForward(pager);

				for (Post post : posts) {
					if (post.tags != null) {
						for (String postTag : post.tags) {
							tag = tagLookup.get(postTag.toLowerCase());

							if (tag == null) {
								tag = new Tag().name(postTag.toLowerCase())
										.posts(new ArrayList<Post>());
								tagLookup.put(tag.name, tag);
							}

							tag.posts.add(post);
						}
					}
				}
			}
		} while (posts != null && posts.size() >= pager.count.intValue());

		if (tagLookup.size() > 0) {
			addTagBatch(tagLookup.values());
		}

	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.tag.ITagService#addTagBatch(java.
	 * util.Collection) */
	@Override
	public void addTagBatch (Collection<Tag> tags) {
		for (Tag tag : tags) {
			if (tag.created == null) {
				tag.created = new Date();
			}

			tag.name = tag.name.toLowerCase();
			tag.slug = PostHelper.slugify(tag.name);

			if (tag.posts != null) {
				for (Post post : tag.posts) {
					if (tag.postKeys == null) {
						tag.postKeys = new ArrayList<Key<Post>>();
					}

					tag.postKeys.add(Key.create(post));
				}
			}
		}

		provide().save().entities(tags).now();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.tag.ITagService#getSlugTag(java.
	 * lang.String) */
	@Override
	public Tag getSlugTag (String slug) {
		return PersistenceHelper.one(load().filter("slug", slug));
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.tag.ITagService#addTagPost(com.
	 * willshex .blogwt.shared.api.datatype.Tag,
	 * com.willshex.blogwt.shared.api.datatype.Post) */
	@Override
	public void addTagPost (final Tag tag, final Post post) {
		provide().transact(new Work<Void>() {

			@Override
			public Void run () {
				Tag latest = getTag(tag.id);

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

				return null;
			}
		});
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.tag.ITagService#removeTagPost(com
	 * .willshex.blogwt.shared.api.datatype.Tag,
	 * com.willshex.blogwt.shared.api.datatype.Post) */
	@Override
	public void removeTagPost (final Tag tag, final Post post) {
		provide().transact(new Work<Void>() {

			@Override
			public Void run () {
				Tag latest = getTag(tag.id);

				if (latest.postKeys == null) {
					latest.postKeys = new ArrayList<Key<Post>>();
				}

				Key<Post> foundKey = null;
				for (Key<Post> key : latest.postKeys) {
					if (post.id.longValue() == key.getId()) {
						foundKey = key;
						break;
					}
				}

				if (foundKey != null) {
					if (latest.postKeys.size() == 1) {
						deleteTag(latest);
					} else {
						latest.postKeys.remove(foundKey);
						provide().save().entity(latest).now();
					}
				}

				return null;
			}
		});
	}
}