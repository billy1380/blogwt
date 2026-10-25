//
//  ReactionHelper.java
//  blogwt
//
//  Created by William Shakour on October 24, 2026.
//  Copyright © 2026 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.shared.helper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ReactionHelper {

	/**
	 * Offered by the clients as one tap reactions, any emoji is accepted
	 */
	public static final List<String> QUICK_EMOTICONS = Collections
			.unmodifiableList(Arrays.asList("👍", "❤️",
					"😂", "😮", "😢",
					"🎉"));

	public static final int MAX_EMOTICON_LENGTH = 24;

	private static final int ZERO_WIDTH_JOINER = 0x200D;
	private static final int VARIATION_SELECTOR_16 = 0xFE0F;
	private static final int COMBINING_KEYCAP = 0x20E3;

	private ReactionHelper () {}

	/**
	 * True when the text is exactly one emoji: a single symbol, a flag, or a
	 * sequence joined with zero width joiners, with any variation selector,
	 * skin tone or tag characters
	 */
	public static boolean isEmoticon (String emoticon) {
		if (emoticon == null || emoticon.length() == 0
				|| emoticon.length() > MAX_EMOTICON_LENGTH) return false;

		if (isKeycap(emoticon)) return true;

		int clusters = 0;
		int previous = -1;
		int regionalIndicators = 0;

		int i = 0;
		while (i < emoticon.length()) {
			int codePoint = emoticon.charAt(i);
			i++;

			if (codePoint >= 0xD800 && codePoint <= 0xDBFF
					&& i < emoticon.length()) {
				int low = emoticon.charAt(i);

				if (low >= 0xDC00 && low <= 0xDFFF) {
					codePoint = ((codePoint - 0xD800) << 10) + (low - 0xDC00)
							+ 0x10000;
					i++;
				}
			}

			if (isModifier(codePoint)) {
				if (previous == -1) return false;
			} else if (isEmojiBase(codePoint)) {
				boolean joined = previous == ZERO_WIDTH_JOINER;
				boolean flagPair = isRegionalIndicator(codePoint)
						&& isRegionalIndicator(previous)
						&& regionalIndicators == 1;

				if (!joined && !flagPair) {
					clusters++;
				}

				regionalIndicators = isRegionalIndicator(codePoint)
						? regionalIndicators + 1 : 0;
			} else {
				return false;
			}

			previous = codePoint;
		}

		return clusters == 1;
	}

	/**
	 * A digit, hash or star, an optional variation selector and the combining
	 * keycap
	 */
	private static boolean isKeycap (String emoticon) {
		char base = emoticon.charAt(0);

		if (!(base == '#' || base == '*' || (base >= '0' && base <= '9')))
			return false;

		int length = emoticon.length();

		return (length == 2 && emoticon.charAt(1) == COMBINING_KEYCAP)
				|| (length == 3 && emoticon.charAt(1) == VARIATION_SELECTOR_16
						&& emoticon.charAt(2) == COMBINING_KEYCAP);
	}

	private static boolean isModifier (int codePoint) {
		return codePoint == ZERO_WIDTH_JOINER
				|| codePoint == VARIATION_SELECTOR_16
				|| codePoint == COMBINING_KEYCAP
				|| (codePoint >= 0x1F3FB && codePoint <= 0x1F3FF)
				|| (codePoint >= 0xE0020 && codePoint <= 0xE007F);
	}

	private static boolean isRegionalIndicator (int codePoint) {
		return codePoint >= 0x1F1E6 && codePoint <= 0x1F1FF;
	}

	private static boolean isEmojiBase (int codePoint) {
		return (codePoint >= 0x1F000 && codePoint <= 0x1FAFF)
				|| (codePoint >= 0x2190 && codePoint <= 0x21FF)
				|| (codePoint >= 0x2300 && codePoint <= 0x23FF)
				|| (codePoint >= 0x25A0 && codePoint <= 0x25FF)
				|| (codePoint >= 0x2600 && codePoint <= 0x27BF)
				|| (codePoint >= 0x2900 && codePoint <= 0x297F)
				|| (codePoint >= 0x2B00 && codePoint <= 0x2BFF)
				|| codePoint == 0x00A9 || codePoint == 0x00AE
				|| codePoint == 0x24C2
				|| codePoint == 0x203C || codePoint == 0x2049
				|| codePoint == 0x2122 || codePoint == 0x2139
				|| codePoint == 0x3030 || codePoint == 0x303D
				|| codePoint == 0x3297 || codePoint == 0x3299;
	}

}
