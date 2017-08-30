//
//  UiHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 13 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.helper;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author William Shakour (billy1380)
 *
 */
public class UiHelper {
	public static final String HAS_ERROR_STYLE = "has-error";

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

	public static void autoFocus (Widget widget) {
		widget.getElement().setAttribute("autofocus", "");
	}

	public static void setVisible (Element e, boolean visible) {
		if (visible) {
			e.getStyle().clearDisplay();
		} else {
			e.getStyle().setDisplay(Display.NONE);
		}
	}

	public static void showError (HTMLPanel pnl, HTMLPanel note, String error) {
		pnl.addStyleName(HAS_ERROR_STYLE);
		note.getElement().setInnerHTML(error);
		note.setVisible(true);
	}

	public static void removeError (HTMLPanel pnl, HTMLPanel note) {
		pnl.removeStyleName(HAS_ERROR_STYLE);
		note.setVisible(false);
	}

	public static <E extends Enum<E>> void add (ListBox cboPosition,
			Class<E> e) {

		for (E v : e.getEnumConstants()) {
			cboPosition.addItem(v.toString());
		}
	}

	public static <E extends Enum<E>> void select (ListBox cbo, E value) {
		int count = cbo.getItemCount();
		for (int i = 0; i < count; i++) {
			if (cbo.getItemText(i).equals(value.toString())) {
				cbo.setSelectedIndex(i);
				break;
			}
		}
	}
}
