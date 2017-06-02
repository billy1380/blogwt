//  
//  InvoiceService.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.server.service.invoice.invoice;

import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.Query;
import com.willshex.blogwt.server.service.invoice.currency.CurrencyServiceProvider;
import com.willshex.blogwt.server.service.invoice.currency.ICurrencyService;
import com.willshex.blogwt.server.service.invoice.customer.CustomerServiceProvider;
import com.willshex.blogwt.server.service.invoice.customer.ICustomerService;
import com.willshex.blogwt.server.service.invoice.vendor.VendorServiceProvider;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.invoice.Invoice;
import com.willshex.blogwt.shared.api.datatype.invoice.InvoiceSortType;
import com.willshex.blogwt.shared.api.datatype.invoice.InvoiceStatusType;
import com.willshex.blogwt.shared.api.datatype.invoice.Item;
import com.willshex.blogwt.shared.api.datatype.invoice.Vendor;

final class InvoiceService implements IInvoiceService {
	public String getName () {
		return NAME;
	}

	public Invoice getInvoice (Long id) {
		Invoice invoice = provide().load().type(Invoice.class)
				.id(id.longValue()).now();
		invoice.currency = CurrencyServiceProvider.provide()
				.getCurrency(Long.valueOf(invoice.currencyKey.getId()));

		invoice.items = getInvoiceItems(invoice);

		return invoice;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.invoice.
	 * IInvoiceService #
	 * addInvoice(com.spacehopperstudios.quickinvoice.shared.api.datatypes.
	 * Invoice ) */
	@Override
	public Invoice addInvoice (Invoice invoice) {
		if (invoice.created == null) {
			invoice.created = new Date();
		}

		invoice.vendorKey = Key.create(invoice.vendor);
		invoice.currencyKey = Key.create(invoice.currency);
		invoice.customerKey = Key.create(invoice.customer);
		invoice.accountKey = Key.create(invoice.account);

		if (invoice.date == null) {
			invoice.date = invoice.created;
		}

		if (invoice.status == null) {
			invoice.status = InvoiceStatusType.InvoiceStatusTypeCreated;
		}

		invoice.vendor = VendorServiceProvider.provide()
				.incrementInvoices(invoice.vendor);
		invoice.customer = CustomerServiceProvider.provide()
				.incrementInvoices(invoice.customer);

		invoice.vendor = VendorServiceProvider.provide()
				.incrementOutstanding(invoice.vendor);
		invoice.customer = CustomerServiceProvider.provide()
				.incrementOutstanding(invoice.customer);

		invoice.name = String.format("%s%04d", invoice.customer.code,
				invoice.customer.invoiceCount.intValue());

		Key<Invoice> key = provide().save().entity(invoice).now();
		invoice.id = Long.valueOf(key.getId());

		int i = 0;
		double total = 0;
		for (Item item : invoice.items) {
			addInvoiceItem(invoice, item);
			total += item.price.intValue() * item.quantity.floatValue();
			i++;
		}

		invoice = incrementItems(invoice, Integer.valueOf(i));
		invoice = incrementPrice(invoice, Integer.valueOf((int) total));

		return invoice;
	}

	@Override
	public Invoice incrementPrice (final Invoice invoice,
			final Integer increment) {
		Invoice u = provide().transact(new Work<Invoice>() {

			@Override
			public Invoice run () {
				Invoice l = provide().load().type(Invoice.class)
						.id(invoice.id.longValue()).now();
				l.vendor = invoice.vendor;
				l.customer = invoice.customer;
				l.totalPrice = Integer.valueOf(
						(l.totalPrice == null ? 0 : l.totalPrice.intValue())
								+ increment);
				updateInvoice(l, false);

				return l;
			}

		});

		invoice.totalPrice = u.totalPrice;

		return invoice;
	}

	@Override
	public Invoice incrementItems (Invoice invoice) {
		return incrementItems(invoice, Integer.valueOf(1));
	}

	@Override
	public Invoice incrementItems (final Invoice invoice,
			final Integer increment) {
		Invoice u = provide().transact(new Work<Invoice>() {

			@Override
			public Invoice run () {
				Invoice l = provide().load().type(Invoice.class)
						.id(invoice.id.longValue()).now();
				l.vendor = invoice.vendor;
				l.customer = invoice.customer;
				l.itemCount = Integer.valueOf(
						(l.itemCount == null ? 0 : l.itemCount.intValue())
								+ increment);
				return updateInvoice(l, false);
			}

		});

		invoice.itemCount = u.itemCount;

		return invoice;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.invoice.
	 * IInvoiceService
	 * #updateInvoice(com.spacehopperstudios.quickinvoice.shared.api.datatypes.
	 * Invoice ) */
	@Override
	public Invoice updateInvoice (Invoice invoice, Boolean decrementCount) {
		provide().save().entity(invoice);

		if (decrementCount == Boolean.TRUE) {
			invoice.vendor = VendorServiceProvider.provide()
					.incrementOutstanding(invoice.vendor, Integer.valueOf(-1));
			invoice.customer = CustomerServiceProvider.provide()
					.incrementOutstanding(invoice.customer,
							Integer.valueOf(-1));
		}

		return invoice;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.invoice.
	 * IInvoiceService
	 * #deleteInvoice(com.spacehopperstudios.quickinvoice.shared.api.datatypes.
	 * Invoice ) */
	@Override
	public void deleteInvoice (Invoice invoice) {
		provide().delete().entity(invoice);

		CustomerServiceProvider.provide().incrementInvoices(invoice.customer,
				Integer.valueOf(-1));
		VendorServiceProvider.provide().incrementInvoices(invoice.vendor,
				Integer.valueOf(-1));
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.invoice.
	 * IInvoiceService
	 * #getVendorInvoicesCount(com.spacehopperstudios.quickinvoice.shared.api.
	 * datatypes .Vendor) */
	@Override
	public Integer getVendorInvoicesCount (Vendor vendor) {
		return vendor.invoiceCount;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.invoice.
	 * IInvoiceService #
	 * getVendorInvoices(com.spacehopperstudios.quickinvoice.shared.api.
	 * datatypes .Vendor, java.lang.Long, java.lang.Long,
	 * com.spacehopperstudios.quickinvoice.shared.api.datatypes.InvoiceSortType,
	 * com.spacehopperstudios.quickinvoice.shared.api.SortDirectionType) */
	@Override
	public List<Invoice> getVendorInvoices (Vendor vendor, Long start,
			Long count, InvoiceSortType sortBy,
			SortDirectionType sortDirection) {
		Query<Invoice> query = provide().load().type(Invoice.class)
				.filter("vendorKey", vendor);

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

		List<Invoice> invoices = query.list();

		ICustomerService customerService = CustomerServiceProvider.provide();
		ICurrencyService currencyService = CurrencyServiceProvider.provide();
		for (Invoice invoice : invoices) {
			invoice.customer = customerService
					.getCustomer(Long.valueOf(invoice.customerKey.getId()));
			invoice.currency = currencyService
					.getCurrency(Long.valueOf(invoice.currencyKey.getId()));
		}

		return invoices;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.invoice.
	 * IInvoiceService #getItem(java.lang.Long) */
	@Override
	public Item getItem (Long id) {
		return provide().load().type(Item.class).id(id.intValue()).now();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.invoice.
	 * IInvoiceService
	 * #addInvoiceItem(com.spacehopperstudios.quickinvoice.shared.api.datatypes.
	 * Invoice ,
	 * com.spacehopperstudios.quickinvoice.shared.api.datatypes.Item) */
	@Override
	public Item addInvoiceItem (Invoice invoice, Item item) {
		if (item.created == null) {
			item.created = new Date();
		}

		if (item.date == null) {
			item.date = item.created;
		}

		item.invoiceKey = Key.create(invoice);
		Key<Item> key = provide().save().entity(item).now();
		item.id = Long.valueOf(key.getId());

		return item;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.invoice.
	 * IInvoiceService#updateInvoiceItem(com.spacehopperstudios.quickinvoice.
	 * shared.api.datatypes .Invoice,
	 * com.spacehopperstudios.quickinvoice.shared.api.datatypes.Item) */
	@Override
	public Item updateInvoiceItem (Invoice invoice, Item item) {
		item.invoiceKey = Key.create(invoice);
		provide().save().entity(item).now();
		return item;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.invoice.
	 * IInvoiceService #
	 * getInvoiceItems(com.spacehopperstudios.quickinvoice.shared.api.datatypes.
	 * Invoice) */
	@Override
	public List<Item> getInvoiceItems (Invoice invoice) {
		return provide().load().type(Item.class).filter("invoiceKey", invoice)
				.order("date").list();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.invoice.
	 * IInvoiceService #getInvoiceByName(java.lang.String) */
	@Override
	public Invoice getNamedInvoice (String name) {
		Key<Invoice> key = provide().load().type(Invoice.class)
				.filter("name", name).limit(1).keys().first().now();
		return (key == null ? null : getInvoice(Long.valueOf(key.getId())));
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.invoice.
	 * IInvoiceService #
	 * getVendorOutstandingInvoicesCount(com.spacehopperstudios.quickinvoice.
	 * shared .api.datatypes.Vendor) */
	@Override
	public Integer getVendorOutstandingInvoicesCount (Vendor currentVendor) {
		return currentVendor.outstandingCount;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.invoice.
	 * IInvoiceService #
	 * getVendorOutstandingInvoices(com.spacehopperstudios.quickinvoice.shared.
	 * api .datatypes.Vendor, java.lang.Long, java.lang.Long,
	 * com.spacehopperstudios.quickinvoice.shared.api.datatypes.InvoiceSortType,
	 * com.spacehopperstudios.quickinvoice.shared.api.SortDirectionType) */
	@Override
	public List<Invoice> getVendorOutstandingInvoices (Vendor vendor,
			Long start, Long count, InvoiceSortType sortBy,
			SortDirectionType sortDirection) {
		Query<Invoice> query = provide().load().type(Invoice.class)
				.filter("vendorKey", vendor);

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

		List<Invoice> invoices = query.list();

		ICustomerService customerService = CustomerServiceProvider.provide();
		ICurrencyService currencyService = CurrencyServiceProvider.provide();
		for (Invoice invoice : invoices) {
			invoice.customer = customerService
					.getCustomer(Long.valueOf(invoice.customerKey.getId()));
			invoice.currency = currencyService
					.getCurrency(Long.valueOf(invoice.currencyKey.getId()));
		}

		return invoices;
	}

}