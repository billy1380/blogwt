package com.willshex.blogwt.server.helper;

import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.willshex.blogwt.server.service.post.IPostService;
import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.PostContent;

/**
 * Expands {@code <?post id="..." ?>} processing instructions in a post's body
 * by splicing in the referenced post's body. Only posts by the same author are
 * expanded; missing posts, other authors, invalid ids, cycles and depths past
 * {@link #MAX_DEPTH} are stripped. Directives inside fenced code blocks or
 * inline code spans are left untouched.
 */
public final class PostEmbedHelper {
	public static final int MAX_DEPTH = 5;

	private static final Pattern ID_ATTR = Pattern.compile(
			"(?i)(?:^|\\s)id\\s*=\\s*(?:\"(\\d+)\"|'(\\d+)'|(\\d+))(?=\\s|$)");

	private PostEmbedHelper () {
	}

	/** Returns the expanded body of {@code post} without mutating it. */
	public static String expand (Post post) {
		return expand(post, serviceLoader(new HashMap<Long, Post>()));
	}

	/** Replaces {@code post.content} with a copy holding the expanded body. */
	public static void expandInPlace (Post post) {
		expandInPlace(post, serviceLoader(new HashMap<Long, Post>()));
	}

	/** Expands every post in {@code posts}, sharing one lookup cache. */
	public static void expandInPlace (Collection<Post> posts) {
		if (posts == null || posts.isEmpty()) {
			return;
		}

		Function<Long, Post> loader = serviceLoader(new HashMap<Long, Post>());
		for (Post post : posts) {
			expandInPlace(post, loader);
		}
	}

	static void expandInPlace (Post post, Function<Long, Post> loader) {
		if (post == null || post.content == null || post.content.body == null) {
			return;
		}

		String original = post.content.body;
		String expanded = expand(post, loader);
		if (!original.equals(expanded)) {
			PostContent copy = new PostContent();
			copy.id = post.content.id;
			copy.created = post.content.created;
			copy.postKey = post.content.postKey;
			copy.body = expanded;
			post.content = copy;
		}
	}

	static String expand (Post post, Function<Long, Post> loader) {
		if (post == null || post.content == null || post.content.body == null) {
			return post != null && post.content != null ? post.content.body
					: null;
		}

		Long authorId = authorIdOf(post);
		Set<Long> ancestors = new HashSet<Long>();
		if (post.id != null) {
			ancestors.add(post.id);
		}

		return expandBody(post.content.body, authorId, loader, ancestors, 0);
	}

	private static Function<Long, Post> serviceLoader (Map<Long, Post> cache) {
		return (Long id) -> {
			if (cache.containsKey(id)) {
				return cache.get(id);
			}

			IPostService service = PostServiceProvider.provide();
			Post loaded = service.getPost(id);
			if (loaded != null && loaded.contentKey != null
					&& loaded.content == null) {
				loaded.content = service.getPostContent(loaded);
			}
			cache.put(id, loaded);
			return loaded;
		};
	}

	private static Long authorIdOf (Post post) {
		if (post == null) {
			return null;
		}
		if (post.authorKey != null) {
			return keyToId(post.authorKey);
		}
		return post.author != null ? post.author.id : null;
	}

