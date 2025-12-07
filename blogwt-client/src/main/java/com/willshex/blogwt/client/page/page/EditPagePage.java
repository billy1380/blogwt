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

import com.google.gwt.core.client.GWT;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.page.event.CreatePageEventHandler;
import com.willshex.blogwt.client.api.page.event.GetPageEventHandler;
import com.willshex.blogwt.client.api.page.event.UpdatePageEventHandler;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.PageController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.PageTypeHelper;
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
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class EditPagePage extends WizardDialogPage
		implements PagePlanFinishedHandler, CreatePageEventHandler,
		GetPageEventHandler, UpdatePageEventHandler {

	public EditPagePage () {
		super(PageType.EditPagePageType);
	}
	@Override
	protected void onAttach () {
		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				(p, c) -> {
					String action = c.getAction();
					if (action != null && "new".equalsIgnoreCase(action)) {
						setPlan((new PagePlanBuilder())
								.addPage(new EditPageWizardPage())
								.addPage(new SelectPostWizardPage())
								.setName("New Page").addFinishedHandler(this)
								.build());
					} else {
						Page page = null;
						if (c.getParameterCount() >= 1) {
							switch (c.getAction()) {
							case "id":
								(page = new Page())
										.id(Long.valueOf(c.getParameter(0)));
								break;
							case "slug":
								page = new Page().slug(c.getParameter(0));
								break;
							}
						} else {
							page = new Page().slug(c.getAction());
						}

						if (page != null) {
							PageController.get().getPage(page, true);
						}
					}
				}));
		register(DefaultEventBus.get().addHandlerToSource(
				CreatePageEventHandler.TYPE, PageController.get(), this));
		register(DefaultEventBus.get().addHandlerToSource(
				GetPageEventHandler.TYPE, PageController.get(), this));
		register(DefaultEventBus.get().addHandlerToSource(
				UpdatePageEventHandler.TYPE, PageController.get(), this));

		super.onAttach();
	}
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
	@Override
	public void onCancelled () {}
	@Override
	public void createPageSuccess (CreatePageRequest input,
			CreatePageResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			PageTypeHelper.show(PageType.PageDetailPageType, input.page.slug);
		}
	}
	@Override
	public void createPageFailure (CreatePageRequest input, Throwable caught) {
		GWT.log("createPageFailure", caught);
	}
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
	@Override
	public void getPageFailure (GetPageRequest input, Throwable caught) {}
	@Override
	public void updatePageSuccess (UpdatePageRequest input,
			UpdatePageResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			PageTypeHelper.show(PageType.PageDetailPageType, input.page.slug);
		}
	}
	@Override
	public void updatePageFailure (UpdatePageRequest input, Throwable caught) {}

}
