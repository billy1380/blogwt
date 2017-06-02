//
//  BankAccountController.java
//  reckoning
//
//  Created by William Shakour (billy1380) on 26 Apr 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.client.controller.invoice;

/**
 * @author William Shakour (billy1380)
 *
 */
public class BankAccountController {
	private static BankAccountController one = null;

	public static BankAccountController get() {
		if (one == null) {
			one = new BankAccountController();
		}

		return one;
	}
}
