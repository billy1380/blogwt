//  
//  IPostService.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.post;

import java.util.Collection;
import java.util.List;

import com.spacehopperstudios.service.IService;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.PostContent;
import com.willshex.blogwt.shared.api.datatype.PostSortType;
import com.willshex.blogwt.shared.api.datatype.User;

/**
 * 
 * @author William Shakour (billy1380)
 *
 */
public interface IPostService extends IService {
	public static final String NAME = "blogwt.post";

	/**
	 * @param id
	 * @return
	 */
	public Post getPost (Long id);

	/**
	 * @param post
	 * @return
	 */
	public Post addPost (Post post);

	/**
	 * @param post
	 * @return
	 */
	public Post updatePost (Post post);

	/**
	 * @param post
	 */
	public void deletePost (Post post);

	/**
	 * 
	 * 	
	 * @param user
	 *            , get all posts published by user
	 * @param showAll
	 *            , if true show even unpublished posts
	 * @param includeContents
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public List<Post> getUserViewablePosts (User user, Boolean showAll,
			Boolean includeContents, Integer start, Integer count,
			PostSortType sortBy, SortDirectionType sortDirection);

	/**
	 * 
	 * @param showAll
	 * @param includeContents
	  * @param start
	 * @param count
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public List<Post> getPosts (Boolean showAll, Boolean includeContents,
			Integer start, Integer count, PostSortType sortBy,
			SortDirectionType sortDirection);

	/**
	 * 
	 * @param user
	 * @param showAll
	 * @return
	 */
	public Long getUserViewablePostsCount (User user, Boolean showAll);

	/**
	 * 
	 * @param showAll
	 * @return
	 */
	public Long getPostsCount (Boolean showAll);

	/**
	 * 
	 * @param slug
	 * @return
	 */
	public Post getSlugPost (String slug);

	/**
	 * @param post
	 * @return
	 */
	public PostContent getPostContent (Post post);

	/**
	 * @param ids
	 */
	public List<Post> getPostBatch (Collection<Long> ids);

}