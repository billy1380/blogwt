// 
//  GetCurrenciesActionHandler.java
//  rekoning
// 
//  Created by William Shakour on June 2, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.api.invoice.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.service.invoice.currency.CurrencyServiceProvider;
import com.willshex.blogwt.server.service.invoice.currency.ICurrencyService;
import com.willshex.blogwt.shared.api.datatype.invoice.CurrencySortType;
import com.willshex.blogwt.shared.api.invoice.call.GetCurrenciesRequest;
import com.willshex.blogwt.shared.api.invoice.call.GetCurrenciesResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.gson.web.service.server.InputValidationException;

public final class GetCurrenciesActionHandler
		extends ActionHandler<GetCurrenciesRequest, GetCurrenciesResponse> {

	private static final Logger LOG = Logger
			.getLogger(GetCurrenciesActionHandler.class.getName());

	@Override
	public void handle (GetCurrenciesRequest input,
			GetCurrenciesResponse output) throws Exception {
		input = ApiValidator.request(input, GetCurrenciesRequest.class);
		input.accessCode = ApiValidator.accessCode(input.accessCode,
				"input.accessCode");
		output.session = input.session = SessionValidator.lookup(input.session,
				"input.session");

		if (input.pager == null) {
			input.pager = PagerHelper.createDefaultPager();
		}

		CurrencySortType sortBy = null;
		if (input.pager.sortBy != null) {
			if ((sortBy = CurrencySortType
					.fromString(input.pager.sortBy)) == null)
				throw new InputValidationException(100025,
						"Invalid argument - CurrencySortBy: input.sortBy");
		}

		ICurrencyService currencyService = CurrencyServiceProvider.provide();

		output.currencies = currencyService.getCurrencies(input.pager.start,
				input.pager.count, sortBy, input.pager.sortDirection);

		output.pager = PagerHelper.moveForward(input.pager);
	}

	@Override
	protected GetCurrenciesResponse newOutput () {
		return new GetCurrenciesResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}