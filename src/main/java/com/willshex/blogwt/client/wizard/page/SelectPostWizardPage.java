//
//  SelectPostWizardPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 30 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.wizard.page;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.helper.PostHelper;
import com.willshex.blogwt.client.wizard.WizardPage;
import com.willshex.blogwt.shared.api.datatype.Post;

/**
 * @author William Shakour (billy1380)
 *
 */
public class SelectPostWizardPage extends Composite implements WizardPage<Post> {

	private static SelectPostWizardPageUiBinder uiBinder = GWT
			.create(SelectPostWizardPageUiBinder.class);

	interface SelectPostWizardPageUiBinder extends
			UiBinder<Widget, SelectPostWizardPage> {}

	@UiField SuggestBox txtPost;
	private Post post;

	public SelectPostWizardPage () {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#isRepeatable() */
	@Override
	public boolean isRepeatable () {
		return true;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getData() */
	@Override
	public Post getData () {
		if (post == null) {
			post = new Post();
		}

		if (post.id == null) {
			post.slug = txtPost.getValue();
		}

		return post;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.wizard.WizardPage#setData(java.lang.Object) */
	@Override
	public void setData (Post data) {
		post = data;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getPageTitle() */
	@Override
	public String getPageTitle () {
		return "Add Post";
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getPageDescription() */
	@Override
	public String getPageDescription () {
		return PostHelper
				.makeMarkup("Enter the id or slug of an existing post to add it as a section on the page.");
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getBody() */
	@Override
	public Widget getBody () {
		return this;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#another() */
	@Override
	public WizardPage<?> another () {
		return new SelectPostWizardPage();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#validate() */
	@Override
	public boolean validate () {
		return true;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getAutoFocusField() */
	@Override
	public Focusable getAutoFocusField () {
		return null;
	}

}
