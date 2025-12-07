// 
//  IGeneratedDownloadService.java
//  blogwt
// 
//  Created by William Shakour on June 26, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.generateddownload;

import java.util.List;

import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownload;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownloadSortType;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.service.IService;

public interface IGeneratedDownloadService extends IService {

	public static final String NAME = "blogwt.generateddownload";

	/**
	* @param id
	* @return
	*/
	public GeneratedDownload getGeneratedDownload (Long id);

	/**
	* @param generatedDownload
	* @return
	*/
	public GeneratedDownload addGeneratedDownload (
			GeneratedDownload generatedDownload);

	/**
	* @param generatedDownload
	* @return
	*/
	public GeneratedDownload updateGeneratedDownload (
			GeneratedDownload generatedDownload);

	/**
	* @param generatedDownload
	*/
	public void deleteGeneratedDownload (GeneratedDownload generatedDownload);

	/**
	 * Get user generated downloads
	 * 
	 * @param user
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public List<GeneratedDownload> getUserGeneratedDownloads (User user,
			Integer start, Integer count, GeneratedDownloadSortType sortBy,
			SortDirectionType sortDirection);

	/**
	 * Get generated Downloads
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public List<GeneratedDownload> getGeneratedDownloads (Integer start,
			Integer count, GeneratedDownloadSortType sortBy,
			SortDirectionType sortDirection);

}