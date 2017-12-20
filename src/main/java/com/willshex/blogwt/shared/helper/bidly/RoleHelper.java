//
//  RoleHelper.java
//  bidly
//
//  Created by William Shakour (billy1380) on 20 Dec 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.helper.bidly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.willshex.blogwt.shared.api.datatype.Role;

/**
 * @author William Shakour (billy1380)
 *
 */
public class RoleHelper extends com.willshex.blogwt.shared.helper.RoleHelper {

	public static final String SALES = "SLS";
	public static final String MANAGER = "MGR";
	public static final String DIRECTOR = "DIR";

	public static final String SALES_NAME = "Sales person";
	public static final String MANAGER_NAME = "Manager";
	public static final String DIRECTOR_NAME = "Director";

	public static final String SALES_DESCRIPTION = "Contains all permissions granted to sales people";
	public static final String MANAGER_DESCRIPTION = "Contains all permissions granted to managers";
	public static final String DIRECTOR_DESCRIPTION = "Contains all permissions granted to dealership directors";

	public static Role createSales () {
		return create(SALES);
	}

	public static Role createFullSales () {
		return createFull(SALES, SALES_NAME, SALES_DESCRIPTION);
	}

	public static Role createManager () {
		return create(MANAGER);
	}

	public static Role createFullManager () {
		return createFull(MANAGER, MANAGER_NAME, MANAGER_DESCRIPTION);
	}

	public static Role createDirector () {
		return create(DIRECTOR);
	}

	public static Role createFullDirector () {
		return createFull(DIRECTOR, DIRECTOR_NAME, DIRECTOR_DESCRIPTION);
	}

	public static Collection<Role> createAll () {
		List<Role> all = new ArrayList<Role>(
				com.willshex.blogwt.shared.helper.RoleHelper.createAll());

		all.add(createFullSales());
		all.add(createFullManager());
		all.add(createFullDirector());

		return all;
	}

}
