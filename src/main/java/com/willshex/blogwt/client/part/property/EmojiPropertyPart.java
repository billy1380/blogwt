//
//  EmojiPropertyPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 31 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.property;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.shared.helper.PropertyHelper;

import emoji.gwt.emoji.Emoji;
import emoji.gwt.emoji.res.Apple;
import emoji.gwt.emoji.res.Emojis;
import emoji.gwt.emoji.res.Noto;
import emoji.gwt.emoji.res.Twemoji;

/**
 * @author billy1380
 *
 */
public class EmojiPropertyPart extends AbstractPropertyPart {

	@UiField Element elDescription;
	@UiField Element elName;
	@UiField RadioButton rdoNone;
	@UiField RadioButton rdoApple;
	@UiField RadioButton rdoGoogleNoto;
	@UiField RadioButton rdoTwitter;

	private static EmojiPropertyPartUiBinder uiBinder = GWT
			.create(EmojiPropertyPartUiBinder.class);

	interface EmojiPropertyPartUiBinder
			extends UiBinder<Widget, EmojiPropertyPart> {}

	public EmojiPropertyPart () {
		initWidget(uiBinder.createAndBindUi(this));

		appendAll(rdoApple.getElement().getChild(1), Apple.INSTANCE);
		appendAll(rdoGoogleNoto.getElement().getChild(1), Noto.INSTANCE);
		appendAll(rdoTwitter.getElement().getChild(1), Twemoji.INSTANCE);

	}

	/**
	 * @param appleLabel
	 * @param instance
	 */
	private void appendAll (Node node, Emojis theme) {
		append(node, theme, ":poultry_leg:", ":eggplant:", ":birthday:",
				":moneybag:");
	}

	/**
	 * @param instance
	 * @param string
	 */
	private void append (final Node node, final Emojis theme,
			final String... names) {
		new Emoji().setTheme(theme, new Emoji.Ready() {
			public void ready (Emoji emoji) {
				for (String name : names) {
					Image one = new Image(emoji.resource(name));
					one.setHeight("32px");
					one.setWidth("32px");
					node.appendChild(one.getElement());
				}
			}
		});
	}

	@UiHandler({ "rdoNone", "rdoApple", "rdoGoogleNoto", "rdoTwitter" })
	void onSelectionValueChanged (ValueChangeEvent<Boolean> vce) {
		if (Boolean.TRUE.equals(vce.getValue())) {
			String value = PropertyHelper.NONE_VALUE;

			if (rdoApple == vce.getSource()) {
				value = PropertyHelper.APPLE_VALUE;
			} else if (rdoGoogleNoto == vce.getSource()) {
				value = PropertyHelper.NOTO_VALUE;
			} else if (rdoTwitter == vce.getSource()) {
				value = PropertyHelper.TWITTER_VALUE;
			}

			setValue(value, true);
		}
	}

	@Override
	public void setValue (String value, boolean fireEvents) {
		if (value == null) {
			value = PropertyHelper.NOTO_VALUE;
		}

		String oldValue = getValue();

		switch (value) {
		case PropertyHelper.NONE_VALUE:
			rdoNone.setValue(Boolean.TRUE, true);
			break;
		case PropertyHelper.APPLE_VALUE:
			rdoApple.setValue(Boolean.TRUE, true);
			break;
		case PropertyHelper.NOTO_VALUE:
			rdoGoogleNoto.setValue(Boolean.TRUE, true);
			break;
		case PropertyHelper.TWITTER_VALUE:
			rdoTwitter.setValue(Boolean.TRUE, true);
			break;
		}

		this.value = value;

		if (value.equals(oldValue)) { return; }
		if (fireEvents) {
			ValueChangeEvent.fire(this, value);
		}
	}

	@Override
	public void setName (String name) {
		elName.setInnerText(name);
	}

	@Override
	public String getName () {
		return elName.getInnerText();
	}

	@Override
	public void setDescription (String description) {
		elDescription.setInnerText(description);
	}

}