	private static String expandBody (String text, Long authorId,
			Function<Long, Post> loader, Set<Long> ancestors, int depth) {
		if (text == null || text.indexOf("<?") == -1) {
			return text;
		}

		StringBuilder out = new StringBuilder(text.length());
		int n = text.length();
		int i = 0;
		char fenceChar = 0;
		int fenceLen = 0;

		while (i < n) {
			boolean lineStart = (i == 0 || text.charAt(i - 1) == '\n');

			if (lineStart) {
				int lineEnd = text.indexOf('\n', i);
				int end = lineEnd == -1 ? n : lineEnd;
				int next = lineEnd == -1 ? n : lineEnd + 1;

				if (fenceChar != 0) {
					out.append(text, i, next);
					if (isClosingFence(text, i, end, fenceChar, fenceLen)) {
						fenceChar = 0;
						fenceLen = 0;
					}
					i = next;
					continue;
				}

				int[] fence = openingFence(text, i, end);
				if (fence != null) {
					fenceChar = (char) fence[0];
					fenceLen = fence[1];
					out.append(text, i, next);
					i = next;
					continue;
				}
			}

			char c = text.charAt(i);

			if (c == '`') {
				int ticks = countChar(text, i, '`');
				int close = findClosingTicks(text, i + ticks, ticks);
				if (close != -1) {
					out.append(text, i, close + ticks);
					i = close + ticks;
				} else {
					out.append(text, i, i + ticks);
					i += ticks;
				}
				continue;
			}

			if (c == '<' && startsPostDirective(text, i)) {
				int close = findDirectiveEnd(text, i + 6);
				if (close != -1) {
					String attrs = text.substring(i + 6, close);
					Long targetId = parseId(attrs);

					if (targetId != null && authorId != null
							&& depth < MAX_DEPTH
							&& !ancestors.contains(targetId)) {
						Post target = loader.apply(targetId);
						if (target != null && target.content != null
								&& target.content.body != null
								&& authorId.equals(authorIdOf(target))) {
							ancestors.add(targetId);
							out.append(expandBody(target.content.body, authorId,
									loader, ancestors, depth + 1));
							ancestors.remove(targetId);
						}
					}

					i = close + 2;
					continue;
				}
			}

			out.append(c);
			i++;
		}

		return out.toString();
	}

	private static int[] openingFence (String text, int start, int end) {
		int p = start;
		int spaces = 0;
		while (p < end && text.charAt(p) == ' ' && spaces < 3) {
			p++;
			spaces++;
		}
		if (p >= end) {
			return null;
		}
		char ch = text.charAt(p);
		if (ch != '`' && ch != '~') {
			return null;
		}
		int len = 0;
		while (p + len < end && text.charAt(p + len) == ch) {
			len++;
		}
		if (len < 3) {
			return null;
		}
		if (ch == '`') {
			for (int k = p + len; k < end; k++) {
				if (text.charAt(k) == '`') {
					return null;
				}
			}
		}
		return new int[] { ch, len };
	}

	private static boolean isClosingFence (String text, int start, int end,
			char fenceChar, int minLen) {
		int p = start;
		int spaces = 0;
		while (p < end && text.charAt(p) == ' ' && spaces < 3) {
			p++;
			spaces++;
		}
		int len = 0;
		while (p + len < end && text.charAt(p + len) == fenceChar) {
			len++;
		}
		if (len < minLen) {
			return false;
		}
		for (int k = p + len; k < end; k++) {
			char ch = text.charAt(k);
			if (ch != ' ' && ch != '\t' && ch != '\r') {
				return false;
			}
		}
		return true;
	}

	private static int countChar (String text, int start, char ch) {
		int n = text.length();
		int len = 0;
		while (start + len < n && text.charAt(start + len) == ch) {
			len++;
		}
		return len;
	}

	private static int findClosingTicks (String text, int from, int ticks) {
		int n = text.length();
		int i = from;
		while (i < n) {
			if (text.charAt(i) == '`') {
				int run = countChar(text, i, '`');
				if (run == ticks) {
					return i;
				}
				i += run;
			} else {
				i++;
			}
		}
		return -1;
	}

	private static boolean startsPostDirective (String text, int index) {
		if (index + 6 > text.length()) {
			return false;
		}
		if (!text.regionMatches(true, index, "<?post", 0, 6)) {
			return false;
		}
		if (index + 6 == text.length()) {
			return false;
		}
		char next = text.charAt(index + 6);
		return Character.isWhitespace(next) || next == '?';
	}

	private static int findDirectiveEnd (String text, int from) {
		int n = text.length();
		char quote = 0;
		for (int i = from; i < n - 1; i++) {
			char ch = text.charAt(i);
			if (quote != 0) {
				if (ch == quote) {
					quote = 0;
				}
			} else if (ch == '"' || ch == '\'') {
				quote = ch;
			} else if (ch == '?' && text.charAt(i + 1) == '>') {
				return i;
			}
		}
		return -1;
	}

	private static Long parseId (String attrs) {
		Matcher m = ID_ATTR.matcher(attrs);
		if (!m.find()) {
			return null;
		}
		String digits = m.group(1) != null ? m.group(1)
				: (m.group(2) != null ? m.group(2) : m.group(3));
		try {
			return Long.valueOf(digits);
		} catch (NumberFormatException ex) {
			return null;
		}
	}
}
