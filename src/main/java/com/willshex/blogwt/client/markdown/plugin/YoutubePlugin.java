//
//  YoutubePlugin.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 7 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.markdown.plugin;

import java.util.List;
import java.util.Map;

import org.markdown4j.Plugin;

/**
 * @author William Shakour (billy1380)
 *
 */
public class YoutubePlugin extends Plugin {

	public static final String ASPECT_16_9 = "16by9";
	public static final String ASPECT_4_3 = "4by3";

	public static final String RESPONSIVE_CLASS = "embed-responsive";
	public static final String RESPONSIVE_ITEM_CLASS = "embed-responsive-item";

	public YoutubePlugin () {
		super("youtube");
	}

	@Override
	public void emit (StringBuilder out, List<String> lines,
			Map<String, String> params) {

		boolean hasAllowFullscreen = params.containsKey("allowfullscreen"), hasResponsive = params
				.containsKey("responsive"), hasWidth = params
				.containsKey("width"), hasHeight = params.containsKey("height"), hasSrc = params
				.containsKey("src"), hasFrameBorder = params
				.containsKey("frameborder");

		if (hasResponsive) {
			String aspect = ASPECT_16_9;

			if (params.containsKey("aspectratio")) {
				switch (params.get("aspectratio")) {
				case ASPECT_4_3:
					aspect = ASPECT_4_3;
					break;
				}
			}

			out.append("<div class=\"");
			out.append(RESPONSIVE_CLASS);
			out.append(" ");
			out.append(RESPONSIVE_CLASS);
			out.append("-");
			out.append(aspect);
			out.append("\">");
		} else {
			out.append("<div class=\"text-center\">");
		}

		out.append("<iframe");
		out.append(" ");

		if (hasWidth && !hasResponsive) {
			out.append("width=\"");
			out.append(params.get("width"));
			out.append("\"");
			out.append(" ");
		}

		if (hasHeight && !hasResponsive) {
			out.append("height=\"");
			out.append(params.get("height"));
			out.append("\"");
			out.append(" ");
		}

		if (hasSrc) {
			out.append("src=\"");
			out.append(params.get("src"));
			out.append("\"");
			out.append(" ");
		}

		if (hasFrameBorder) {
			out.append("frameborder=\"");
			out.append(params.get("frameborder"));
			out.append("\"");
			out.append(" ");
		}

		if (hasAllowFullscreen) {
			out.append("allowfullscreen");
			out.append(" ");
		}

		out.append("></iframe></div>");

	}
}
