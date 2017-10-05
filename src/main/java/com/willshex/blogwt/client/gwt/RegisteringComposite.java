//
//  RegisteringComposite.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 7 Apr 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.gwt;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;

/**
 * @author William Shakour (billy1380)
 *
 */
public class RegisteringComposite extends Composite {

	private List<HandlerRegistration> registrations = null;

	protected void register (HandlerRegistration handlerRegistration) {
		ensureRegister().add(handlerRegistration);
	}

	private List<HandlerRegistration> ensureRegister () {
		if (registrations == null) {
			registrations = new ArrayList<>();
		}

		return registrations;
	}

	protected void unregisterAll () {
		if (registrations != null) {
			for (HandlerRegistration registration : registrations) {
				registration.removeHandler();
			}
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onDetach() */
	@Override
	protected void onDetach () {
		unregisterAll();

		super.onDetach();
	}
}
