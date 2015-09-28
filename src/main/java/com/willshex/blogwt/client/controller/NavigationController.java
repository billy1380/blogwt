//
//  NavigationController.java
//  blogwt
//
//  Created by billy1380 on 14 Aug 2013.
//  Copyright © 2013 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.client.controller;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.PageTypeHelper;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.blogwt.shared.page.Stack;

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
	 * 
	 * @param value
	 * @return
	 */
	public HTMLPanel setPageHolder (HTMLPanel value) {
		return pageHolder = value;
	}

	public static final String ADD_ACTION_PARAMETER_VALUE = "add";
	public static final String EDIT_ACTION_PARAMETER_VALUE = "edit";
	public static final String DELETE_ACTION_PARAMETER_VALUE = "delete";
	public static final String VIEW_ACTION_PARAMETER_VALUE = "view";

	private Map<String, Composite> pages = new HashMap<String, Composite>();

	private Stack stack;

	private String intended = null;

	private void attachPage (PageType type) {
		Composite page = null;

		if ((page = pages.get(type.toString())) == null) {
			pages.put(type.toString(), page = PageTypeHelper.createPage(type));
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
		value = (value == null || value.trim().length() == 0 || value
				.replace("!", "").trim().length() == 0) ? PageController.get()
				.homePageTargetHistoryToken() : value;
		Stack s = Stack.parse(value);
		PageType p = s == null ? null : PageType.fromString(s.getPage());

		if (PropertyController.get().blog() == null
				&& p != PageType.SetupBlogPageType) {
			PageTypeHelper.show(PageType.SetupBlogPageType);
		} else {
			if (PropertyController.get().blog() != null
					&& p == PageType.SetupBlogPageType) {
				PageTypeHelper.show(PageController.get()
						.homePageTargetHistoryToken());
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
				PageTypeHelper.show(PageController.get()
						.homePageTargetHistoryToken());
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
			intended = PageController.get().homePageSlug();
		}

		addPage(intended);
	}

	public void setLastIntendedPage (Stack value) {
		intended = value.toString();
	}

	public void showNext () {
		if (stack.hasNext()) {
			PageTypeHelper.show(PageType.fromString(stack.getNext().getPage()),
					stack.getNext().toString(1));
		} else {
			PageTypeHelper.show(PageController.get()
					.homePageTargetHistoryToken());
		}

	}

	public void showPrevious () {
		if (stack.hasPrevious()) {
			PageTypeHelper.show(PageType.fromString(stack.getPrevious()
					.getPage()), stack.getPrevious().toString(1));
		} else {
			PageTypeHelper.show(PageController.get()
					.homePageTargetHistoryToken());
		}
	}

	/**
	 * Purges all pages
	 */
	public void purgeAllPages () {
		pages.clear();
	}
}
