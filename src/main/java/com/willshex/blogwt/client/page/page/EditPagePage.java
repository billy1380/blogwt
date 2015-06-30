//
//  EditPagePage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 22 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.page;

import java.util.List;

import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.NavigationController.Stack;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.page.wizard.WizardDialogPage;
import com.willshex.blogwt.client.wizard.PagePlanFinishedHandler;
import com.willshex.blogwt.client.wizard.WizardPage;
import com.willshex.blogwt.client.wizard.page.EditPageWizardPage;
import com.willshex.blogwt.client.wizard.page.SelectPostWizardPage;
import com.willshex.blogwt.client.wizard.PagePlan.PagePlanBuilder;

/**
 * @author William Shakour (billy1380)
 *
 */
public class EditPagePage extends WizardDialogPage implements
		NavigationChangedEventHandler, PagePlanFinishedHandler {

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

		super.onAttach();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.wizard.PagePlanFinishedHandler#onfinished
	 * (java.util.List) */
	@Override
	public void onfinished (List<WizardPage<?>> pages) {

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

		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.wizard.PagePlanFinishedHandler#onCancelled() */
	@Override
	public void onCancelled () {

	}

}
