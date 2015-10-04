//
//  RelatedPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 18 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.cell.blog.ResultSummaryCell;
import com.willshex.blogwt.client.controller.PostController;
import com.willshex.blogwt.client.controller.SearchController;
import com.willshex.blogwt.client.controller.SearchController.SearchResult;
import com.willshex.blogwt.shared.api.blog.call.GetPostRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostResponse;
import com.willshex.blogwt.shared.api.blog.call.event.GetPostEventHandler;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class RelatedPart extends Composite implements GetPostEventHandler {

	private static RelatedPartUiBinder uiBinder = GWT
			.create(RelatedPartUiBinder.class);

	interface RelatedPartUiBinder extends UiBinder<Widget, RelatedPart> {}

	private HandlerRegistration registration;

	@UiField(provided = true) CellList<SearchResult> clPosts = new CellList<SearchResult>(
			new ResultSummaryCell(), BootstrapGwtCellList.INSTANCE);
	@UiField NoneFoundPanel pnlNoPosts;
	@UiField LoadingPanel pnlLoading;

	public RelatedPart () {
		initWidget(uiBinder.createAndBindUi(this));

		pnlNoPosts.removeFromParent();
		clPosts.setEmptyListWidget(pnlNoPosts);

		pnlLoading.removeFromParent();
		clPosts.setLoadingIndicator(pnlLoading);

		SearchController.get().addDataDisplay(clPosts);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		registration = DefaultEventBus.get().addHandlerToSource(
				GetPostEventHandler.TYPE, PostController.get(), this);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onDetach() */
	@Override
	protected void onDetach () {
		if (registration != null) {
			registration.removeHandler();
		}

		super.onDetach();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.shared.api.blog.call.event.GetPostEventHandler#
	 * getPostSuccess(com.willshex.blogwt.shared.api.blog.call.GetPostRequest,
	 * com.willshex.blogwt.shared.api.blog.call.GetPostResponse) */
	@Override
	public void getPostSuccess (GetPostRequest input, GetPostResponse output) {
		if (output.status == StatusType.StatusTypeSuccess
				&& output.post != null && output.post.tags != null) {
			StringBuffer query = new StringBuffer();
			for (String tag : output.post.tags) {
				if (query.length() > 0) {
					query.append(" OR ");
				}

				query.append("tag:" + tag);
			}

			if (query.length() > 0) {
				query.append(" ");
			}

			query.append("NOT slug:" + output.post.slug);

			SearchController.get().setQuery(query.toString());
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.shared.api.blog.call.event.GetPostEventHandler#
	 * getPostFailure(com.willshex.blogwt.shared.api.blog.call.GetPostRequest,
	 * java.lang.Throwable) */
	@Override
	public void getPostFailure (GetPostRequest input, Throwable caught) {

	}

}
