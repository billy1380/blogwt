//  
//  IPageService.java
//  blogwt
//
//  Created by William Shakour on June 22, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.page;

import java.util.List;

import com.spacehopperstudios.service.IService;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.PageSortType;

public interface IPageService extends IService {
	public static final String NAME = "blogwt.page";

	/**
	* @param id
	* @return
	*/
	public Page getPage (Long id);

	/**
	* @param page
	* @return
	*/
	public Page addPage (Page page);

	/**
	* @param page
	* @return
	*/
	public Page updatePage (Page page);

	/**
	* @param page
	*/
	public void deletePage (Page page);

	/**
	 * @param slug
	 * @param includePostContents
	 * @return
	 */
	public Page getSlugPage (String slug, Boolean includePostContents);

	/**
	 * @param id
	 * @param includePostContents
	 * @return
	 */
	public Page getPage (Long id, Boolean includePostContents);

	/**
	 * 
	 * @param includePostContents
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public List<Page> getPages (Boolean includePostContents, Integer start,
			Integer count, PageSortType sortBy, SortDirectionType sortDirection);

	/**
	 * 
	 * @param partialSlug
	 * @param includePostContents
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public List<Page> getPartialSlugPages (String partialSlug,
			Boolean includePostContents, Integer start, Integer count,
			PageSortType sortBy, SortDirectionType sortDirection);

	/**
	 * 
	 */
	public void indexAll ();

}