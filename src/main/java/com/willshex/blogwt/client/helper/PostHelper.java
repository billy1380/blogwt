//
//  PostHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 30 May 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.client.helper;

import org.markdown4j.ExtDecorator;
import org.markdown4j.client.MarkdownProcessor;

import com.github.rjeschke.txtmark.Decorator;
import com.github.rjeschke.txtmark.EmojiEmitter;
import com.github.rjeschke.txtmark.MarkdownUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.willshex.blogwt.client.Resources;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostHelper extends
		com.willshex.blogwt.shared.api.helper.PostHelper {

	private static MarkdownProcessor processor;

	private static MarkdownProcessor processor () {
		if (processor == null) {
			processor = new MarkdownProcessor() {
				/* (non-Javadoc)
				 * 
				 * @see org.markdown4j.client.MarkdownProcessor#emojiEmitter() */
				@Override
				protected EmojiEmitter emojiEmitter () {
					return new EmojiEmitter() {

						@Override
						public void emitEmoji (StringBuilder out, String name,
								Decorator decorator) {
							SafeUri safeLink = emoji.gwt.emoji.Emoji.get()
									.safeUri(name);
							String link;
							String comment;
							if (safeLink != null
									&& (link = safeLink.asString()).length() != 0) {
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
							} else {
								out.append(name);
							}
						}

					};
				}
			};

			processor.setDecorator(new ExtDecorator() {

				/* (non-Javadoc)
				 * 
				 * @see
				 * org.markdown4j.ExtDecorator#openImage(java.lang.StringBuilder
				 * ) */
				@Override
				public void openImage (StringBuilder out) {
					super.openImage(out);
					out.append(" class=\"" + Resources.RES.styles().image()
							+ "\" ");
				}

			});
		}

		return processor;
	}

	public static String makeMarkup (String value) {
		return processor().process(value);
	}

	public static String makeHeading (String value) {
		if (!value.startsWith("#")) {
			value = "#" + value;
		}

		return makeMarkup(value);
	}

	public static String makeHeading2 (String value) {
		if (!value.startsWith("##")) {
			value = "##" + value;
		}

		return makeMarkup(value);
	}

}
