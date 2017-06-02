//  
//  BankAccountService.java
//  xsdwsdl2code
//
//  Created by William Shakour on April 25, 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS. All rights reserved.
//
package com.willshex.blogwt.server.service.invoice.bankaccount;

import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.willshex.blogwt.shared.api.datatype.invoice.BankAccount;
import com.willshex.blogwt.shared.api.datatype.invoice.Vendor;

final class BankAccountService implements IBankAccountService {
	public String getName () {
		return NAME;
	}

	@Override
	public BankAccount getBankAccount (Long id) {
		return provide().load().type(BankAccount.class).id(id.longValue())
				.now();
	}

	@Override
	public BankAccount addBankAccount (BankAccount bankAccount) {
		if (bankAccount.created == null) {
			bankAccount.created = new Date();
		}

		bankAccount.vendorKey = Key.create(bankAccount.vendor);

		Key<BankAccount> key = provide().save().entity(bankAccount).now();
		bankAccount.id = Long.valueOf(key.getId());
		return bankAccount;
	}

	@Override
	public BankAccount updateBankAccount (BankAccount bankAccount) {
		provide().save().entity(bankAccount);
		return bankAccount;
	}

	@Override
	public void deleteBankAccount (BankAccount bankAccount) {
		provide().delete().entity(bankAccount);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.bankaccount.
	 * IBankAccountService#getVendorBankAccounts(com.spacehopperstudios.
	 * quickinvoice.shared. api.datatypes.Vendor) */
	@Override
	public List<BankAccount> getVendorBankAccounts (Vendor vendor) {
		Query<BankAccount> query = provide().load().type(BankAccount.class)
				.filter("vendorKey", vendor);

		// if (start != null) {
		// query = query.offset(start.intValue());
		// }
		//
		// if (count != null) {
		// query = query.limit(count.intValue());
		// }
		//
		// if (sortBy != null) {
		// String condition = sortBy.toString();
		//
		// if (sortDirection != null) {
		// switch (sortDirection) {
		// case SortDirectionTypeDescending:
		// condition = "-" + condition;
		// break;
		// default:
		// break;
		// }
		// }
		String condition = "name";
		query = query.order(condition);
		// }

		return query.list();
	}

}