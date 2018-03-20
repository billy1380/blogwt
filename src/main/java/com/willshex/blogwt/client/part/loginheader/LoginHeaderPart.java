//
//  LoginHeaderPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 20 Mar 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.loginheader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.shared.helper.PropertyHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class LoginHeaderPart extends Composite {

	private static LoginHeaderPartUiBinder uiBinder = GWT
			.create(LoginHeaderPartUiBinder.class);

	interface LoginHeaderPartUiBinder
			extends UiBinder<Widget, LoginHeaderPart> {}

	private static LoginHeaderPart one = null;

	public static LoginHeaderPart get () {
		if (one == null) {
			one = new LoginHeaderPart();
		}

		return one;
	}

	@UiField Element elTitle;
	@UiField Element elExtendedTitle;
	@UiField Image imgLargeBrand;

	public LoginHeaderPart () {
		initWidget(uiBinder.createAndBindUi(this));

		elTitle.setInnerText(PropertyController.get().title());
		elExtendedTitle.setInnerText(PropertyController.get().extendedTitle());
		imgLargeBrand.setAltText(PropertyController.get().title());

		String largeLogo = PropertyController.get()
				.stringProperty(PropertyHelper.LARGE_LOGO_URL);
		if (largeLogo != null && !PropertyHelper.NONE_VALUE.equals(largeLogo)) {
			imgLargeBrand.getElement().removeAttribute("width");
			imgLargeBrand.getElement().removeAttribute("height");
			imgLargeBrand.addStyleName("img-responsive");
			imgLargeBrand.addStyleName("center-block");
			imgLargeBrand.setUrl(largeLogo);
		} else {
			imgLargeBrand.setResource(Resources.RES.largeBrand());
		}
	}

}
