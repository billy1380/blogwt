// 
//  SetInvoiceStatusActionHandler.java
//  rekoning
// 
//  Created by William Shakour on June 2, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.api.invoice.action;

import java.util.logging.Logger;

import com.willshex.blogwt.shared.api.invoice.call.SetInvoiceStatusRequest;
import com.willshex.blogwt.shared.api.invoice.call.SetInvoiceStatusResponse;
import com.willshex.blogwt.server.api.ActionHandler;

public final class SetInvoiceStatusActionHandler extends
		ActionHandler<SetInvoiceStatusRequest, SetInvoiceStatusResponse> {

	private static final Logger LOG = Logger
			.getLogger(SetInvoiceStatusActionHandler.class.getName());

	@Override
	public void handle (SetInvoiceStatusRequest input,
			SetInvoiceStatusResponse output) throws Exception {}

	@Override
	protected SetInvoiceStatusResponse newOutput () {
		return new SetInvoiceStatusResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}