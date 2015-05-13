//
//  SetupBlogPage.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 13 May 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.client.page.blog;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.page.wizard.WizardDialogPage;
import com.willshex.blogwt.client.wizard.PagePlan.PagePlanBuilder;
import com.willshex.blogwt.client.wizard.PagePlanFinishedHandler;
import com.willshex.blogwt.client.wizard.WizardPage;
import com.willshex.blogwt.client.wizard.page.AddUserWizardPage;
import com.willshex.blogwt.client.wizard.page.BlogPropertiesWizardPage;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.User;

/**
 * @author William Shakour (billy1380)
 *
 */
public class SetupBlogPage extends WizardDialogPage {

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
		for (WizardPage<?> page : pages) {
			if (page instanceof BlogPropertiesWizardPage) {
				List<Property> blogProperties = ((BlogPropertiesWizardPage) page)
						.getData();

				GWT.log(Integer.toString(blogProperties.toArray().length));
			} else if (page instanceof AddUserWizardPage) {
				User user = ((AddUserWizardPage) page).getData();

				GWT.log(user.toString());
			}
		}
	}

}
