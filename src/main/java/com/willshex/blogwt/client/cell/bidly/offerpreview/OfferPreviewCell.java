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
import com.willshex.blogwt.client.page.bidly.dummy.data.Offers;
import com.willshex.blogwt.client.page.bidly.dummy.datatypes.Offer;

/**
 * @author William Shakour (billy1380)
 *
 */
public class OfferPreviewCell extends AbstractCell<Offer> {

	interface Templates extends SafeHtmlTemplates {
		public static final Templates T = GWT.create(Templates.class);

		@Template("<div class=\"col-sm-4 col-md-3\">"
				+ "<div class=\"thumbnail\">"
				+ "<div style=\"height:5px;background-color:{2}\" class=\"img-rounded\">&nbsp;</div>"
				+ "<div class=\"caption\"><h3>{0}</h3>"
				+ "<span class=\"glyphicon glyphicon-map-marker\"></span> "
				+ "<a target=\"_blank\" href=\"http://google.com/maps?q={1}\">{1}</a>"
				+ "</div></div></div>")
		SafeHtml cell (String name, String location, String color);
	}

	private static final String AMBER = "#ffc107";
	private static final String RED = "#f44336";
	private static final String GREEN = "#4caf50";
	private static final String BLUE = "#1e88e5";
	private static final String GREY = "#546e7a";

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client
	 * .Cell.Context, java.lang.Object,
	 * com.google.gwt.safehtml.shared.SafeHtmlBuilder) */
	@Override
	public void render (Context context, Offer value, SafeHtmlBuilder sb) {
		String postcode = value.address.postcode;
		postcode = postcode.replace(" ", "");
		postcode = postcode.substring(0, postcode.length() - 3);
		sb.append(
				Templates.T.cell(value.user.surname, postcode, colour(value)));
	}

	public String colour (Offer offer) {
		String colour = null;

		if (Offers.ACCEPTED.contains(offer)) {
			colour = GREEN;
		} else if (Offers.EXPIRED.contains(offer)) {
			colour = RED;
		} else if (Offers.NEGOTIATING.contains(offer)) {
			colour = AMBER;
		} else if (offer.id.equals(1L)) {
			colour = BLUE;
		} else {
			colour = GREY;
		}

		return colour;
	}
}
