//
//  FormController.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 13 Nov 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.controller;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.shared.api.datatype.Form;
import com.willshex.blogwt.shared.api.page.call.SubmitFormRequest;
import com.willshex.blogwt.shared.api.page.call.SubmitFormResponse;
import com.willshex.blogwt.shared.api.page.call.event.SubmitFormEventHandler;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class FormController {
	private static FormController one = null;

	public static FormController get () {
		if (one == null) {
			one = new FormController();
		}

		return one;
	}

	public void submitForm (Form form) {
		final SubmitFormRequest input = ApiHelper
				.setAccessCode(new SubmitFormRequest());
		input.form = form;

		ApiHelper.createPageClient().submitForm(input,
				new AsyncCallback<SubmitFormResponse>() {

					@Override
					public void onSuccess (SubmitFormResponse output) {
						if (output.status == StatusType.StatusTypeSuccess) {

						}

						DefaultEventBus.get().fireEventFromSource(
								new SubmitFormEventHandler.SubmitFormSuccess(
										input, output), FormController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new SubmitFormEventHandler.SubmitFormFailure(
										input, caught), FormController.this);
					}
				});
	}
}
