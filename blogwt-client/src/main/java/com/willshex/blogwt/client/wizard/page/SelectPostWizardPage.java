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
import com.willshex.blogwt.client.controller.PostController;
import com.willshex.blogwt.client.helper.PostHelper;
import com.willshex.blogwt.client.part.BootstrapGwtSuggestBox;
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

	@UiField(provided = true) SuggestBox txtPost = new SuggestBox(
			PostController.get().oracle());

	//	private Post post;

	public SelectPostWizardPage () {
		initWidget(uiBinder.createAndBindUi(this));

		BootstrapGwtSuggestBox.INSTANCE.styles().ensureInjected();
	}
	@Override
	public boolean isRepeatable () {
		return true;
	}
	@Override
	public Post getData () {
		return new Post().slug(txtPost.getValue());
	}
	@Override
	public void setData (Post data) {
		if (data != null) {
			txtPost.setText(data.slug == null ? data.id.toString() : data.slug);
		}
	}
	@Override
	public String getPageTitle () {
		return "Add Post";
	}
	@Override
	public String getPageDescription () {
		return PostHelper
				.makeMarkup("Enter the id or slug of an existing post to add it as a section on the page.");
	}
	@Override
	public Widget getBody () {
		return this;
	}
	@Override
	public WizardPage<?> another () {
		return new SelectPostWizardPage();
	}
	@Override
	public boolean isValid () {
		return true;
	}
	@Override
	public Focusable getAutoFocusField () {
		return txtPost;
	}

}
