//
//  PagePlan.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 1 Aug 2013.
//  Copyright © 2013 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.wizard;

import java.util.ArrayList;
import java.util.List;

/**
 * @author William Shakour (billy1380)
 * 
 */
public class PagePlan {

	private String name = null;
	private List<WizardPage<?>> unmodifiablePages = new ArrayList<WizardPage<?>>();
	private List<WizardPage<?>> pages = new ArrayList<WizardPage<?>>();
	private List<PagePlanFinishedHandler> finishHandlers = new ArrayList<PagePlanFinishedHandler>();

	/**
	 * @return the name
	 */
	public String getName () {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName (String name) {
		this.name = name;
	}

	public static class PagePlanBuilder {
		PagePlan plan;

		public PagePlan build () {
			return plan;
		}

		public PagePlanBuilder addPage (WizardPage<?> page, boolean modifiable) {

			if (plan == null) {
				plan = new PagePlan();
			}

			if (modifiable) {
				plan.add(page);
			} else {
				plan.addUnmodifiable(page);
			}

			return this;
		}

		public PagePlanBuilder addPage (WizardPage<?> page) {
			addPage(page, false);
			return this;
		}

		public PagePlanBuilder setName (String name) {
			plan.name = name;

			return this;
		}

		/**
		 * @param wizardDialogFinishedHandler
		 * @return
		 */
		public PagePlanBuilder addFinishedHandler (
				PagePlanFinishedHandler handler) {
			plan.addFinishedHandler(handler);

			return this;
		}
	}

	public WizardPage<?> get (int index) {
		if (index > 0 && pages.get(index) instanceof DeferredWizardPage) {
			DeferredWizardPage<?> deferred = (DeferredWizardPage<?>) pages
					.get(index);
			List<WizardPage<?>> replacement = deferred.getPages(pages, index);

			int i = index;
			int j = findUnmodifiablePageIndex(index);

			pages.remove(i);
			unmodifiablePages.remove(j);

			if (replacement != null) {
				for (WizardPage<?> wizardPage : replacement) {
					addUnmodifiableAt(i++, j++, wizardPage);
				}
			}
		}

		return pages.get(index);
	}

	/**
	 * @param index
	 * @return
	 */
	private int findUnmodifiablePageIndex (int index) {
		WizardPage<?> existingPage = null;
		int immutablePageIndex = -1;

		for (int i = index; immutablePageIndex == -1 && i >= 0; i--) {
			existingPage = pages.get(i);
			immutablePageIndex = unmodifiablePages.indexOf(existingPage);
		}

		return immutablePageIndex;
	}

	/**
	 * @param handler
	 */
	public void addFinishedHandler (PagePlanFinishedHandler handler) {
		finishHandlers.add(handler);
	}

	public void removeFinishedHandler (PagePlanFinishedHandler handler) {
		finishHandlers.remove(handler);
	}

	public int count () {
		return pages.size();
	}

	public void add (WizardPage<?> page) {
		pages.add(page);
	}

	/**
	 * @param currentPage
	 * @param another
	 */
	public void addAt (int index, WizardPage<?> page) {
		pages.add(index, page);
	}

	private void addUnmodifiable (WizardPage<?> page) {
		unmodifiablePages.add(page);
		pages.add(page);
	}

	private void addUnmodifiableAt (int pageIndex, int unmodifiablePageIndex,
			WizardPage<?> page) {
		pages.add(pageIndex, page);
		unmodifiablePages.add(unmodifiablePageIndex, page);
	}

	public boolean canRemove (WizardPage<?> page) {
		return !unmodifiablePages.contains(page);
	}

	public void remove (WizardPage<?> page) {
		if (unmodifiablePages.contains(page)) { throw new RuntimeException(
				"Cannot remove page inserted by builder"); }

		pages.remove(page);
	}

	/**
	 * 
	 */
	public void finished () {
		for (PagePlanFinishedHandler handler : finishHandlers) {
			handler.onfinished(pages);
		}
	}

	public void cancelled () {
		for (PagePlanFinishedHandler handler : finishHandlers) {
			handler.onCancelled();
		}
	}

}
