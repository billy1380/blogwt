//
//  UiHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 13 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.helper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author William Shakour (billy1380)
 *
 */
public class UiHelper {
	/**
	 * Swaps a TextBox with an element of the same type for remember password. The text box needs to be within an panel. The styles of the text box are also
	 * copied
	 * 
	 * @param textBox
	 * @param elementId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends TextBox> T swap (T textBox, String elementId) {
		Panel parent = (Panel) textBox.getParent();
		T newTextBox = null;

		if (textBox instanceof PasswordTextBox) {
			newTextBox = (T) PasswordTextBox
					.wrap(DOM.getElementById(elementId));
		} else if (textBox instanceof TextBox) {
			newTextBox = (T) TextBox.wrap(DOM.getElementById(elementId));
		}

		newTextBox.getElement().setAttribute("class",
				textBox.getElement().getAttribute("class"));
		newTextBox.removeFromParent();
		parent.getElement().insertBefore(newTextBox.getElement(),
				textBox.getElement());

		textBox.removeFromParent();

		return newTextBox;
	}

	/**
	 * @param txtCopyrightHolder
	 * @param string
	 */
	public static void addPlaceholder (Widget widget, String placeholder) {
		widget.getElement().setAttribute("placeholder", placeholder);
	}
}
