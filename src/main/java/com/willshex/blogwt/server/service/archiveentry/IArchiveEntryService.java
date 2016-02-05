//  
//  IArchiveEntryService.java
//  blogwt
//
//  Created by William Shakour on August 25, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.server.service.archiveentry;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.willshex.blogwt.shared.api.datatype.ArchiveEntry;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.service.IService;

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

	/**
	 * Generate archive
	 */
	public void generateArchive ();

	/**
	 * Add archive entry batch
	 * @param archiveEntries
	 */
	public void addArchiveEntryBatch (Collection<ArchiveEntry> archiveEntries);

	/**
	 * Get date archive entry
	 * @param date
	 * @return
	 */
	public ArchiveEntry getDateArchiveEntry (Date date);

	/**
	 * 
	 * @param archiveEntry
	 * @param post
	 * @return
	 */
	public ArchiveEntry updateArchiveEntryPost (ArchiveEntry archiveEntry,
			Post post);

	/**
	 * Delete post archive entry
	 * @param post
	 */
	public void deleteArchiveEntryPost (ArchiveEntry archiveEntry, Post post);

}