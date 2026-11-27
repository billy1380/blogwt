package com.willshex.blogwt.server.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.PostContent;
import com.willshex.blogwt.shared.api.datatype.User;

public class PostEmbedHelperTest {

	private static Post post (long id, long authorId, String body) {
		Post p = new Post();
		p.id = id;
		p.author = new User();
		p.author.id = authorId;
		p.content = new PostContent();
		p.content.id = id * 10;
		p.content.body = body;
		return p;
	}

	@Test
	public void expandsSameAuthorAndSupportsDrafts () {
		Map<Long, Post> posts = new HashMap<>();
		Post main = post(1L, 100L,
				"Intro\n\n<?post id=\"2\" ?>\n\n<?post id='3'?>\n\n<?post id=2 ?>");
		posts.put(1L, main);
		posts.put(2L, post(2L, 100L, "Shared snippet"));
		posts.put(3L, post(3L, 100L, "Third part"));

		String result = PostEmbedHelper.expand(main, posts::get);

		assertEquals("Intro\n\nShared snippet\n\nThird part\n\nShared snippet",
				result);
	}

	@Test
	public void stripsOtherAuthorMissingAndInvalidDirectives () {
		Map<Long, Post> posts = new HashMap<>();
		Post main = post(1L, 100L,
				"A<?post id=\"2\" ?>B<?post id=\"999\" ?>C<?post id=\"abc\" ?>D<?post ?>E");
		posts.put(1L, main);
		posts.put(2L, post(2L, 200L, "Secret other author"));

		assertEquals("ABCDE", PostEmbedHelper.expand(main, posts::get));
	}

	@Test
	public void handlesNestedAndStopsCyclesAndDepth () {
		Map<Long, Post> posts = new HashMap<>();
		Post a = post(1L, 100L, "1(<?post id=\"2\" ?>|<?post id=\"1\" ?>)");
		Post b = post(2L, 100L, "2(<?post id=\"3\" ?>|<?post id=\"1\" ?>)");
		Post c = post(3L, 100L, "3");
		posts.put(1L, a);
		posts.put(2L, b);
		posts.put(3L, c);

		assertEquals("1(2(3|)|)", PostEmbedHelper.expand(a, posts::get));

		Map<Long, Post> deep = new HashMap<>();
		for (long i = 1; i <= 8; i++) {
			deep.put(i, post(i, 100L,
					i + "[<?post id=\"" + (i + 1) + "\" ?>]"));
		}
		assertEquals("1[2[3[4[5[6[]]]]]]",
				PostEmbedHelper.expand(deep.get(1L), deep::get));
	}

	@Test
	public void preservesDirectivesInsideCodeBlocksAndInlineCode () {
		Map<Long, Post> posts = new HashMap<>();
		String body = "Use `<?post id=\"2\" ?>` inline.\n\n"
				+ "```html\n<?post id=\"2\" ?>\n```\n\n"
				+ "~~~\n<?post id=\"2\" ?>\n~~~\n\n"
				+ "Live: <?post id=\"2\" ?>";
		Post main = post(1L, 100L, body);
		posts.put(1L, main);
		posts.put(2L, post(2L, 100L, "EXPANDED"));

		String expected = "Use `<?post id=\"2\" ?>` inline.\n\n"
				+ "```html\n<?post id=\"2\" ?>\n```\n\n"
				+ "~~~\n<?post id=\"2\" ?>\n~~~\n\n"
				+ "Live: EXPANDED";
		assertEquals(expected, PostEmbedHelper.expand(main, posts::get));
	}

	@Test
	public void expandInPlaceDoesNotMutateOriginalContentInstance () {
		Map<Long, Post> posts = new HashMap<>();
		Post main = post(1L, 100L, "Before <?post id=\"2\" ?>");
		PostContent originalContent = main.content;
		posts.put(1L, main);
		posts.put(2L, post(2L, 100L, "After"));

		PostEmbedHelper.expandInPlace(main, posts::get);

		assertNotSame(originalContent, main.content);
		assertEquals("Before <?post id=\"2\" ?>", originalContent.body);
		assertEquals("Before After", main.content.body);
	}
}
