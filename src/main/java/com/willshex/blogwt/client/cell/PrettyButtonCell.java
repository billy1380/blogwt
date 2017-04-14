//
//  PrettyButtonCell.java
//  com.willshex.loadtest
//
//  Created by William Shakour (billy1380) on 20 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.cell;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PrettyButtonCell extends ButtonCell {

	@Override
	public void render (Context context, SafeHtml data, SafeHtmlBuilder sb) {
		String buttonStyle = null, glyph = null;

		if (data != null) {
			switch (data.asString()) {
			case "re-run":
				glyph = "glyphicon-repeat";
				buttonStyle = "btn-default";
				break;
			case "run":
				glyph = "glyphicon-play";
				buttonStyle = "btn-primary";
				break;
			case "resume":
				buttonStyle = "btn-default";
				glyph = "glyphicon-play";
				break;
			case "pause":
				buttonStyle = "btn-default";
				glyph = "glyphicon-pause";
				break;
			case "delete":
				buttonStyle = "btn-danger";
				glyph = "glyphicon-trash";
				break;
			case "suspend":
				buttonStyle = "btn-danger";
				glyph = "glyphicon-ban-circle";
				break;
			case "unsuspend":
				buttonStyle = "btn-success";
				break;
			case "clone":
				buttonStyle = "btn-default";
				glyph = "glyphicon-duplicate";
				break;
			case "edit":
				buttonStyle = "btn-default";
				glyph = "glyphicon-edit";
				break;
			case "admin":
				buttonStyle = "btn-default";
				glyph = "glyphicon-sunglasses";
				break;
			case "user":
				buttonStyle = "btn-default";
				glyph = "glyphicon-user";
				break;
			default:
				buttonStyle = "btn-default";
				break;
			}
		}

		sb.appendHtmlConstant("<button type=\"button\" class=\"btn btn-xs "
				+ (buttonStyle == null || buttonStyle.length() == 0 ? ""
						: buttonStyle)
				+ "\" tabindex=\"-1\">");

		if (glyph != null && glyph.length() > 0) {
			sb.appendHtmlConstant(
					"<span class=\"glyphicon " + glyph + "\"></span> ");
		}

		if (data != null) {
			sb.append(data);
		}
		sb.appendHtmlConstant("</button>");
	}
}
