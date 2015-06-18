//
//  PropertiesPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 18 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.blog.admin;

import java.util.List;

import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.page.wizard.WizardDialogPage;
import com.willshex.blogwt.client.wizard.PagePlan.PagePlanBuilder;
import com.willshex.blogwt.client.wizard.PagePlanFinishedHandler;
import com.willshex.blogwt.client.wizard.WizardPage;
import com.willshex.blogwt.client.wizard.page.BlogPropertiesWizardPage;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PropertiesPage extends WizardDialogPage {

	@Override
	protected void onAttach () {
		super.onAttach();
	}

	/**
	* 
	*/
	public PropertiesPage () {
		super(PageType.PropertiesPageType);

		setPlan((new PagePlanBuilder()).addPage(new BlogPropertiesWizardPage())
				.setName("Site <small>settings</small>")
				.addFinishedHandler(new PagePlanFinishedHandler() {

					@Override
					public void onfinished (List<WizardPage<?>> pages) {
						PropertiesPage.this.finish(pages);
					}
				}).build());
	}

	/**
	* @param pages
	*/
	protected void finish (List<WizardPage<?>> pages) {

	}
}