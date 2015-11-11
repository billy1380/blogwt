//
//  ReCaptchaPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 Nov 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.form;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author billy1380
 *
 */
public class ReCaptchaPart extends Composite {

	private static ReCaptchaPartUiBinder uiBinder = GWT
			.create(ReCaptchaPartUiBinder.class);

	interface ReCaptchaPartUiBinder extends UiBinder<Widget, ReCaptchaPart> {}

	public ReCaptchaPart () {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setApiKey (String value) {
		this.getWidget().getElement().setPropertyString("data-sitekey", value);
	}

	@Override
	protected void onLoad () {
		super.onLoad();

		ScriptInjector.fromUrl("https://www.google.com/recaptcha/api.js")
				.setWindow(ScriptInjector.TOP_WINDOW)
				.setCallback(new Callback<Void, Exception>() {

					@Override
					public void onSuccess (Void result) {}

					@Override
					public void onFailure (Exception reason) {}
				}).inject();
	}
}
