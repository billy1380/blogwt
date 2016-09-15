//  
//  SearchAllEventHandler.java
//  blogwt
//
//  Created by William Shakour on August 20, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.api.search.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.search.call.SearchAllRequest;
import com.willshex.blogwt.shared.api.search.call.SearchAllResponse;

public interface SearchAllEventHandler extends EventHandler {
	public static final GwtEvent.Type<SearchAllEventHandler> TYPE = new GwtEvent.Type<SearchAllEventHandler>();

	public void searchAllSuccess (final SearchAllRequest input,
			final SearchAllResponse output);

	public void searchAllFailure (final SearchAllRequest input,
			final Throwable caught);

	public class SearchAllSuccess extends GwtEvent<SearchAllEventHandler> {
		private SearchAllRequest input;
		private SearchAllResponse output;

		public SearchAllSuccess (final SearchAllRequest input,
				final SearchAllResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<SearchAllEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (SearchAllEventHandler handler) {
			handler.searchAllSuccess(input, output);
		}
	}

	public class SearchAllFailure extends GwtEvent<SearchAllEventHandler> {
		private SearchAllRequest input;
		private Throwable caught;

		public SearchAllFailure (final SearchAllRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<SearchAllEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (SearchAllEventHandler handler) {
			handler.searchAllFailure(input, caught);
		}
	}

}