//  
//  ICurrencyService.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.server.service.invoice.currency;

import java.util.List;

import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.invoice.Currency;
import com.willshex.blogwt.shared.api.datatype.invoice.CurrencySortType;
import com.willshex.service.IService;

public interface ICurrencyService extends IService {
	public static final String NAME = "quickinvoice.currency";

	/**
	 * @param id
	 * @return
	 */
	public Currency getCurrency (Long id);

	/**
	 * @param currency
	 * @return
	 */
	public Currency addCurrency (Currency currency);

	/**
	 * @param currency
	 * @return
	 */
	public Currency updateCurrency (Currency currency);

	/**
	 * @param currency
	 */
	public void deleteCurrency (Currency currency);

	/**
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public List<Currency> getCurrencies (Long start, Long count,
			CurrencySortType sortBy, SortDirectionType sortDirection);

	/**
	 * @return
	 */
	public Integer getCurrenciesCount ();

	/**
	 * @param code
	 * @return
	 */
	public Currency getCodeCurrency (String code);

	/**
	 * 
	 */
	public void setupCurrencies ();

}