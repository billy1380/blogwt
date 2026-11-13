package com.willshex.blogwt.server.service.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PersistenceServiceProviderTest {
	@Test
	public void unsetOrDefaultDatabaseIdUsesTheDefaultDatabase () {
		assertNull(PersistenceServiceProvider.databaseId(null));
		assertNull(PersistenceServiceProvider.databaseId("  "));
		assertNull(PersistenceServiceProvider.databaseId("(default)"));
	}

	@Test
	public void namedDatabaseIdIsTrimmed () {
		assertEquals("test", PersistenceServiceProvider.databaseId(" test "));
	}
}
