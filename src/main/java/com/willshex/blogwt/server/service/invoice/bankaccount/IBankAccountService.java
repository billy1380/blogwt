//  
//  IBankAccountService.java
//  xsdwsdl2code
//
//  Created by William Shakour on April 25, 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS. All rights reserved.
//
package com.willshex.blogwt.server.service.invoice.bankaccount;

import java.util.List;

import com.willshex.blogwt.shared.api.datatype.invoice.BankAccount;
import com.willshex.blogwt.shared.api.datatype.invoice.Vendor;
import com.willshex.service.IService;

public interface IBankAccountService extends IService {
	public static final String NAME = "quickinvoice.bankaccount";

	/**
	 * @param id
	 * @return
	 */
	public BankAccount getBankAccount (Long id);

	/**
	 * @param bankAccount
	 * @return
	 */
	public BankAccount addBankAccount (BankAccount bankAccount);

	/**
	 * @param bankAccount
	 * @return
	 */
	public BankAccount updateBankAccount (BankAccount bankAccount);

	/**
	 * @param bankAccount
	 */
	public void deleteBankAccount (BankAccount bankAccount);

	/**
	 * Get vendor bank accounts
	 * 
	 * @return
	 */
	public List<BankAccount> getVendorBankAccounts (Vendor vendor);

}