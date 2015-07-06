//
//  NavigationController.java
//  blogwt
//
//  Created by billy1380 on 14 Aug 2013.
//  Copyright © 2013 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.client.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.spacehopperstudios.utility.StringUtils;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.part.FooterPart;
import com.willshex.blogwt.client.part.HeaderPart;

/**
 * @author billy1380
 * 
 */
public class NavigationController implements ValueChangeHandler<String> {
	private static NavigationController one = null;

	private HTMLPanel pageHolder = null;

	/**
	 * @return
	 */
	public static NavigationController get () {
		if (one == null) {
			one = new NavigationController();
		}

		return one;
	}

	/**
	 * @return
	 */
	public Widget getPageHolder () {
		if (pageHolder == null) {
			pageHolder = new HTMLPanel("<!-- pages -->");
		}

		return pageHolder;
	}

	public static final String ADD_ACTION_PARAMETER_VALUE = "add";
	public static final String EDIT_ACTION_PARAMETER_VALUE = "edit";
	public static final String DELETE_ACTION_PARAMETER_VALUE = "delete";
	public static final String VIEW_ACTION_PARAMETER_VALUE = "view";

	private Map<String, Composite> pages = new HashMap<String, Composite>();

	private HeaderPart header = null;
	private FooterPart footer = null;

	private Stack stack;

	private String intended = null;

	public static class Stack {
		public static final String NEXT_KEY = "nextgoto=";
		public static final String PREVIOUS_KEY = "prevgoto=";

		private String allParts;
		private String[] parts;

		private Stack previous;
		private Stack next;

		private Stack (String value) {
			if (value != null) {
				allParts = value;
				parts = allParts.split("/");

				for (String part : parts) {
					String[] parameters;
					if (next == null
							&& (parameters = Stack.decode(NEXT_KEY, part)) != null) {
						next = new Stack(StringUtils.join(
								Arrays.asList(parameters), "/"));
					} else if (previous == null
							&& (parameters = Stack.decode(PREVIOUS_KEY, part)) != null) {
						previous = new Stack(StringUtils.join(
								Arrays.asList(parameters), "/"));
					}
				}
			}
		}

		public String getPage () {
			return parts.length > 0 ? parts[0] : null;
		}

		public String getPageSlug () {
			String slug = null;
			if (parts.length > 0) {
				if (parts[0] != null && parts[0].length() > 0
						&& parts[0].charAt(0) == '!') {
					slug = parts[0].substring(1);
				}
			}

			return slug;
		}

		public String getAction () {
			return parts.length > 1 ? parts[1] : null;
		}

		public String getParameter (int index) {
			return parts.length > (2 + index) ? parts[2 + index] : null;
		}

		public static Stack parse (String value) {
			return new Stack(value);
		}

		/**
		 * @return
		 */
		public boolean hasAction () {
			return getAction() != null;
		}

		public boolean hasPage () {
			return getPage() != null;
		}

		@Override
		public String toString () {
			return allParts;
		}

		public String toString (int fromPart) {
			return parts == null ? "" : StringUtils.join(Arrays.asList(parts)
					.subList(fromPart, parts.length), "/");
		}

		public String toString (String... param) {
			return toString() + "/"
					+ StringUtils.join(Arrays.asList(param), "/");
		}

		public String toString (int fromPart, String... param) {
			return toString(fromPart) + "/"
					+ StringUtils.join(Arrays.asList(param), "/");
		}

		public String asParameter () {
			return Stack.encode(null, allParts);
		}

		public String asNextParameter () {
			return Stack.encode(NEXT_KEY, allParts);
		}

		public String asPreviousParameter () {
			return Stack.encode(PREVIOUS_KEY, allParts);
		}

		/**
		 * @return
		 */
		public boolean hasNext () {
			return next != null;
		}

		/**
		 * @return
		 */
		public Stack getNext () {
			return next;
		}

		/**
		 * @return
		 */
		public boolean hasPrevious () {
			return previous != null;
		}

		/**
		 * @return
		 */
		public Stack getPrevious () {
			return previous;
		}

