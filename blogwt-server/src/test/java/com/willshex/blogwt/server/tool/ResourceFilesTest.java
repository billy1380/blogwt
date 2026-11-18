package com.willshex.blogwt.server.tool;

import org.junit.Assert;
import org.junit.Test;

public class ResourceFilesTest {

	private static ResourceFiles.Fetch of (byte[] file) {
		return (start, end) -> {
			if (start >= file.length) throw new IllegalArgumentException();

			int to = (int) Math.min(end + 1, file.length);

			return java.util.Arrays.copyOfRange(file, (int) start, to);
		};
	}

	@Test
	public void testAFileInSeveralRangesIsJoinedWhole () {
		byte[] file = new byte[25];
		for (int i = 0; i < file.length; i++) file[i] = (byte) i;

		Assert.assertArrayEquals(file,
				ResourceFiles.assemble(of(file), 10, 1000));
	}

	@Test
	public void testAFileThatFillsTheLastRangeEndsOnTheMissingOne () {
		byte[] file = new byte[20];

		Assert.assertEquals(20,
				ResourceFiles.assemble(of(file), 10, 1000).length);
	}

	@Test
	public void testNothingToReadIsNull () {
		Assert.assertNull(ResourceFiles.assemble(of(new byte[0]), 10, 1000));
	}

	@Test
	public void testAHugeFileStopsAtTheLimit () {
		byte[] huge = new byte[100];

		Assert.assertEquals(30,
				ResourceFiles.assemble(of(huge), 10, 30).length);
	}

}
