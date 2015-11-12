//  
//  SubmitFormEventHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on November 12, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.page.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.page.call.SubmitFormRequest;
import com.willshex.blogwt.shared.api.page.call.SubmitFormResponse;

public interface SubmitFormEventHandler extends EventHandler {
	public static final GwtEvent.Type<SubmitFormEventHandler> TYPE = new GwtEvent.Type<SubmitFormEventHandler>();

	public void submitFormSuccess (final SubmitFormRequest input,
			final SubmitFormResponse output);

	public void submitFormFailure (final SubmitFormRequest input,
			final Throwable caught);

	public class SubmitFormSuccess extends GwtEvent<SubmitFormEventHandler> {
		private SubmitFormRequest input;
		private SubmitFormResponse output;

		public SubmitFormSuccess (final SubmitFormRequest input,
				final SubmitFormResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<SubmitFormEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (SubmitFormEventHandler handler) {
			handler.submitFormSuccess(input, output);
		}
	}

	public class SubmitFormFailure extends GwtEvent<SubmitFormEventHandler> {
		private SubmitFormRequest input;
		private Throwable caught;

		public SubmitFormFailure (final SubmitFormRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<SubmitFormEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (SubmitFormEventHandler handler) {
			handler.submitFormFailure(input, caught);
		}
	}

}