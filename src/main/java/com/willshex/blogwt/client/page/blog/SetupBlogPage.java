//
//  SetupBlogPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 13 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.blog;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.page.wizard.WizardDialogPage;
import com.willshex.blogwt.client.wizard.PagePlan.PagePlanBuilder;
import com.willshex.blogwt.client.wizard.PagePlanFinishedHandler;
import com.willshex.blogwt.client.wizard.WizardPage;
import com.willshex.blogwt.client.wizard.page.AddUserWizardPage;
import com.willshex.blogwt.client.wizard.page.BlogPropertiesWizardPage;
import com.willshex.blogwt.shared.api.blog.call.SetupBlogRequest;
import com.willshex.blogwt.shared.api.blog.call.SetupBlogResponse;
import com.willshex.blogwt.shared.api.blog.call.event.SetupBlogEventHandler;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class SetupBlogPage extends WizardDialogPage implements
		SetupBlogEventHandler {

	@Override
	protected void onAttach () {
		super.onAttach();

		register(DefaultEventBus.get().addHandlerToSource(
				SetupBlogEventHandler.TYPE, PropertyController.get(), this));
	}

	/**
	 * 
	 */
	public SetupBlogPage () {
		super(PageType.SetupBlogPageType);

		setPlan((new PagePlanBuilder()).addPage(new BlogPropertiesWizardPage())
				.addPage(new AddUserWizardPage()).setName("Setup Blog")
				.addFinishedHandler(new PagePlanFinishedHandler() {

					@Override
					public void onfinished (List<WizardPage<?>> pages) {
						SetupBlogPage.this.finish(pages);
					}
				}).build());
	}

	/**
	 * @param pages
	 */
	protected void finish (List<WizardPage<?>> pages) {
		List<User> users = null;
		List<Property> properties = null;
		for (WizardPage<?> page : pages) {
			if (page instanceof BlogPropertiesWizardPage) {
				properties = ((BlogPropertiesWizardPage) page).getData();
			} else if (page instanceof AddUserWizardPage) {
				if (users == null) {
					users = new ArrayList<User>();
				}

				users.add(((AddUserWizardPage) page).getData());
			}
		}

		PropertyController.get().setupBlog(properties, users);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.blog.call.event.SetupBlogEventHandler
	 * #setupBlogSuccess
	 * (com.willshex.blogwt.shared.api.blog.call.SetupBlogRequest,
	 * com.willshex.blogwt.shared.api.blog.call.SetupBlogResponse) */
	@Override
	public void setupBlogSuccess (SetupBlogRequest input,
			SetupBlogResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			Window.Location.reload();
		} else {
			GWT.log("setupBlogFailure - input:" + input.toString()
					+ " - output:" + output);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.blog.call.event.SetupBlogEventHandler
	 * #setupBlogFailure
	 * (com.willshex.blogwt.shared.api.blog.call.SetupBlogRequest,
	 * java.lang.Throwable) */
	@Override
	public void setupBlogFailure (SetupBlogRequest input, Throwable caught) {
		GWT.log("setupBlogFailure - input:" + input.toString(), caught);
	}

}
