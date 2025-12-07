// 
//  GeneratedDownloadService.java
//  blogwt
// 
//  Created by William Shakour on June 26, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.service.generateddownload;

import static com.willshex.blogwt.server.helper.PersistenceHelper.id;
import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.willshex.blogwt.server.background.generatedownload.GenerateDownloadServlet;
import com.willshex.blogwt.server.helper.GcsHelper;
import com.willshex.blogwt.server.helper.GeneratedDownloadHelper;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.server.service.ISortable;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownload;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownloadSortType;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownloadStatusType;
import com.willshex.blogwt.shared.api.datatype.User;

final class GeneratedDownloadService implements IGeneratedDownloadService,
		ISortable<GeneratedDownloadSortType> {
	public String getName() {
		return NAME;
	}

	@Override
	public GeneratedDownload getGeneratedDownload(Long id) {
		return id(load(), id);
	}

	/**
	 * @return
	 */
	private LoadType<GeneratedDownload> load() {
		return provide().load().type(GeneratedDownload.class);
	}

	@Override
	public GeneratedDownload addGeneratedDownload(
			GeneratedDownload generatedDownload) {
		if (generatedDownload.created == null) {
			generatedDownload.created = new Date();
		}

		generatedDownload.userKey = Key.create(generatedDownload.user);
		generatedDownload.status = GeneratedDownloadStatusType.GeneratedDownloadStatusTypeGenerating;

		Key<GeneratedDownload> generatedKey = provide().save()
				.entity(generatedDownload).now();
		generatedDownload.id = Long.valueOf(generatedKey.getId());

		GenerateDownloadServlet.generateDownload(generatedDownload);

		return generatedDownload;
	}

	@Override
	public GeneratedDownload updateGeneratedDownload(
			GeneratedDownload generatedDownload) {
		provide().save().entity(generatedDownload).now();

		return generatedDownload;
	}

	@Override
	public void deleteGeneratedDownload(GeneratedDownload generatedDownload) {
		GcsHelper.delete(GeneratedDownloadHelper.path(generatedDownload));

		provide().delete().entity(generatedDownload).now();
	}
	@Override
	public List<GeneratedDownload> getUserGeneratedDownloads(User user,
			Integer start, Integer count, GeneratedDownloadSortType sortBy,
			SortDirectionType sortDirection) {
		return PersistenceHelper.pagedAndSorted(load().filter(
				map(GeneratedDownloadSortType.GeneratedDownloadSortTypeUser),
				user), start, count, sortBy, this, sortDirection);
	}
	@Override
	public String map(GeneratedDownloadSortType sortBy) {
		String mapped = sortBy.toString();

		if (sortBy == GeneratedDownloadSortType.GeneratedDownloadSortTypeUser) {
			mapped += "Key";
		}

		return mapped;
	}
	@Override
	public List<GeneratedDownload> getGeneratedDownloads(Integer start,
			Integer count, GeneratedDownloadSortType sortBy,
			SortDirectionType sortDirection) {
		return PersistenceHelper.pagedAndSorted(load(), start, count, sortBy,
				this, sortDirection);
	}

}
