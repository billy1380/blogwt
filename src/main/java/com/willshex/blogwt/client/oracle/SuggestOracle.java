//
//  SuggestOracle.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 30 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.oracle;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Timer;

/**
 * @author William Shakour (billy1380)
 *
 */
public abstract class SuggestOracle<T> extends
		com.google.gwt.user.client.ui.SuggestOracle {

	private Request request;
	private Callback callback;
	private com.google.gwt.http.client.Request tracking;
	private Timer lookup = new Timer() {

		@Override
		public void run () {
			SuggestOracle.this.lookup(request, callback);
		}
	};

	class Suggestion implements
			com.google.gwt.user.client.ui.SuggestOracle.Suggestion {

		T item;

		public Suggestion (T item) {
			this.item = item;
		}

		/* (non-Javadoc)
		 * 
		 * @see
		 * com.google.gwt.user.client.ui.SuggestOracle.Suggestion#getDisplayString
		 * () */
		@Override
		public String getDisplayString () {
			return SuggestOracle.this.getDisplayString(item);
		}

		/* (non-Javadoc)
		 * 
		 * @see com.google.gwt.user.client.ui.SuggestOracle.Suggestion#
		 * getReplacementString() */
		@Override
		public String getReplacementString () {
			return SuggestOracle.this.getReplacementString(item);
		}

	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.client.ui.SuggestOracle#requestSuggestions(com.google
	 * .gwt.user.client.ui.SuggestOracle.Request,
	 * com.google.gwt.user.client.ui.SuggestOracle.Callback) */
	@Override
	public void requestSuggestions (Request request, Callback callback) {
		if (request.getQuery().length() >= getQueryMinLength()) {
			lookup.cancel();
			if (this.tracking != null) {
				this.tracking.cancel();
			}
			this.request = request;
			this.callback = callback;
			lookup.schedule(500);
		}
	}

	protected abstract void lookup (Request request, Callback callback);

	protected void foundItems (Request request, Callback callback, List<T> items) {
		if (items != null && request == SuggestOracle.this.request) {
			if (callback != null) {
				List<Suggestion> suggestions = new ArrayList<Suggestion>();

				for (T item : items) {
					suggestions.add(new Suggestion(item));
				}

				callback.onSuggestionsReady(request, new Response(suggestions));
			}
		}
	}

	/**
	 * @param item
	 * @return
	 */
	protected abstract String getDisplayString (T item);

	/**
	 * @param item
	 * @return
	 */
	protected abstract String getReplacementString (T item);

	protected int getQueryMinLength () {
		return 3;
	}

}
