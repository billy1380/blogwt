//
//  Users.java
//  bidly
//
//  Created by William Shakour (billy1380) on 3 Jan 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.bidly.dummy.data;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.helper.DataTypeHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public interface Users {

	public static final List<User> ALL = Arrays.asList(
			(User) new User().forename("Jeremiah").surname("Edwards").id(1L),
			(User) new User().forename("Ida").surname("Kuhn").id(2L),
			(User) new User().forename("Dennis").surname("Palmer").id(3L),
			(User) new User().forename("Willard").surname("Payne").id(4L),
			(User) new User().forename("Brooklyn").surname("Walker").id(5L),
			(User) new User().forename("Bonnie").surname("Castillo").id(6L),
			(User) new User().forename("Caleb").surname("Barnes").id(7L),
			(User) new User().forename("Hazel").surname("Baker").id(8L),
			(User) new User().forename("Dana").surname("Banks").id(9L),
			(User) new User().forename("Jared").surname("Carter").id(10L));

	public static final Map<Long, User> LOOKUP = DataTypeHelper.map(ALL);

}
