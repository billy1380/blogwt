//
//  ShortDescriptionPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 16 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.helper.PostHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ShortDescriptionPart extends Composite {

	private static ShortDescriptionPartUiBinder uiBinder = GWT
			.create(ShortDescriptionPartUiBinder.class);

	interface ShortDescriptionPartUiBinder extends
			UiBinder<Widget, ShortDescriptionPart> {}

	@UiField Element elDescription;

	public ShortDescriptionPart () {
		initWidget(uiBinder.createAndBindUi(this));

		String description = PropertyController.get().stringProperty(
				PropertyHelper.SHORT_DESCRIPTION);
		if (description == null
				|| PropertyHelper.NONE_VALUE.equals(description)) {
			setVisible(false);
		} else {
			elDescription.setInnerHTML(PostHelper.makeMarkup(description));
		}
	}
}
