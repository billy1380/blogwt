//
//  CommentPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 20 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.property;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.part.Resources;

/**
 * @author William Shakour (billy1380)
 *
 */
public class CommentPart extends Composite {

	@UiField Element elDescription;
	@UiField Element elName;
	@UiField RadioButton rdoNone;
	@UiField RadioButton rdoDisqus;

	private static final String NONE_VALUE = "none";
	private static final String DISQUS_VALUE = "disqus";

	private static CommentPartUiBinder uiBinder = GWT
			.create(CommentPartUiBinder.class);

	interface CommentPartUiBinder extends UiBinder<Widget, CommentPart> {}

	public CommentPart () {
		initWidget(uiBinder.createAndBindUi(this));

		Image disqus = new Image(Resources.RES.disqusLogo());
		disqus.setHeight("20px");
		disqus.setWidth("110px");

		// get the label
		rdoDisqus.getElement().getChild(1).appendChild(disqus.getElement());
	}

	/**
	 * @param description
	 */
	public void setDescription (String description) {
		elDescription.setInnerText(description);
	}

	/**
	 * @param name
	 */
	public void setName (String name) {
		elName.setInnerText(name);
	}

	public void setValue (String value) {
		if (value == null) {
			value = NONE_VALUE;
		}

		switch (value) {
		case NONE_VALUE:
			rdoNone.setValue(Boolean.TRUE, true);
			break;
		case DISQUS_VALUE:
			rdoDisqus.setValue(Boolean.TRUE, true);
			break;
		}
	}

	public String getValue () {
		return rdoNone.getValue().booleanValue() ? NONE_VALUE : DISQUS_VALUE;
	}
}
