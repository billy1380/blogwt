//
//  EditPagePage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 22 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.page;

import java.util.ArrayList;
import java.util.List;

import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.NavigationController.Stack;
import com.willshex.blogwt.client.controller.PageController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.page.wizard.WizardDialogPage;
import com.willshex.blogwt.client.wizard.PagePlan.PagePlanBuilder;
import com.willshex.blogwt.client.wizard.PagePlanFinishedHandler;
import com.willshex.blogwt.client.wizard.WizardPage;
import com.willshex.blogwt.client.wizard.page.EditPageWizardPage;
import com.willshex.blogwt.client.wizard.page.SelectPostWizardPage;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.page.call.CreatePageRequest;
import com.willshex.blogwt.shared.api.page.call.CreatePageResponse;
import com.willshex.blogwt.shared.api.page.call.GetPageRequest;
import com.willshex.blogwt.shared.api.page.call.GetPageResponse;
import com.willshex.blogwt.shared.api.page.call.UpdatePageRequest;
import com.willshex.blogwt.shared.api.page.call.UpdatePageResponse;
import com.willshex.blogwt.shared.api.page.call.event.CreatePageEventHandler;
import com.willshex.blogwt.shared.api.page.call.event.GetPageEventHandler;
import com.willshex.blogwt.shared.api.page.call.event.UpdatePageEventHandler;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class EditPagePage extends WizardDialogPage implements
		NavigationChangedEventHandler, PagePlanFinishedHandler,
		CreatePageEventHandler, GetPageEventHandler, UpdatePageEventHandler {

	public EditPagePage () {
		super(PageType.EditPagePageType);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#onAttach() */
	@Override
	protected void onAttach () {
		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				this));
		register(DefaultEventBus.get().addHandlerToSource(
				CreatePageEventHandler.TYPE, PageController.get(), this));
		register(DefaultEventBus.get().addHandlerToSource(
				GetPageEventHandler.TYPE, PageController.get(), this));
		register(DefaultEventBus.get().addHandlerToSource(
				UpdatePageEventHandler.TYPE, PageController.get(), this));

		super.onAttach();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.wizard.PagePlanFinishedHandler#onfinished
	 * (java.util.List) */
	@Override
	public void onfinished (List<WizardPage<?>> pages) {
		Page page = null;
		Post post = null;
		for (WizardPage<?> wizardPage : pages) {
			if (wizardPage instanceof EditPageWizardPage) {
				page = ((EditPageWizardPage) wizardPage).getData();

				if (page.posts != null) {
					page.posts.clear();
				}
			} else if (wizardPage instanceof SelectPostWizardPage) {
				if (page.posts == null) {
					page.posts = new ArrayList<Post>();
				}

				post = ((SelectPostWizardPage) wizardPage).getData();
				page.posts.add(post);
			}
		}

		if (page.id == null) {
			PageController.get().createPage(page);
		} else {
			PageController.get().updatePage(page);
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
		String action = current.getAction();
		if (action != null && "new".equalsIgnoreCase(action)) {
			setPlan((new PagePlanBuilder()).addPage(new EditPageWizardPage())
					.addPage(new SelectPostWizardPage()).setName("New Page")
					.addFinishedHandler(this).build());
		} else {
			Page page = null;
			if (current.getParameterCount() >= 1) {
				switch (current.getAction()) {
				case "id":
					(page = new Page())
							.id(Long.valueOf(current.getParameter(0)));
					break;
				case "slug":
					page = new Page().slug(current.getParameter(0));
					break;
				}
			} else {
				page = new Page().slug(current.getAction());
			}

			if (page != null) {
				PageController.get().getPage(page, true);
			}
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.wizard.PagePlanFinishedHandler#onCancelled() */
	@Override
	public void onCancelled () {}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.page.call.event.CreatePageEventHandler
	 * #createPageSuccess
	 * (com.willshex.blogwt.shared.api.page.call.CreatePageRequest,
	 * com.willshex.blogwt.shared.api.page.call.CreatePageResponse) */
	@Override
	public void createPageSuccess (CreatePageRequest input,
			CreatePageResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			PageType.PageDetailPageType.show(input.page.slug);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.page.call.event.CreatePageEventHandler
	 * #createPageFailure
	 * (com.willshex.blogwt.shared.api.page.call.CreatePageRequest,
	 * java.lang.Throwable) */
	@Override
	public void createPageFailure (CreatePageRequest input, Throwable caught) {}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.shared.api.page.call.event.GetPageEventHandler#
	 * getPageSuccess(com.willshex.blogwt.shared.api.page.call.GetPageRequest,
	 * com.willshex.blogwt.shared.api.page.call.GetPageResponse) */
	@Override
	public void getPageSuccess (GetPageRequest input, GetPageResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			PagePlanBuilder builder = new PagePlanBuilder();

			EditPageWizardPage ewp = new EditPageWizardPage();
			ewp.setData(output.page);

			builder.addPage(ewp);

			SelectPostWizardPage spwp = new SelectPostWizardPage();
			for (Post post : output.page.posts) {
				spwp = new SelectPostWizardPage();
				spwp.setData(post);
				builder.addPage(spwp, post != output.page.posts.get(0));
			}

			setPlan(builder.setName("Edit " + output.page.title)
					.addFinishedHandler(this).build());
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.shared.api.page.call.event.GetPageEventHandler#
	 * getPageFailure(com.willshex.blogwt.shared.api.page.call.GetPageRequest,
	 * java.lang.Throwable) */
	@Override
	public void getPageFailure (GetPageRequest input, Throwable caught) {}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.page.call.event.UpdatePageEventHandler
	 * #updatePageSuccess
	 * (com.willshex.blogwt.shared.api.page.call.UpdatePageRequest,
	 * com.willshex.blogwt.shared.api.page.call.UpdatePageResponse) */
	@Override
	public void updatePageSuccess (UpdatePageRequest input,
			UpdatePageResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			PageType.PageDetailPageType.show(input.page.slug);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.page.call.event.UpdatePageEventHandler
	 * #updatePageFailure
	 * (com.willshex.blogwt.shared.api.page.call.UpdatePageRequest,
	 * java.lang.Throwable) */
	@Override
	public void updatePageFailure (UpdatePageRequest input, Throwable caught) {}

}
