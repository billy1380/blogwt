//
//  UiHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 13 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.helper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Function;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.cell.PrettyButtonCell;
import com.willshex.blogwt.client.cell.StyledImageCell;
import com.willshex.blogwt.client.controller.PropertyController;

/**
 * @author William Shakour (billy1380)
 *
 */
public class UiHelper {

	public interface UiHelperTemplates extends SafeHtmlTemplates {

		@Template("<a class=\"btn btn-default {1}\" href=\"{0}\"><span class=\"glyphicon glyphicon-edit\"></span> edit</a>")
		SafeHtml edit (SafeUri u, String size);

		default SafeHtml edit (SafeUri u) {
			return edit(u, "");
		}

		default SafeHtml xsEdit (SafeUri u) {
			return edit(u, "btn-xs");
		}
	}

	public static final String HAS_ERROR_STYLE = "has-error";

	public static UiHelperTemplates TEMPLATES = GWT
			.create(UiHelperTemplates.class);

	public static final ButtonCell ACTION_PROTOTYPE = new PrettyButtonCell();
	public static final Cell<SafeHtml> SAFE_HTML_PROTOTYPE = new SafeHtmlCell();
	public static final StyledImageCell IMAGE_PROTOTYPE = new StyledImageCell(
			20.0, 20.0);

	private static final String ACTIVE = "active";

	static {
		IMAGE_PROTOTYPE.addClassName("img-rounded");
	}

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

	@SafeVarargs
	public static <E extends Enum<E>> void add (ListBox cbo, Class<E> e,
			E... exclude) {
		add(false, cbo, e, exclude);
	}

	@SafeVarargs
	public static <E extends Enum<E>> void add (boolean allowNone, ListBox cbo,
			Class<E> e, E... exclude) {

		HashSet<E> s = new HashSet<>(Arrays.asList(exclude));

		if (allowNone) {
			cbo.addItem("None", "");
		}

		for (E v : e.getEnumConstants()) {
			if (!s.contains(v)) {
				cbo.addItem(v.toString());
			}
		}
	}

	public static <E extends Enum<E>> void select (ListBox cbo, E value) {
		select(cbo, value.toString());
	}

	public static void select (ListBox cbo, String value) {
		int count = cbo.getItemCount();
		for (int i = 0; i < count; i++) {
			if (cbo.getValue(i).equals(value)) {
				cbo.setSelectedIndex(i);
				break;
			}
		}
	}

	/**
	 * @param string
	 * @return
	 */
	public static String pageTitle (String title) {
		return PropertyController.get().title() + ": " + title;
	}

	public static void activateItem (Element element, boolean active) {
		if (element != null) {
			if (active && !element.hasClassName(ACTIVE)) {
				element.addClassName(ACTIVE);
			} else if (!active && element.hasClassName(ACTIVE)) {
				element.removeClassName(ACTIVE);
			}
		}
	}

	public static void activateItem (String page, boolean active,
			Function<String, Element> get) {
		activateItem(get.apply(page), active);
	}

	public static void scaleSvg (Element parent, float scale) {
		Element el = parent.getFirstChildElement();
		String w = el.getAttribute("width"), h = el.getAttribute("height");
		String wu = w.substring(w.length() - 2, w.length()),
				hu = h.substring(h.length() - 2, h.length());
		float wf = Float.parseFloat(w.substring(0, w.length() - 2)),
				hf = Float.parseFloat(h.substring(0, h.length() - 2));

		String nw = Float.toString(wf * scale) + wu,
				nh = Float.toString(hf * scale) + hu;

		el.setAttribute("width", nw);
		el.setAttribute("height", nh);
	}

	public static void insertSvg (Element el, TextResource resource) {
		el.setInnerHTML(resource.getText());
	}

	public static void setSvgFill (Element parent, String colour) {
		parent.getElementsByTagName("path").getItem(0).getStyle()
				.setProperty("fill", colour);
	}
}
