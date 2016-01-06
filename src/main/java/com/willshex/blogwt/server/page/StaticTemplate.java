//
//  StaticTemplate.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 18 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.page;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.markdown4j.server.IncludePlugin;
import org.markdown4j.server.MarkdownProcessor;

import com.willshex.blogwt.server.service.page.PageServiceProvider;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.PageSortType;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.blogwt.shared.page.Stack;

/**
 * @author William Shakour (billy1380)
 *
 */
abstract class StaticTemplate implements PageMarkup {

	public static final String ACCESS_CODE = "ded02740-5e12-11e5-b0c2-7054d251af02";

	protected Stack stack;

	protected Page home;
	protected List<Page> headerPages;

	private MarkdownProcessor processor;

	public StaticTemplate (Stack stack) {
		this.stack = stack;

		processor = new MarkdownProcessor();
		processor.registerPlugins(new IncludePlugin());
	}

	protected String process (String markdown) {
		String processed = "";
		try {
			processed = processor.process(markdown);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return processed;
	}

	protected <T extends Request> T input (Class<T> type) {
		T input = null;
		try {
			input = type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		input.accessCode = ACCESS_CODE;

		return input;
	}

	protected void appendHeader (StringBuffer markup) {
		String title = PropertyServiceProvider.provide()
				.getNamedProperty(PropertyHelper.TITLE).value;
		String extendedTitle = PropertyServiceProvider.provide()
				.getNamedProperty(PropertyHelper.EXTENDED_TITLE).value;

		markup.append(
				"<html><head><link rel=\"alternate\" type=\"application/rss+xml\" title=\"");
		markup.append(title);
		markup.append(
				" (RSS feed)\" href=\"/feed\"><link rel=\"icon\" href=\"");
		markup.append("{favicon}");
		markup.append("\" type=\"image/x-icon\"><title>");
		markup.append(title + " - " + title());
		markup.append("</title></head><body>");
		markup.append("<h1>");
		markup.append(title);
		markup.append("</h1>");
		markup.append("<h2>");
		markup.append(extendedTitle);
		markup.append("</h2>");

		appendNavigationLinks(markup);
	}

	private void appendNavigationLinks (StringBuffer markup) {
		List<Page> pages = PageServiceProvider.provide().getPages(Boolean.FALSE,
				Integer.valueOf(0), null, PageSortType.PageSortTypePriority,
				null);

		if (pages != null) {
			markup.append("<ul>");

			for (Page page : pages) {
				markup.append("<li><a href=\"/#!").append(page.slug)
						.append("\" >").append(page.title).append("</a></li>");
			}

			markup.append("</ul>");
		}
	}

	protected void appendFooter (StringBuffer markup) {
		String copyright = PropertyServiceProvider.provide()
				.getNamedProperty(PropertyHelper.COPYRIGHT_URL).value;
		String copyrightHolder = PropertyServiceProvider.provide()
				.getNamedProperty(PropertyHelper.COPYRIGHT_HOLDER).value;

		String title = PropertyServiceProvider.provide()
				.getNamedProperty(PropertyHelper.TITLE).value;

		markup.append(
				"<footer class=\"footer\"> <div class=\"container\">Copyright &copy; <a href=\"");
		markup.append(copyright);
		markup.append("\" target=\"_blank\">");
		markup.append(copyrightHolder);
		markup.append("</a> ");
		markup.append(Calendar.getInstance().get(Calendar.YEAR));
		markup.append(". All rights reserved - ");
		markup.append(title);
		markup.append(".</div> </footer></div></div></body></html>");
	}

	public String asString () {
		StringBuffer markup = new StringBuffer();

		appendHeader(markup);

		appendPage(markup);

		appendFooter(markup);

		return markup.toString();
	}

	protected abstract void appendPage (StringBuffer markup);

	protected abstract String title ();

}
