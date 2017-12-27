//
//  DashboardPage.java
//  bidly
//
//  Created by William Shakour (billy1380) on 20 Dec 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.bidly.dealer.dashboard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.page.Page;

/**
 * @author William Shakour (billy1380)
 *
 */
public class DashboardPage extends Page {

	private static DashboardPageUiBinder uiBinder = GWT
			.create(DashboardPageUiBinder.class);

	interface DashboardPageUiBinder extends UiBinder<Widget, DashboardPage> {}

	interface Style extends CssResource {
		String links ();

		String polygons ();

		String sites ();
	}

	@UiField Style style;
	@UiField Element elChart;
	@UiField Element elChart1;
	@UiField Element elChart2;
	@UiField Element elChart3;
	@UiField Element elChart4;
	@UiField Element elChart5;

	public DashboardPage () {
		initWidget(uiBinder.createAndBindUi(this));

		ScriptInjector
				.fromUrl(
						"//cdn.jsdelivr.net/chartist.js/latest/chartist.min.js")
				.setWindow(ScriptInjector.TOP_WINDOW).inject();

		RequestBuilder b = new RequestBuilder(RequestBuilder.GET,
				"//cdn.jsdelivr.net/chartist.js/latest/chartist.min.css");

		b.setCallback(new RequestCallback() {

			@Override
			public void onResponseReceived (Request request,
					Response response) {
				StyleInjector.inject(response.getText());
			}

			@Override
			public void onError (Request request, Throwable exception) {}
		});

		try {
			b.send();
		} catch (RequestException e) {}

		Scheduler.get().scheduleFinally( () -> {
			if (!isLoaded()) return true;

			run(elChart,
					"{\"labels\" : [\"Mon\", \"Tue\", \"Wed\", \"Thu\", \"Fri\"],\"series\" : [[5, 2, 4, 2, 0]]}");
			run(elChart1,
					"{\"labels\" : [\"Mon\", \"Tue\", \"Wed\", \"Thu\", \"Fri\"],\"series\" : [[],[],[],[],[0.5, 1, 4, 6, 5]]}");
			run(elChart2,
					"{\"labels\" : [\"Jan\", \"Feb\", \"Mar\"],\"series\" : [[],[],[],[5, 2, 4]]}");
			run(elChart3,
					"{\"labels\" : [\"Apr\", \"May\", \"Jun\"],\"series\" : [[], [3, 2, 1]]}");
			run(elChart4,
					"{\"labels\" : [\"Jul\", \"Aug\", \"Sep\"],\"series\" : [[], [], [3, 5, 2]]}");
			run(elChart5,
					"{\"labels\" : [\"Oct\", \"Nov\", \"Dec\"],\"series\" : [[], [], [],[], [], [3, 5, 2]]}");

			return false;
		});
	}

	private static native boolean isLoaded () /*-{
	return $wnd.Chartist != null;
	}-*/;

	private static native void run (Element element, String json) /*-{
	var data = $wnd.JSON.parse(json);
	new $wnd.Chartist.Line(element, data, {
	    low : 0,
	    showArea : true,
	    fullWidth : true
	});
	}-*/;

}
