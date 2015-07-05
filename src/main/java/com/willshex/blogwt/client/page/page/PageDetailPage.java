//
//  PageDetailPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 22 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.page;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.controller.NavigationController.Stack;
import com.willshex.blogwt.client.controller.PageController;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.helper.PermissionHelper;
import com.willshex.blogwt.shared.api.page.call.DeletePageRequest;
import com.willshex.blogwt.shared.api.page.call.DeletePageResponse;
import com.willshex.blogwt.shared.api.page.call.GetPageRequest;
import com.willshex.blogwt.shared.api.page.call.GetPageResponse;
import com.willshex.blogwt.shared.api.page.call.event.DeletePageEventHandler;
import com.willshex.blogwt.shared.api.page.call.event.GetPageEventHandler;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PageDetailPage extends com.willshex.blogwt.client.page.Page
		implements NavigationChangedEventHandler, GetPageEventHandler,
		DeletePageEventHandler {

	private static PageDetailPageUiBinder uiBinder = GWT
			.create(PageDetailPageUiBinder.class);

	interface PageDetailPageUiBinder extends UiBinder<Widget, PageDetailPage> {}

	private Page page;
	@UiField HTMLPanel pnlLoading;
	@UiField HTMLPanel pnlContent;

	@UiField InlineHyperlink lnkEditPage;
	@UiField Button btnDeletePage;

	public PageDetailPage () {
		super(PageType.PageDetailPageType);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("btnDeletePage")
	void onBtnDeletePage (ClickEvent event) {
		if (page != null
				&& Window.confirm("Are you sure you want to delete \""
						+ page.title + "\"")) {
			PageController.get().deletePage(page);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.event.NavigationChangedEventHandler#
	 * navigationChanged
	 * (com.willshex.blogwt.client.controller.NavigationController.Stack,
	 * com.willshex.blogwt.client.controller.NavigationController.Stack) */
	@Override
	public void navigationChanged (Stack previous, Stack current) {
		if (current.getAction() != null) {
			String slug;
			if ((slug = current.getAction()) != null) {
				PageController.get().getPage(new Page().slug(slug), true);
				pnlLoading.setVisible(true);
			}
		}

		boolean canChange = SessionController.get().isAuthorised(
				Arrays.asList(PermissionHelper
						.create(PermissionHelper.MANAGE_PAGES)));
		lnkEditPage.setVisible(canChange);
		btnDeletePage.setVisible(canChange);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.page.call.event.DeletePageEventHandler
	 * #deletePageSuccess
	 * (com.willshex.blogwt.shared.api.page.call.DeletePageRequest,
	 * com.willshex.blogwt.shared.api.page.call.DeletePageResponse) */
	@Override
	public void deletePageSuccess (DeletePageRequest input,
			DeletePageResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			PageType.PagesPageType.show();
		} else {

		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.page.call.event.DeletePageEventHandler
	 * #deletePageFailure
	 * (com.willshex.blogwt.shared.api.page.call.DeletePageRequest,
	 * java.lang.Throwable) */
	@Override
	public void deletePageFailure (DeletePageRequest input, Throwable caught) {}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.shared.api.page.call.event.GetPageEventHandler#
	 * getPageSuccess(com.willshex.blogwt.shared.api.page.call.GetPageRequest,
	 * com.willshex.blogwt.shared.api.page.call.GetPageResponse) */
	@Override
	public void getPageSuccess (GetPageRequest input, GetPageResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			show(page = output.page);
		} else {
			PageType.PagesPageType.show();
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.shared.api.page.call.event.GetPageEventHandler#
	 * getPageFailure(com.willshex.blogwt.shared.api.page.call.GetPageRequest,
	 * java.lang.Throwable) */
	@Override
	public void getPageFailure (GetPageRequest input, Throwable caught) {}

	private void show (Page page) {

		pnlLoading.setVisible(false);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#reset() */
	@Override
	protected void reset () {
		super.reset();

		lnkEditPage.setTargetHistoryToken(PageType.EditPagePageType
				.asTargetHistoryToken(""));

		pnlContent.getElement().setInnerHTML("");
		pnlLoading.setVisible(true);
	}

}
