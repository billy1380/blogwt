//
//  StyledImageCell.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.cell;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * @author William Shakour (billy1380)
 *
 */
public class StyledImageCell extends AbstractCell<String> {

	private List<String> classNames = null;
	private Double width, height;

	public StyledImageCell addClassName (String className) {
		if (className != null && className.length() > 0) {
			ensureClassNames();
			classNames.add(className);
		}

		return this;
	}

	public StyledImageCell () {}

	public StyledImageCell (double width, double height) {
		this.width = Double.valueOf(width);
		this.height = Double.valueOf(height);
	}

	private List<String> ensureClassNames () {
		if (classNames == null) {
			classNames = new ArrayList<String>();
		}

		return classNames;
	}

	@Override
	public void render (Context context, String value, SafeHtmlBuilder sb) {
		if (value != null) {
			sb.appendHtmlConstant("<img ");

			if (classNames != null) {
				sb.appendHtmlConstant("class=\"");

				boolean first = true;
				for (String className : classNames) {
					if (first) {
						first = false;
					} else {
						sb.appendHtmlConstant(" ");
					}

					sb.appendEscaped(className);
				}
				sb.appendHtmlConstant("\" ");
			}

			sb.appendHtmlConstant("style=\"object-fit: cover;\" ");

			if (width != null) {
				sb.appendHtmlConstant("width=\"");
				sb.appendEscaped(width.toString());
				sb.appendEscaped("px");
				sb.appendHtmlConstant("\" ");
			}

			if (height != null) {
				sb.appendHtmlConstant("height=\"");
				sb.appendEscaped(height.toString());
				sb.appendEscaped("px");
				sb.appendHtmlConstant("\" ");
			}

			sb.appendHtmlConstant("src=\"");
			sb.append(SafeHtmlUtils.fromString(value));
			sb.appendHtmlConstant("\">");
		}
	}

	public StyledImageCell width (double value) {
		width = Double.valueOf(value);
		return this;
	}

	public StyledImageCell height (double value) {
		height = Double.valueOf(value);
		return this;
	}
}
