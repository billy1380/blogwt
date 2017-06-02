//  
//  CurrencyService.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.server.service.invoice.currency;

import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.invoice.Currency;
import com.willshex.blogwt.shared.api.datatype.invoice.CurrencyPossitionType;
import com.willshex.blogwt.shared.api.datatype.invoice.CurrencySortType;

final class CurrencyService implements ICurrencyService {

	public String getName () {
		return NAME;
	}

	public Currency getCurrency (Long id) {
		return provide().load().type(Currency.class).id(id.longValue()).now();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.currency.
	 * ICurrencyService#addCurrency(com.spacehopperstudios.quickinvoice.shared.
	 * api.datatypes. Currency ) */
	@Override
	public Currency addCurrency (Currency currency) {
		if (currency.created == null) {
			currency.created = new Date();
		}
		Key<Currency> key = provide().save().entity(currency).now();
		currency.id = Long.valueOf(key.getId());
		return currency;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.currency.
	 * ICurrencyService#updateCurrency(com.spacehopperstudios.quickinvoice.
	 * shared.api.datatypes .Currency) */
	@Override
	public Currency updateCurrency (Currency currency) {
		provide().save().entity(currency);
		return currency;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.currency.
	 * ICurrencyService#deleteCurrency(com.spacehopperstudios.quickinvoice.
	 * shared.api.datatypes .Currency) */
	@Override
	public void deleteCurrency (Currency currency) {
		provide().delete().entity(currency);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.invoice.currency.ICurrencyService#
	 * getCurrencies(java.lang.Integer, java.lang.Integer,
	 * com.willshex.blogwt.shared.api.datatype.invoice.CurrencySortType,
	 * com.willshex.blogwt.shared.api.SortDirectionType) */
	@Override
	public List<Currency> getCurrencies (Integer start, Integer count,
			CurrencySortType sortBy, SortDirectionType sortDirection) {
		Query<Currency> query = provide().load().type(Currency.class);

		if (start != null) {
			query = query.offset(start.intValue());
		}

		if (count != null) {
			query = query.limit(count.intValue());
		}

		if (sortBy != null) {
			String condition = sortBy.toString();

			if (sortDirection != null) {
				switch (sortDirection) {
				case SortDirectionTypeDescending:
					condition = "-" + condition;
					break;
				default:
					break;
				}
			}

			query = query.order(condition);
		}

		return query.list();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.currency.
	 * ICurrencyService#getCurrenciesCount() */
	@Override
	public Integer getCurrenciesCount () {
		return Integer.valueOf(PropertyServiceProvider.provide()
				.getNamedProperty("currency.count").value);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.currency.
	 * ICurrencyService#getCodeCurrency(java.lang.String) */
	@Override
	public Currency getCodeCurrency (String code) {
		Key<Currency> key = provide().load().type(Currency.class)
				.filter("code", code).limit(1).keys().first().now();
		return getCurrency(Long.valueOf(key.getId()));
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.currency.
	 * ICurrencyService#setupCurrencies() */
	@Override
	public void setupCurrencies () {
		if (PropertyServiceProvider.provide()
				.getNamedProperty("currency.count") == null) {
			CurrencyServiceProvider.provide().addCurrency(new Currency()
					.code("GBP").description("Pounds Sterling (Great Britain)")
					.symbol("£").symbolMarkup("pound").possition(
							CurrencyPossitionType.CurrencyPossitionTypeBefore));
			CurrencyServiceProvider.provide().addCurrency(new Currency()
					.code("USD").description("United States Dollar").symbol("$")
					.symbolMarkup(null).possition(
							CurrencyPossitionType.CurrencyPossitionTypeBefore));

			PropertyServiceProvider.provide()
					.addProperty(new Property().name("currency.count")
							.group("Invoice properties").value("2")
							.description("Number of currencies available")
							.type("system"));
		}
	}

}