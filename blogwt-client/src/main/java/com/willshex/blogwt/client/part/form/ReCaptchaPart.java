//
//  ReCaptchaPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 Nov 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author billy1380
 *
 */
public class ReCaptchaPart extends Composite implements FormField {

	private static ReCaptchaPartUiBinder uiBinder = GWT
			.create(ReCaptchaPartUiBinder.class);

	interface ReCaptchaPartUiBinder extends UiBinder<Widget, ReCaptchaPart> {}

	private static ScriptElement SCRIPT = null;
	private String key;
	private String id = null;
	private String error;

	public ReCaptchaPart () {
		initWidget(uiBinder.createAndBindUi(this));
		getWidget().getElement().setId(HTMLPanel.createUniqueId());
	}

	public void setApiKey (String value) {
		key = value;
	}

	@Override
	protected void onLoad () {
		super.onLoad();
		if (SCRIPT == null) {
			exposeMethod(this);
			SCRIPT = ScriptInjector.fromUrl(
					"https://www.google.com/recaptcha/api.js?onload=call_65fd7992_9064_11e5_aeca_fbe3616fc7e4&render=explicit")
					.setWindow(ScriptInjector.TOP_WINDOW).inject().cast();
		} else {
			render();
		}
	}

	private native void exposeMethod (ReCaptchaPart widget) /*-{
	$wnd.call_65fd7992_9064_11e5_aeca_fbe3616fc7e4 = function() {
	    widget.@com.willshex.blogwt.client.part.form.ReCaptchaPart::render()();
	};
	}-*/;

	private void render () {
		if (SCRIPT != null && id == null) {
			id = render(getWidget().getElement(), key);
		}
	}

	private native String render (Object element, String key) /*-{
	return $wnd.grecaptcha.render(element, {
	    'sitekey' : key
	});
	}-*/;

	public void reset () {
		if (SCRIPT != null && id != null) {
			reset(id);
		}
	}

	public native void reset (String id) /*-{
	$wnd.grecaptcha.reset(id);
	}-*/;
	@Override
	public boolean isValid () {
		boolean valid = true;
		if (value().length() < 1) {
			error = "Please verify that you are not a robot";
			valid = false;
		}

		return valid;
	}
	@Override
	public void showError () {}
	@Override
	public void hideError () {
		error = null;
	}
	@Override
	public String value () {
		TextAreaElement valueHolder = getWidget().getElement().getFirstChild()
				.getFirstChild().getNextSibling().<TextAreaElement> cast();
		return valueHolder.getValue();
	}
	@Override
	public String name () {
		return null;
	}
}
