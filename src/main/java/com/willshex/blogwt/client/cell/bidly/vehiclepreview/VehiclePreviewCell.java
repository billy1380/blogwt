//
//  VehicePreviewCell.java
//  bidly
//
//  Created by William Shakour (billy1380) on 27 Dec 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.cell.bidly.vehiclepreview;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.willshex.blogwt.client.page.bidly.dummy.datatypes.Vehicle;

/**
 * @author William Shakour (billy1380)
 *
 */
public class VehiclePreviewCell extends AbstractCell<Vehicle> {

	interface Templates extends SafeHtmlTemplates {
		public static final Templates T = GWT.create(Templates.class);

		@Template("<div class=\"col-sm-4 col-md-3\">"
				+ "<div class=\"btn btn-default\" style=\"width:100%;text-align:left;margin-bottom:16px;text-transform:none;\">"
				+ "<div style=\"margin:20px 0;color:#333;\"><h3>{0} <br /> <span class=\"h4 text-muted\">{1}</span> <br /><span class=\"h4\" style=\"font-family:'Courier New',Courier,'Lucida Sans Typewriter','Lucida Typewriter',monospace;\">{2}</span></h3></div></div></div>")
		SafeHtml cell (String make, String model, String registration);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client
	 * .Cell.Context, java.lang.Object,
	 * com.google.gwt.safehtml.shared.SafeHtmlBuilder) */
	@Override
	public void render (Context context, Vehicle value, SafeHtmlBuilder sb) {
		sb.append(Templates.T.cell(value.build.make, value.build.model,
				value.registration));
	}

}
