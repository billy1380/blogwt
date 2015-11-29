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
	private String id;

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
			SCRIPT = ScriptInjector
					.fromUrl(
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

	public String getValue () {
		TextAreaElement valueHolder = getWidget().getElement().getFirstChild()
				.getFirstChild().getNextSibling().<TextAreaElement> cast();
		return valueHolder.getValue();
	}

	private void render () {
		id = render(getWidget().getElement(), key);
	}

	private native String render (Object element, String key) /*-{
																return $wnd.grecaptcha.render(element, {'sitekey' : key}) ;
																}-*/;

	public void reset () {
		reset(id);
	}

	private void remove () {
		remove(id);
	}

	private native void remove (String id) /*-{
											$wnd.grecaptcha.remove(id);
											}-*/;

	public native void reset (String id) /*-{
																				$wnd.grecaptcha.reset(id);
																				}-*/;

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.part.form.FormField#isValid() */
	@Override
	public boolean isValid () {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.part.form.FormField#showError() */
	@Override
	public void showError () {
		// TODO Auto-generated method stub

	}
}