		public static String encode (String name, String... values) {
			String parameters = "";

			if (values != null && values.length > 0) {
				parameters = StringUtils.join(Arrays.asList(values), "/");
			}

			return (name == null || name.length() == 0) ? parameters
					: (name + parameters);
		}

		public static String[] decode (String name, String encoded) {
			String content;
			String[] splitContent = null;

			if (encoded != null && encoded.length() > 0
					&& encoded.startsWith(name)) {
				content = encoded.substring(name.length());

				if (content != null && content.length() > 0) {
					splitContent = content.split("/");
				}
			}

			return splitContent;
		}

		public int getParameterCount () {
			int count = parts.length - 2;
			return count > 0 ? count : 0;
		}
	}

	private void attachPage (PageType type) {
		Composite page = null;

		if ((page = pages.get(type.toString())) == null) {
			pages.put(type.toString(), page = type.create());
		}

		if (!page.isAttached()) {
			pageHolder.clear();
			pageHolder.add(page);
		}
	}

	/**
	 * @param value
	 */
	public void addPage (String value) {
		Stack s = Stack.parse(value);
		PageType p = s == null ? null : PageType.fromString(s.getPage());

		if (PropertyController.get().blog() == null
				&& p != PageType.SetupBlogPageType) {
			PageType.SetupBlogPageType.show();
		} else {
			if (PropertyController.get().blog() != null
					&& p == PageType.SetupBlogPageType) {
				PageType.PostsPageType.show();
			} else if (p != null && p.requiresLogin()
					&& !SessionController.get().isValidSession()) {
				SessionController.get().logout(PageType.LoginPageType,
						s.asNextParameter());
			} else if (p != null
					&& p.requiresLogin()
					&& p.getRequiredPermissions() != null
					&& p.getRequiredPermissions().size() > 0
					&& !SessionController.get().isAuthorised(
							p.getRequiredPermissions())) {
				PageType.PostsPageType.show();
			} else {
				if (intended != null && intended.equals(s.toString())) {
					intended = null;
				}

				addStack(s);
			}
		}
	}

	private void addStack (Stack value) {
		String page = value.getPage();

		PageType stackPage = PageType.fromString(page);

		if (stackPage == null) {
			stackPage = PageType.PageDetailPageType;
		}

		final Stack previous = stack;
		stack = value;

		attachPage(stackPage);

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute () {
				DefaultEventBus
						.get()
						.fireEventFromSource(
								new NavigationChangedEventHandler.NavigationChangedEvent(
										previous, stack),
								NavigationController.this);
			}
		});
	}

	/**
	 * @return
	 */
	public Widget getHeader () {
		if (header == null) {
			header = new HeaderPart();
		}
		return header;
	}

	/**
	 * @return
	 */
	public Widget getFooter () {
		if (footer == null) {
			footer = new FooterPart();
		}
		return footer;
	}

	public PageType getCurrentPage () {
		PageType p = null;
		if (stack != null) {
			p = PageType.fromString(stack.getPage());
		}
		return p;
	}

	public Stack getStack () {
		return stack;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.logical.shared.ValueChangeHandler#onValueChange(
	 * com.google.gwt.event.logical.shared.ValueChangeEvent) */
	@Override
	public void onValueChange (ValueChangeEvent<String> event) {
		addPage(event.getValue());
	}

	public void showIntendedPage () {
		if (intended == null) {
			intended = PageType.PostsPageType.toString();
		}

		addPage(intended);
	}

	public void setLastIntendedPage (Stack value) {
		intended = value.toString();
	}

	public void showNext () {
		if (stack.hasNext()) {
			PageType.fromString(stack.getNext().getPage()).show(
					stack.getNext().toString(1));
		} else {
			PageType.PostsPageType.show();
		}

	}

	public void showPrevious () {
		if (stack.hasPrevious()) {
			PageType.fromString(stack.getPrevious().getPage()).show(
					stack.getPrevious().toString(1));
		} else {
			PageType.PostsPageType.show();
		}
	}

	/**
	 * Purges all pages
	 */
	public void purgeAllPages () {
		pages.clear();
	}
}
