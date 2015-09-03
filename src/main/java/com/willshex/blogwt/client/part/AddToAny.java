//
//  AddToAny.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 12 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.blogwt.shared.helper.TagHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class AddToAny extends Composite {

	private static AddToAnyUiBinder uiBinder = GWT
			.create(AddToAnyUiBinder.class);

	interface AddToAnyUiBinder extends UiBinder<Widget, AddToAny> {}

	@UiField AnchorElement lnkAddToAny;
	@UiField Element elAddToAny;
	private ScriptElement elAddToAnyScript;
	private String title;
	private String url;

	public AddToAny () {
		initWidget(uiBinder.createAndBindUi(this));

		String share = PropertyController.get().stringProperty(
				PropertyHelper.POST_SHARE_ENABLED);
		if (!PropertyHelper.NONE_VALUE.equals(share)) {
			List<String> shareWith = TagHelper.convertToTagList(share);

			AnchorElement anchor;
			Document doc = Document.get();
			for (String service : shareWith) {
				anchor = doc.createAnchorElement();
				anchor.setClassName("a2a_button_" + service);
				elAddToAny.appendChild(anchor);
			}
		}
	}

	/**
	 * @param url
	 */
	public void setUrl (String url) {
		this.url = url;
		refresh();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.UIObject#setTitle(java.lang.String) */
	@Override
	public void setTitle (String title) {
		this.title = title;
		refresh();
	}

	public void refresh () {
		configureAddToAny(url, title);
		installAddToAny(url, title);
	}

	private static native void configureAddToAny (String url, String title) /*-{
																			$wnd.a2a_config = $wnd.a2a_config || {};
																			$wnd.a2a_config.linkname = title;
																			$wnd.a2a_config.linkurl = url;
																			}-*/;

	private void installAddToAny (String url, String title) {
		removeAddToAny();

		lnkAddToAny.setHref("https://www.addtoany.com/share_save?linkurl="
				+ url + "&linkname=" + title + "");

		elAddToAnyScript = Document.get().createScriptElement();
		elAddToAnyScript.setType("text/javascript");
		elAddToAnyScript.setSrc("//static.addtoany.com/menu/page.js");
		Document.get().getBody().appendChild(elAddToAnyScript);
	}

	private void removeAddToAny () {
		if (elAddToAnyScript != null && elAddToAnyScript.hasParentElement()) {
			elAddToAnyScript.removeFromParent();
		}
	}

}
