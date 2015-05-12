//  
//  IPostService.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.post;

import java.util.List;

import com.spacehopperstudios.service.IService;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Post;
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
	 * @throws DataAccessException
	 */
	public Post getPost (Long id);

	/**
	 * @param post
	 * @return
	 * @throws DataAccessException
	 */
	public Post addPost (Post post);

	/**
	 * @param post
	 * @return
	 * @throws DataAccessException
	 */
	public Post updatePost (Post post);

	/**
	 * @param post
	 * @throws DataAccessException
	 */
	public void deletePost (Post post);

	/**
	 * 
	 * 	 * 
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
	 * @throws DataAccessException
	 */
	public List<Post> getPosts (Boolean showAll, Boolean includeContents,
			Integer start, Integer count, PostSortType sortBy,
			SortDirectionType sortDirection);

	/**
	 * 
	 * @param user
	 * @param showAll
	 * @return
	 * @throws DataAccessException
	 */
	public Long getUserViewablePostsCount (User user, Boolean showAll);

	/**
	 * 
	 * @param showAll
	 * @return
	 * @throws DataAccessException
	 */
	public Long getPostsCount (Boolean showAll);

	/**
	 * 
	 * @param slug
	 * @return
	 * @throws DataAccessException
	 */
	public Post getSlugPost (String slug);

}