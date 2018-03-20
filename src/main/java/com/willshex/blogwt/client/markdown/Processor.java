//
//  Processor.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 12 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.markdown;

import static com.google.gwt.user.client.Window.Location.getHost;

import java.util.HashMap;
import java.util.Map;

import org.markdown4j.ExtDecorator;
import org.markdown4j.client.MarkdownProcessor;
import org.markdown4j.client.WebSequencePlugin;
import org.markdown4j.client.shared.DefaultEmojiEmitter;

import com.github.rjeschke.txtmark.Decorator;
import com.github.rjeschke.txtmark.EmojiEmitter;
import com.github.rjeschke.txtmark.MarkdownUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.markdown.plugin.CachedIncludePlugin;
import com.willshex.blogwt.client.markdown.plugin.FormPlugin;
import com.willshex.blogwt.client.markdown.plugin.GalleryPlugin;
import com.willshex.blogwt.client.markdown.plugin.MapPlugin;
import com.willshex.blogwt.client.markdown.plugin.PostsPlugin;
import com.willshex.blogwt.client.markdown.plugin.YoutubePlugin;
import com.willshex.blogwt.shared.helper.PropertyHelper;

import emoji.gwt.emoji.Emoji;
import emoji.gwt.emoji.Emoji.Ready;

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

	private static final String OPEN = "<md>", CLOSE = "</md>";
	private static final int OPEN_LENGTH = OPEN.length(),
			CLOSE_LENGTH = CLOSE.length();

	/* (non-Javadoc)
	 * 
	 * @see org.markdown4j.client.MarkdownProcessor#emojiEmitter() */
	@Override
	protected EmojiEmitter emojiEmitter () {
		String enableEmoji = PropertyController.get()
				.stringProperty(PropertyHelper.POST_ENABLE_EMOJI);
		return !PropertyHelper.NONE_VALUE.equals(enableEmoji)
				&& Emoji.get() != null ? Processor::emitEmoji
						: Processor::platformEmitEmoji;
	}

	static void platformEmitEmoji (final StringBuilder out, final String name,
			Decorator decorator) {
		out.append(DefaultEmojiEmitter.html(name));
	}

	static void emitEmoji (final StringBuilder out, final String name,
			Decorator decorator) {
		String enableEmoji = PropertyController.get()
				.stringProperty(PropertyHelper.POST_ENABLE_EMOJI);

		if (!PropertyHelper.NONE_VALUE.equals(enableEmoji)
				&& Emoji.get() != null) {
			SafeUri safeLink = Emoji.get().safeUri(name);
			String link;
			String comment;
			if (safeLink != null
					&& (link = safeLink.asString()).length() != 0) {
				comment = name + " emoji";

				out.append("<img class=\"" + Resources.RES.styles().emoji()
						+ "\" src=\"");
				MarkdownUtils.appendValue(out, link, 0, link.length());
				out.append("\" alt=\"");
				MarkdownUtils.appendValue(out, name, 0, name.length());
				out.append('"');
				if (comment != null) {
					out.append(" title=\"");
					MarkdownUtils.appendValue(out, comment, 0,
							comment.length());
					out.append('"');
				}
				out.append(" />");
			}
		}
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
				new GalleryPlugin(ensureManager()),
				mapsApiKey == null ? null
						: new MapPlugin(mapsApiKey, ensureManager()),
				new YoutubePlugin(), new FormPlugin(ensureManager()),
				new PostsPlugin(ensureManager()));
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
									+ "\" target=\"_blank\" rel=\"noopener\" ");
				}
			}
		});
		decorator.addStyleClass("text-justify", "p");
	}

	/* (non-Javadoc)
	 * 
	 * @see org.markdown4j.client.MarkdownProcessor#process(java.lang.String) */
	@Override
	public String process (String markdown) {
		StringBuffer b = new StringBuffer(markdown);

		Map<String, String> replaced = new HashMap<>();

		String id;

		int start = 0, end;
		while ((start = b.indexOf(OPEN, start)) > 0) {
			if ((end = b.indexOf(CLOSE, start)) > 0) {
				id = HTMLPanel.createUniqueId();
				replaced.put(id, b.substring(start + OPEN_LENGTH, end));
				b.replace(start, end + CLOSE_LENGTH, marker(id));
			}
		}

		String processed = super.process(b.toString());

		if (replaced.size() > 0) {
			b.setLength(0);
			b.append(processed);

			String match, content;
			int matchLength;
			start = 0;
			for (String key : replaced.keySet()) {
				match = marker(key);
				matchLength = match.length();

				while ((start = b.indexOf(match, start)) > 0) {
					b.replace(start, start + matchLength,
							content = process(replaced.get(key)));
					start += content.length();
				}
			}

			processed = b.toString();
		}

		return processed;
	}

	/**
	 * @param id
	 * @return
	 */
	private String marker (String id) {
		return "<span id=\"md:" + id + "\">" + id + "</span>";
	}

	/**
	 * @param ready
	 */
	public static void init (Ready ready) {
		String enableEmoji = PropertyController.get()
				.stringProperty(PropertyHelper.POST_ENABLE_EMOJI);
		if (!PropertyHelper.NONE_VALUE.equals(enableEmoji)) {
			Emoji.get(enableEmoji, ready);
		} else {
			ready.ready(Emoji.get());
		}
	}
}
