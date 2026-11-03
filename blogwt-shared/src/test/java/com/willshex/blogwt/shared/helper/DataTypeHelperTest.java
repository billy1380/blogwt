//
//  DataTypeHelperTest.java
//  blogwt
//
//  Copyright © 2026 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.helper;

import org.junit.Assert;
import org.junit.Test;

import com.googlecode.objectify.Key;
import com.willshex.blogwt.shared.api.datatype.User;

/**
 * An id is a boxed <code>Long</code>, and two of the same value over 127 are
 * different objects, which is what a download's owner check once compared.
 * 
 * @author William Shakour (billy1380)
 *
 */
public class DataTypeHelperTest {

	private static Key<User> user (long id) {
		return Key.create(com.google.cloud.datastore.Key
				.newBuilder("test-project", "User", id).build());
	}

	@Test
	public void testTheSameLargeIdIsTheSameKey () {
		long id = 5069036098420736L;

		Assert.assertNotSame(user(id).getId(), user(id).getId());
		Assert.assertTrue(DataTypeHelper.same(user(id), user(id)));
	}

	@Test
	public void testDifferentIdsAreDifferentKeys () {
		Assert.assertFalse(DataTypeHelper.same(user(5069036098420736L),
				user(5192746214096896L)));
	}

	@Test
	public void testMissingKeysAreNeverTheSame () {
		Assert.assertFalse(DataTypeHelper.same((Key<User>) null, user(1)));
		Assert.assertFalse(DataTypeHelper.same(user(1), (Key<User>) null));
		Assert.assertFalse(DataTypeHelper.same((Key<User>) null, (Key<User>) null));
	}

	@Test
	public void testAKeyIsTheSameAsTheItemWithItsId () {
		User item = new User();
		item.id = Long.valueOf(5069036098420736L);

		Assert.assertTrue(DataTypeHelper.same(user(5069036098420736L), item));
		Assert.assertFalse(DataTypeHelper.same(user(1), item));
	}
}
