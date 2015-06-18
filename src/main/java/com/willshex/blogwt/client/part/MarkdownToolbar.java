//
//  MarkdownToolbar.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 17 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author William Shakour (billy1380)
 *
 */
public class MarkdownToolbar extends Composite {

	private static MarkdownToolbarUiBinder uiBinder = GWT
			.create(MarkdownToolbarUiBinder.class);

	interface MarkdownToolbarUiBinder extends UiBinder<Widget, MarkdownToolbar> {}

	private TextBoxBase txtContent;

	@UiField PushButton btnHeading;
	@UiField PushButton btnBold;
	@UiField PushButton btnItalic;
	@UiField PushButton btnSuperscript;
	@UiField PushButton btnStrikethrough;
	@UiField PushButton btnFixed;

	@UiField PushButton btnQuote;
	@UiField PushButton btnCode;

	@UiField PushButton btnHr;
	@UiField PushButton btnOl;
	@UiField PushButton btnUl;

	@UiField PushButton btnInsertImage;
	@UiField PushButton btnInsertLink;

	@UiField PushButton btnRemoveFormat;

	public MarkdownToolbar () {
		initWidget(uiBinder.createAndBindUi(this));

		btnHeading.getElement().setInnerHTML(
				"<span class=\"glyphicon glyphicon-text-size\"></span>");
		btnBold.getElement().setInnerHTML(
				"<span class=\"glyphicon glyphicon-bold\"></span>");
		btnItalic.getElement().setInnerHTML(
				"<span class=\"glyphicon glyphicon-italic\"></span>");
		btnStrikethrough.getElement().setInnerHTML(
				"<span class=\"\"></span> strike");
		btnSuperscript.getElement().setInnerHTML(
				"<span class=\"glyphicon glyphicon-superscript\"></span>");
		btnFixed.getElement().setInnerHTML("<span class=\"\">fixed</span>");

		btnQuote.getElement().setInnerHTML("<span class=\"\"></span> quote");
		btnCode.getElement().setInnerHTML(
				"<span class=\"glyphicon glyphicon-console\"></span>");

		btnHr.getElement().setInnerHTML(
				"<span class=\"glyphicon glyphicon-minus\"></span>");

		btnOl.getElement().setInnerHTML("<span class=\"\"></span> numbered");
		btnUl.getElement().setInnerHTML(
				"<span class=\"glyphicon glyphicon-list\"></span>");

		btnInsertImage.getElement().setInnerHTML(
				"<span class=\"glyphicon glyphicon-picture\"></span>");

		btnInsertLink.getElement().setInnerHTML(
				"<span class=\"glyphicon glyphicon-link\"></span>");

		btnRemoveFormat.getElement().setInnerHTML(
				"<span class=\"glyphicon glyphicon-erase\"></span>");

	}

	@UiHandler({ "btnHeading", "btnBold", "btnItalic", "btnStrikethrough",
			"btnSuperscript", "btnFixed", "btnQuote", "btnCode", "btnHr",
			"btnOl", "btnUl", "btnInsertImage", "btnInsertLink",
			"btnRemoveFormat" })
	void onClick (ClickEvent event) {
		if (txtContent != null) {
			Widget sender = (Widget) event.getSource();

			StringBuffer text = new StringBuffer(txtContent.getText());
			String selection = txtContent.getSelectedText();
			int pos = txtContent.getCursorPos(), length = selection.length();

			if (sender == btnHeading) {
				text.insert(pos, "#");
				txtContent.setText(text.toString());
				txtContent.setCursorPos(pos);
			} else if (sender == btnBold) {
				text.insert(pos + length, "__");
				text.insert(pos, "__");
				txtContent.setText(text.toString());
				txtContent.setCursorPos(pos);
			} else if (sender == btnItalic) {
				text.insert(pos + length, "*");
				text.insert(pos, "*");
				txtContent.setText(text.toString());
				txtContent.setCursorPos(pos);
			} else if (sender == btnStrikethrough) {
				text.insert(pos + length, "~~");
				text.insert(pos, "~~");
				txtContent.setText(text.toString());
				txtContent.setCursorPos(pos);
			} else if (sender == btnSuperscript) {
				text.insert(pos + length, "^");
				text.insert(pos, "^");
				txtContent.setText(text.toString());
				txtContent.setCursorPos(pos);
			} else if (sender == btnFixed) {
				text.insert(pos + length, "`");
				text.insert(pos, "`");
				txtContent.setText(text.toString());
				txtContent.setCursorPos(pos);
			} else if (sender == btnCode) {
				String language = Window.prompt("Enter code language:", "");
				if (language != null) {
					text.insert(pos + length, "\n```\n");
					text.insert(pos, "\n```" + language + "\n");
					txtContent.setText(text.toString());
					txtContent.setCursorPos(pos);
				}
			} else if (sender == btnQuote) {
				text.insert(pos, "\n>");
				txtContent.setText(text.toString());
				txtContent.setCursorPos(pos);
			} else if (sender == btnHr) {
				text.insert(pos, "\n\n---\n\n");
				txtContent.setText(text.toString());
				txtContent.setCursorPos(pos);
			} else if (sender == btnUl) {
				text.insert(pos, "- ");
				txtContent.setText(text.toString());
				txtContent.setCursorPos(pos);
			} else if (sender == btnOl) {
				text.insert(pos, "1. ");
				txtContent.setText(text.toString());
				txtContent.setCursorPos(pos);
			} else if (sender == btnRemoveFormat) {
				if (length > 0) {
					selection = selection.replace("*", "").replace("~", "")
							.replace("_", "").replace("^", "").replace("`", "")
							.replace("-", "").replace("#", "");
					text.replace(pos, pos + length, selection);
					txtContent.setText(text.toString());
					txtContent.setCursorPos(pos);
				}
			} else if (sender == btnInsertImage) {
				String url = Window.prompt("Enter an image URL:", "http://");
				if (url != null) {
					if (length > 0) {
						text.insert(pos + length, "](" + url + " \""
								+ selection + "\")");
						text.insert(pos, "![");
					} else {
						text.insert(pos, "![" + url + "](" + url + " \"\")");
					}
					txtContent.setText(text.toString());
					txtContent.setCursorPos(pos);
				}
			} else if (sender == btnInsertLink) {
				String url = Window.prompt("Enter a link URL:", "http://");
				if (url != null) {
					if (length > 0) {
						text.insert(pos + length, "](" + url + " \""
								+ selection + "\")");
						text.insert(pos, "[");
					} else {
						text.insert(pos, "[" + url + "](" + url + " \"\")");
					}
					txtContent.setText(text.toString());
					txtContent.setCursorPos(pos);
				}
			}
		}
	}

	public void setText (TextBoxBase text) {
		this.txtContent = text;
	}

}
