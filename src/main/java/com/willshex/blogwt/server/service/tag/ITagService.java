//  
//  ITagService.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 15, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.tag;

import java.util.Collection;
import java.util.List;

import com.spacehopperstudios.service.IService;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.Tag;

public interface ITagService extends IService {
	public static final String NAME = "blogwt.tag";

	/**
	* @param id
	* @return
	*/
	public Tag getTag (Long id);

	/**
	* @param name
	* @return
	*/
	public Tag getNamedTag (String name);

	/**
	* @param tag
	* @return
	*/
	public Tag addTag (Tag tag);

	/**
	* @param tag
	*/
	public void deleteTag (Tag tag);

	/**
	 * Gets all tags
	 * @return
	 */
	public List<Tag> getTags ();

	/**
	 * Generates tags based on existing posts
	 */
	public void generateTags ();

	/**
	 * Adds tag batch
	 * @param tags
	 */
	public void addTagBatch (Collection<Tag> tags);

	/**
	 * 
	 * @param tag
	 * @param post
	 */
	public void addTagPost (Tag tag, Post post);

	/**
	 * 
	 * @param tag
	 * @param post
	 */
	public void removeTagPost (Tag tag, Post post);

}