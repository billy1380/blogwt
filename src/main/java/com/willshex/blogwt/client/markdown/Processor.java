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

	/* (non-Javadoc)
	 * 
	 * @see org.markdown4j.client.MarkdownProcessor#emojiEmitter() */
	@Override
	protected EmojiEmitter emojiEmitter () {
		return new EmojiEmitter() {

			@Override
			public void emitEmoji (StringBuilder out, String name,
					Decorator decorator) {
				SafeUri safeLink = emoji.gwt.emoji.Emoji.get().safeUri(name);
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
		registerPlugins(new WebSequencePlugin(ensureManager()), includePlugin);
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
			public void openImage (StringBuilder out, String link, String title) {
				super.openImage(out, link, title);
				out.append(" class=\"" + Resources.RES.styles().image() + "\" ");
			}

			/* (non-Javadoc)
			 * 
			 * @see
			 * org.markdown4j.ExtDecorator#openLink(java.lang.StringBuilder,
			 * java.lang.String, java.lang.String) */
			@Override
			public void openLink (StringBuilder out, String link, String title) {
				super.openLink(out, link, title);

				if (link.startsWith(HOST_START)
						|| link.startsWith(HTTP_HOST_START)
						|| link.startsWith(HTTPS_HOST_START)
						|| !(link.startsWith(ANY) || link.startsWith(HTTP) || link
								.startsWith(HTTPS))) {
					out.append(" class=\""
							+ Resources.RES.styles().internalLink() + "\" ");
				} else {
					out.append(" class=\""
							+ Resources.RES.styles().externalLink()
							+ "\" target=\"_blank\" ");
				}
			}
		});
		decorator.addStyleClass("text-justify", "p");
	}
}
