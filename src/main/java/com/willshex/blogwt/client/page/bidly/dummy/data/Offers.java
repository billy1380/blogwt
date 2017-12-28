//
//  Offers.java
//  bidly
//
//  Created by William Shakour (billy1380) on 27 Dec 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.bidly.dummy.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.willshex.blogwt.client.page.bidly.dummy.datatypes.Offer;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.datatype.bidly.Address;
import com.willshex.blogwt.shared.helper.DateTimeHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class Offers {

	private static final List<Offer> ALL = Arrays.asList(
			(Offer) new Offer()
					.user((User) new User().forename("Jeremiah")
							.surname("Edwards"))
					.address(new Address().postcode("CV31 1NU")).id(1L)
					.created(DateTimeHelper.now()),
			(Offer) new Offer()
					.user((User) new User().forename("Ida").surname("Kuhn"))
					.address(new Address().postcode("IV45 8RJ")).id(2L)
					.created(DateTimeHelper.now()),
			(Offer) new Offer()
					.user((User) new User().forename("Dennis")
							.surname("Palmer"))
					.address(new Address().postcode("RM2 5EL")).id(3L)
					.created(DateTimeHelper.now()),
			(Offer) new Offer()
					.user((User) new User().forename("Willard")
							.surname("Payne"))
					.address(new Address().postcode("NN4 5BB")).id(4L)
					.created(DateTimeHelper.now()),
			(Offer) new Offer()
					.user((User) new User().forename("Brooklyn")
							.surname("Walker"))
					.address(new Address().postcode("WF15 6JR")).id(5L)
					.created(DateTimeHelper.now()),
			(Offer) new Offer()
					.user((User) new User().forename("Bonnie")
							.surname("Castillo"))
					.address(new Address().postcode("EC2R 7BP")).id(6L)
					.created(DateTimeHelper.now()),
			(Offer) new Offer()
					.user((User) new User().forename("Caleb").surname("Barnes"))
					.address(new Address().postcode("LL42 1LS")).id(7L)
					.created(DateTimeHelper.now()),
			(Offer) new Offer()
					.user((User) new User().forename("Hazel").surname("Baker"))
					.address(new Address().postcode("DY11 7DQ")).id(8L)
					.created(DateTimeHelper.now()),
			(Offer) new Offer()
					.user((User) new User().forename("Dana").surname("Banks"))
					.address(new Address().postcode("L23 2RF")).id(9L)
					.created(DateTimeHelper.now()));

	public static final List<Offer> NEW = Arrays.asList(ALL.get(0), ALL.get(5));

	public static final List<Offer> AEN = new ArrayList<>();

	public static final List<Offer> ACCEPTED = Arrays.asList(ALL.get(2),
			ALL.get(3), ALL.get(6), ALL.get(8));

	public static final List<Offer> EXPIRED = Arrays.asList(ALL.get(1));

	public static final List<Offer> NEGOTIATING = Arrays.asList(ALL.get(4),
			ALL.get(7));

	public static final List<Offer> EMPTY = Collections.emptyList();

	static {
		AEN.addAll(ACCEPTED);
		AEN.addAll(EXPIRED);
		AEN.addAll(NEGOTIATING);

		AEN.sort( (c1, c2) -> c1.created.compareTo(c2.created));
	}

}
