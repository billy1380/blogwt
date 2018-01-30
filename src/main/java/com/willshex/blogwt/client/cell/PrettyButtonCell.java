//
//  PrettyButtonCell.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 20 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.cell;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PrettyButtonCell extends ButtonCell {

	public static class Button {
		public String name;
		public String glyph;
		public String style;

		public Button (String name, String style, String glyph) {
			this.name = name;
			this.glyph = glyph;
			this.style = style;
		}
	}

	private Map<String, Button> buttons = new HashMap<>();

	public PrettyButtonCell () {
		addButton("re-run", "btn-default", "glyphicon-repeat");
		addButton("run", "btn-primary", "glyphicon-play");
		addButton("resume", "btn-default", "glyphicon-play");
		addButton("pause", "btn-default", "glyphicon-pause");
		addButton("delete", "btn-danger", "glyphicon-trash");
		addButton("suspend", "btn-danger", "glyphicon-ban-circle");
		addButton("unsuspend", "btn-success", null);
		addButton("clone", "btn-default", "glyphicon-duplicate");
		addButton("edit", "btn-default", "glyphicon-edit");
		addButton("admin", "btn-default", "glyphicon-certificate");
		addButton("user", "btn-default", "glyphicon-user");
	}

	/**
	 * @param name
	 * @param glyph
	 * @param style
	 */
	private void addButton (String name, String glyph, String style) {
		Button button = new Button(name, glyph, style);
		buttons.put(name, button);
	}

	public PrettyButtonCell (Button... buttons) {
		this();
		for (Button button : buttons) {
			this.buttons.put(button.name, button);
		}
	}

	@Override
	public void render (Context context, SafeHtml data, SafeHtmlBuilder sb) {
		if (data != null) {
			String buttonStyle = null, glyph = null;

			Button b = buttons.get(data.asString());

			if (b != null) {
				buttonStyle = b.style;
				glyph = b.glyph;
			} else {
				buttonStyle = "btn-default";
			}

			sb.appendHtmlConstant(
					"<button type=\"button\" class=\"btn btn-xs "
							+ (buttonStyle == null || buttonStyle.length() == 0
									? "" : buttonStyle)
							+ "\" tabindex=\"-1\">");

			if (glyph != null && glyph.length() > 0) {
				sb.appendHtmlConstant(
						"<span class=\"glyphicon " + glyph + "\"></span> ");
			}

			sb.append(data);
			sb.appendHtmlConstant("</button>");
		}
	}
}