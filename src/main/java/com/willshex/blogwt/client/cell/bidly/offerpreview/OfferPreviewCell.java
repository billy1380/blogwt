//
//  OfferPreviewCell.java
//  bidly
//
//  Created by William Shakour (billy1380) on 27 Dec 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.cell.bidly.offerpreview;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.willshex.blogwt.client.page.bidly.dummy.datatypes.Offer;

/**
 * @author William Shakour (billy1380)
 *
 */
public class OfferPreviewCell extends AbstractCell<Offer> {

	interface Templates extends SafeHtmlTemplates {
		public static final Templates T = GWT.create(Templates.class);

		@Template("<div class=\"col-sm-4 col-md-3\"><div class=\"thumbnail\"><div class=\"caption\"><h3>{0}</h3><p>{1}</p></div></div></div>")
		SafeHtml cell (String name, String location);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client
	 * .Cell.Context, java.lang.Object,
	 * com.google.gwt.safehtml.shared.SafeHtmlBuilder) */
	@Override
	public void render (Context context, Offer value, SafeHtmlBuilder sb) {
		sb.append(Templates.T.cell(value.user.surname, value.address.postcode));
	}

}
