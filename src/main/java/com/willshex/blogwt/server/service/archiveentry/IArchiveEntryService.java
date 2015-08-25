//  
//  IArchiveEntryService.java
//  xsdwsdl2code
//
//  Created by William Shakour on August 25, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.server.service.archiveentry;

import java.util.List;

import com.spacehopperstudios.service.IService;
import com.willshex.blogwt.shared.api.datatype.ArchiveEntry;
import com.willshex.blogwt.shared.api.datatype.Post;

public interface IArchiveEntryService extends IService {

	public static final String NAME = "blogwt.archiveentry";

	/**
	* @param id
	* @return
	*/
	public ArchiveEntry getArchiveEntry (Long id);

	/**
	* @param archiveEntry
	* @return
	*/
	public ArchiveEntry addArchiveEntry (ArchiveEntry archiveEntry);

	/**
	* @param archiveEntry
	* @return
	*/
	public ArchiveEntry updateArchiveEntry (ArchiveEntry archiveEntry);

	/**
	* @param archiveEntry
	*/
	public void deleteArchiveEntry (ArchiveEntry archiveEntry);

	/**
	 * Archive post
	 * @param post
	 */
	public void archivePost (Post post);

	/**
	 * Get month archive entry
	 * @param month
	 * @param year
	 * @return
	 */
	public ArchiveEntry getMonthArchiveEntry (Integer month, Integer year);

	/**
	 * Get archive entries
	 * @return
	 */
	public List<ArchiveEntry> getArchiveEntries ();

}