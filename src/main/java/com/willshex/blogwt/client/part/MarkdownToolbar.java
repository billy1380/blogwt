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
	@UiField PushButton btnUnderline;
	@UiField PushButton btnSubscript;
	@UiField PushButton btnSuperscript;
	@UiField PushButton btnStrikethrough;
	@UiField PushButton btnFixed;

	@UiField PushButton btnIndent;
	@UiField PushButton btnOutdent;
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
		btnUnderline.getElement().setInnerHTML(
				"<span class=\"icon-underline\"></span> underline");
		btnStrikethrough.getElement().setInnerHTML(
				"<span class=\"\"></span> strike");
		btnSuperscript.getElement().setInnerHTML(
				"<span class=\"glyphicon glyphicon-superscript\"></span>");
		btnSubscript.getElement().setInnerHTML(
				"<span class=\"glyphicon glyphicon-subscript\"></span>");
		btnFixed.getElement().setInnerHTML("<span class=\"\">fixed</span>");

		btnIndent.getElement().setInnerHTML(
				"<span class=\"glyphicon glyphicon-indent-left\"></span>");
		btnOutdent.getElement().setInnerHTML(
				"<span class=\"glyphicon glyphicon-indent-right\"></span>");
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

	@UiHandler({ "btnHeading", "btnBold", "btnItalic", "btnUnderline",
			"btnStrikethrough", "btnSuperscript", "btnSubscript", "btnFixed",
			"btnIndent", "btnOutdent", "btnQuote", "btnCode", "btnHr", "btnOl",
			"btnUl", "btnInsertImage", "btnInsertLink", "btnRemoveFormat" })
	void onClick (ClickEvent event) {
		if (txtContent != null) {
			Widget sender = (Widget) event.getSource();

			StringBuffer text = new StringBuffer(txtContent.getText());
			int pos = txtContent.getCursorPos(), length = txtContent.getSelectionLength();
			
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
			}
		}
	}

	public void setText (TextBoxBase text) {
		this.txtContent = text;
	}

}
