//
//  UsedOfferPreview.java
//  bidly
//
//  Created by William Shakour (billy1380) on 3 Jan 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.cell.bidly.offerpreview;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.willshex.blogwt.client.page.bidly.dummy.data.Offers;
import com.willshex.blogwt.client.page.bidly.dummy.datatypes.Offer;
import com.willshex.blogwt.shared.helper.bidly.AddressHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class UsedOfferPreview extends AbstractCell<Offer> {
	interface Templates extends SafeHtmlTemplates {
		public static final Templates T = GWT.create(Templates.class);

		@Template("<div class=\"col-sm-4 col-md-3\">"
				+ "<div class=\"btn btn-default\" style=\"width:100%;text-align:left;margin-bottom:16px;text-transform:none;\">"
				+ "<div style=\"height:5px;background-color:{5}\">&nbsp;</div>"
				+ "<div style=\"margin:20px 0;color:#333;\">"
				+ "<h3>{0} <span class=\"h5\"><span class=\"glyphicon glyphicon-map-marker\"></span>"
				+ "<a target=\"_blank\" href=\"http://google.com/maps?q={1}\">{1}</a></span></h3>"
				+ "<h3>{2} <br /> <span class=\"h4 text-muted\">{3}</span> <br /><span class=\"h4\" style=\"font-family:'Courier New',Courier,'Lucida Sans Typewriter','Lucida Typewriter',monospace;\">{4}</span></h3>"
				+ "</div></div></div>")
		SafeHtml cell (String name, String location, String make, String model,
				String registration, String color);
	}

	private static final String AMBER = "#ffc107";
	private static final String GREEN = "#4caf50";

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client
	 * .Cell.Context, java.lang.Object,
	 * com.google.gwt.safehtml.shared.SafeHtmlBuilder) */
	@Override
	public void render (Context context, Offer value, SafeHtmlBuilder sb) {
		sb.append(Templates.T.cell(value.user.surname,
				AddressHelper.postcodeArea(value.address.postcode),
				value.vehicle.build.make, value.vehicle.build.model,
				value.vehicle.registration, colour(value)));
	}

	public String colour (Offer offer) {
		String colour = null;

		if (Offers.ACCEPTED.contains(offer) || offer.id.equals(10L)) {
			colour = GREEN;
		} else if (Offers.UEN.contains(offer)) {
			colour = AMBER;
		}

		return colour;
	}
}
