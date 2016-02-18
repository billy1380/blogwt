//
//  Processor.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 12 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.markdown;

import static com.google.gwt.user.client.Window.Location.getHost;

import org.markdown4j.ExtDecorator;
import org.markdown4j.client.MarkdownProcessor;
import org.markdown4j.client.WebSequencePlugin;

import com.github.rjeschke.txtmark.Decorator;
import com.github.rjeschke.txtmark.EmojiEmitter;
import com.github.rjeschke.txtmark.MarkdownUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.markdown.plugin.CachedIncludePlugin;
import com.willshex.blogwt.client.markdown.plugin.FormPlugin;
import com.willshex.blogwt.client.markdown.plugin.GalleryPlugin;
import com.willshex.blogwt.client.markdown.plugin.MapPlugin;
import com.willshex.blogwt.client.markdown.plugin.YoutubePlugin;
import com.willshex.blogwt.shared.helper.PropertyHelper;

import emoji.gwt.emoji.Emoji;
import emoji.gwt.emoji.res.Apple;
import emoji.gwt.emoji.res.Emojis;
import emoji.gwt.emoji.res.Noto;
import emoji.gwt.emoji.res.Twemoji;

/**
 * @author William Shakour (billy1380)
 *
 */
public class Processor extends MarkdownProcessor {

	private static final String ANY = "//";
	private static final String HTTP = "http:";
	private static final String HTTPS = "https:";

	private static final String HOST_START = ANY + getHost();
	private static final String HTTP_HOST_START = HTTP + HOST_START;
	private static final String HTTPS_HOST_START = HTTPS + HOST_START;

	private static String enableEmoji = PropertyController.get()
			.stringProperty(PropertyHelper.POST_ENABLE_EMOJI);
	private static Emoji emoji;

	static {
		if (!PropertyHelper.NONE_VALUE.equals(enableEmoji)) {
			emoji = Emoji.get(theme(enableEmoji), null);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see org.markdown4j.client.MarkdownProcessor#emojiEmitter() */
	@Override
	protected EmojiEmitter emojiEmitter () {
		return new EmojiEmitter() {

			@Override
			public void emitEmoji (final StringBuilder out, final String name,
					Decorator decorator) {

				if (!PropertyHelper.NONE_VALUE.equals(enableEmoji)) {
					Emoji.Ready ready = new Emoji.Ready() {

						@Override
						public void ready (Emoji emoji) {
							boolean addedImage = false;

							SafeUri safeLink = emoji.safeUri(name);
							String link;
							String comment;
							if (safeLink != null && (link = safeLink.asString())
									.length() != 0) {
								comment = name + " emoji";

								out.append("<img class=\""
										+ Resources.RES.styles().emoji()
										+ "\" src=\"");
								MarkdownUtils.appendValue(out, link, 0,
										link.length());
								out.append("\" alt=\"");
								MarkdownUtils.appendValue(out, name, 0,
										name.length());
								out.append('"');
								if (comment != null) {
									out.append(" title=\"");
									MarkdownUtils.appendValue(out, comment, 0,
											comment.length());
									out.append('"');
								}
								out.append(" />");

								addedImage = true;
							}

							if (!addedImage) {
								out.append(name);
							}
						}
					};

					emoji.setTheme(theme(enableEmoji), ready);

				} else {
					out.append(name);
				}

			}
		};
	}

	/* (non-Javadoc)
	 * 
	 * @see org.markdown4j.client.MarkdownProcessor#registerPlugins() */
	@Override
	protected void registerPlugins () {
		CachedIncludePlugin includePlugin = new CachedIncludePlugin(
				ensureManager());
		includePlugin.setProcessor(this);

		String mapsApiKey = PropertyController.get()
				.stringProperty(PropertyHelper.MARKDOWN_MAPS_API_KEY);

		if (mapsApiKey != null && mapsApiKey.trim().length() == 0) {
			mapsApiKey = null;
		}

		registerPlugins(new WebSequencePlugin(ensureManager()), includePlugin,
				new GalleryPlugin(),
				mapsApiKey == null ? null
						: new MapPlugin(mapsApiKey, ensureManager()),
				new YoutubePlugin(), new FormPlugin(ensureManager()));
	}

	public Processor () {
		super();

		ExtDecorator decorator;
		setDecorator(decorator = new ExtDecorator() {

			/* (non-Javadoc)
			 * 
			 * @see
			 * org.markdown4j.ExtDecorator#openImage(java.lang.StringBuilder,
			 * java.lang.String, java.lang.String) */
			@Override
			public void openImage (StringBuilder out, String link,
					String title) {
				super.openImage(out, link, title);
				out.append(" class=\"img-responsive "
						+ Resources.RES.styles().image() + "\" ");
			}

			/* (non-Javadoc)
			 * 
			 * @see
			 * org.markdown4j.ExtDecorator#openLink(java.lang.StringBuilder,
			 * java.lang.String, java.lang.String) */
			@Override
			public void openLink (StringBuilder out, String link,
					String title) {
				super.openLink(out, link, title);

				if (link.startsWith(HOST_START)
						|| link.startsWith(HTTP_HOST_START)
						|| link.startsWith(HTTPS_HOST_START)
						|| !(link.startsWith(ANY) || link.startsWith(HTTP)
								|| link.startsWith(HTTPS))) {
					out.append(" class=\""
							+ Resources.RES.styles().internalLink() + "\" ");
				} else {
					out.append(
							" class=\"" + Resources.RES.styles().externalLink()
									+ "\" target=\"_blank\" ");
				}
			}
		});
		decorator.addStyleClass("text-justify", "p");
	}

	private static Emojis theme (String enableEmoji) {
		Emojis theme = Noto.INSTANCE;

		if (enableEmoji != null) {
			switch (enableEmoji) {
			case PropertyHelper.APPLE_VALUE:
				theme = Apple.INSTANCE;
				break;
			case PropertyHelper.TWITTER_VALUE:
				theme = Twemoji.INSTANCE;
				break;
			}
		}

		return theme;
	}
}
