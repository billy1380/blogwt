//
//  Offers.java
//  bidly
//
//  Created by William Shakour (billy1380) on 27 Dec 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.bidly.dummy.data;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.willshex.blogwt.client.page.bidly.dummy.datatypes.Offer;
import com.willshex.blogwt.shared.api.datatype.bidly.Address;
import com.willshex.blogwt.shared.helper.DataTypeHelper;
import com.willshex.blogwt.shared.helper.DateTimeHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public interface Offers {

	public static final List<Offer> ALL = Arrays.asList(
			(Offer) new Offer().user(Users.LOOKUP.get(1L))
					.address(new Address().postcode("CV31 1NU")).id(1L)
					.created(DateTimeHelper.now()),
			(Offer) new Offer().user(Users.LOOKUP.get(2L))
					.address(new Address().postcode("IV45 8RJ")).id(2L)
					.created(DateTimeHelper.now()),
			(Offer) new Offer().user(Users.LOOKUP.get(3L))
					.address(new Address().postcode("RM2 5EL")).id(3L)
					.created(DateTimeHelper.now()),
			(Offer) new Offer().user(Users.LOOKUP.get(4L))
					.address(new Address().postcode("NN4 5BB")).id(4L)
					.created(DateTimeHelper.now()),
			(Offer) new Offer().user(Users.LOOKUP.get(5L))
					.address(new Address().postcode("WF15 6JR")).id(5L)
					.created(DateTimeHelper.now()),
			(Offer) new Offer().user(Users.LOOKUP.get(6L))
					.address(new Address().postcode("EC2R 7BP")).id(6L)
					.created(DateTimeHelper.now()),
			(Offer) new Offer().user(Users.LOOKUP.get(7L))
					.address(new Address().postcode("LL42 1LS")).id(7L)
					.created(DateTimeHelper.now()),
			(Offer) new Offer().user(Users.LOOKUP.get(8L))
					.address(new Address().postcode("DY11 7DQ")).id(8L)
					.created(DateTimeHelper.now()),
			(Offer) new Offer().user(Users.LOOKUP.get(9L))
					.address(new Address().postcode("L23 2RF")).id(9L)
					.created(DateTimeHelper.now()),
			(Offer) new Offer().user(Users.LOOKUP.get(10L))
					.address(new Address().postcode("CM98 1AR")).id(10L)
					.created(DateTimeHelper.now()));

	public static final Map<Long, Offer> LOOKUP = DataTypeHelper.map(ALL);

	public static final List<Offer> NEW = Arrays.asList(ALL.get(0), ALL.get(5),
			ALL.get(9));

	public static final List<Offer> ACCEPTED = Arrays.asList(ALL.get(2),
			ALL.get(3), ALL.get(6), ALL.get(8));

	public static final List<Offer> EXPIRED = Arrays.asList(ALL.get(1));

	public static final List<Offer> NEGOTIATING = Arrays.asList(ALL.get(4),
			ALL.get(7));

	public static final List<Offer> AEN = Stream
			.concat(ACCEPTED.stream(),
					Stream.concat(EXPIRED.stream(), NEGOTIATING.stream()))
			.sorted(Offers::compareCreated).collect(Collectors.toList());

	public static final List<Offer> USED_NEGOTIATING = Arrays.asList(
			ALL.get(0).vehicle(Garage.ALL.get(0)),
			ALL.get(5).vehicle(Garage.ALL.get(3)));

	public static final List<Offer> USED_ACCEPTED = Arrays
			.asList(ALL.get(9).vehicle(Garage.ALL.get(4)));

	public static final List<Offer> UEN = Stream
			.concat(USED_ACCEPTED.stream(), USED_NEGOTIATING.stream())
			.sorted(Offers::compareCreated).collect(Collectors.toList());

	public static int compareCreated (Offer c1, Offer c2) {
		return c1.created.before(c2.created) ? 1
				: c1.created.after(c2.created) ? -1 : 0;
	}

}
