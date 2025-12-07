//
//  SimplePager.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 25 May 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.AbstractPager;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.Range;

/**
 * @author billy1380
 * 
 */
public class SimplePager extends AbstractPager {

	private static SimplePagerUiBinder uiBinder = GWT
			.create(SimplePagerUiBinder.class);

	interface SimplePagerUiBinder extends UiBinder<Widget, SimplePager> {}

	@UiField UListElement elList;

	private static final int DEFAULT_FAST_FORWARD_ROWS = 1000;

	@UiField Anchor btnFastForward;
	@UiField LIElement elFastForwardItem;

	private final int fastForwardRows;

	@UiField Anchor btnFirstPage;
	@UiField LIElement elFirstPageItem;

	@UiField SpanElement elLabel;

	@UiField Anchor btnLastPage;
	@UiField LIElement elLastPageItem;

	@UiField Anchor btnNextPage;
	@UiField LIElement elNextPageItem;

	@UiField Anchor btnPrevPage;
	@UiField LIElement elPrevPageItem;

	/**
	 * Construct a {@link SimplePager} with the specified text location.
	 * 
	 */
	@UiConstructor
	public SimplePager () {
		this(true, DEFAULT_FAST_FORWARD_ROWS, false);
	}

	/**
	 * Construct a {@link SimplePager} with the default resources, fast forward rows and default image button names.
	 * 
	 * @param showFastForwardButton
	 *            if true, show a fast-forward button that advances by a larger increment than a single page
	 * @param showLastPageButton
	 *            if true, show a button to go the the last page
	 */
	public SimplePager (boolean showFastForwardButton,
			boolean showLastPageButton) {
		this(showFastForwardButton, DEFAULT_FAST_FORWARD_ROWS,
				showLastPageButton);
	}

	/**
	 * Construct a {@link SimplePager} with the specified resources.
	 * 
	 * @param showFastForwardButton
	 *            if true, show a fast-forward button that advances by a larger increment than a single page
	 * @param fastForwardRows
	 *            the number of rows to jump when fast forwarding
	 * @param showLastPageButton
	 *            if true, show a button to go the the last page
	 * @param imageButtonConstants
	 *            Constants that contain the image button names
	 */
	public SimplePager (boolean showFastForwardButton,
			final int fastForwardRows, boolean showLastPageButton) {
		initWidget(uiBinder.createAndBindUi(this));

		this.fastForwardRows = fastForwardRows;

		if (!showFastForwardButton) {
			elFastForwardItem.removeFromParent();
			elFastForwardItem = null;
		}

		if (!showLastPageButton) {
			elLastPageItem.removeFromParent();
			elLastPageItem = null;
		}

		// Disable the buttons by default.
		setDisplay(null);
	}

	@UiHandler("btnFirstPage")
	void onFirstPageClicked (ClickEvent e) {
		firstPage();
	}

	@UiHandler("btnNextPage")
	void onNextPageClicked (ClickEvent e) {
		nextPage();
	}

	@UiHandler("btnPrevPage")
	void onPrevPageClicked (ClickEvent e) {
		previousPage();
	}

	@UiHandler("btnLastPage")
	void onLastPageClicked (ClickEvent e) {
		lastPage();
	}

	@UiHandler("btnFastForward")
	void onFastForwardClicked (ClickEvent e) {
		if (!elFastForwardItem.getClassName().contains("disabled")) {
			setPage(getPage() + getFastForwardPages());
		}
	}

	@Override
	public void firstPage () {
		super.firstPage();
	}

	@Override
	public void setDisplay (HasRows display) {
		// Enable or disable all buttons.
		boolean disableButtons = (display == null);

		setFastForwardDisabled(disableButtons);
		setNextPageButtonsDisabled(disableButtons);
		setPrevPageButtonsDisabled(disableButtons);

		super.setDisplay(display);
	}

	@Override
	public void setPage (int index) {
		super.setPage(index);
	}

	@Override
	public void setPageSize (int pageSize) {
		super.setPageSize(pageSize);
	}

	@Override
	public void setPageStart (int index) {
		super.setPageStart(index);
	}

	/**
	 * Let the page know that the table is loading. Call this method to clear 
	 * all data from the table and hide the current range when new data is being 
	 * loaded into the table.
	 */
	public void startLoading () {
		getDisplay().setRowCount(0, true);
		elLabel.setInnerHTML("");
	}

	/**
	 * Get the text to display in the pager that reflects the state of the pager.
	 * 
	 * @return the text
	 */
	protected String createText () {
		// Default text is 1 based.
		NumberFormat formatter = NumberFormat.getFormat("#,###");
		HasRows display = getDisplay();
		Range range = display.getVisibleRange();
		int pageStart = range.getStart() + 1;
		int pageSize = range.getLength();
		int dataSize = display.getRowCount();
		int endIndex = Math.min(dataSize, pageStart + pageSize - 1);
		endIndex = Math.max(pageStart, endIndex);
		boolean exact = display.isRowCountExact();
		return formatter.format(pageStart) + "-" + formatter.format(endIndex)
				+ (exact ? " of " : " of over ") + formatter.format(dataSize);
	}

	@Override
	protected void onRangeOrRowCountChanged () {
		HasRows display = getDisplay();
		elLabel.setInnerHTML(createText());

		// Update the prev and first buttons.
		setPrevPageButtonsDisabled(!hasPreviousPage());

		// Update the next and last buttons.
		if (isRangeLimited() || !display.isRowCountExact()) {
			setNextPageButtonsDisabled(!hasNextPage());
			setFastForwardDisabled(!hasNextPages(getFastForwardPages()));
		}
	}

	/**
	 * Check if the next button is disabled. Visible for testing.
	 */
	boolean isNextButtonDisabled () {
		return elNextPageItem.getClassName().contains("disabled");
	}

	/**
	 * Check if the previous button is disabled. Visible for testing.
	 */
	boolean isPreviousButtonDisabled () {
		return elPrevPageItem.getClassName().contains("disabled");
	}

	/**
	 * Get the number of pages to fast forward based on the current page size.
	 * 
	 * @return the number of pages to fast forward
	 */
	private int getFastForwardPages () {
		int pageSize = getPageSize();
		return pageSize > 0 ? fastForwardRows / pageSize : 0;
	}

	/**
	 * Enable or disable the fast forward button.
	 * 
	 * @param disabled
	 *            true to disable, false to enable
	 */
	private void setFastForwardDisabled (boolean disabled) {
		if (elFastForwardItem == null) { return; }
		setDisabled(elFastForwardItem, disabled);
	}

	void setDisabled (Element e, boolean disabled) {
		if (disabled) {
			if (!e.getClassName().contains("disabled")) {
				e.addClassName("disabled");
			}
		} else {
			if (e.getClassName().contains("disabled")) {
				e.removeClassName("disabled");
			}
		}
	}

	/**
	 * Enable or disable the next page buttons.
	 * 
	 * @param disabled
	 *            true to disable, false to enable
	 */
	private void setNextPageButtonsDisabled (boolean disabled) {
		setDisabled(elNextPageItem, disabled);
		if (elLastPageItem != null) {
			setDisabled(elLastPageItem, disabled);
		}
	}

	/**
	 * Enable or disable the previous page buttons.
	 * 
	 * @param disabled
	 *            true to disable, false to enable
	 */
	private void setPrevPageButtonsDisabled (boolean disabled) {
		setDisabled(elFirstPageItem, disabled);
		setDisabled(elPrevPageItem, disabled);
	}

}
