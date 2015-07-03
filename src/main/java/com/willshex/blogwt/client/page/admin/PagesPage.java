//
//  PagesPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 22 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.admin;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.controller.PageController;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.part.BootstrapGwtCellTable;
import com.willshex.blogwt.client.part.NoneFoundPanel;
import com.willshex.blogwt.client.part.PrettyButtonCell;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.helper.DateTimeHelper;
import com.willshex.blogwt.shared.api.helper.PagerHelper;
import com.willshex.blogwt.shared.api.helper.UserHelper;
import com.willshex.blogwt.shared.api.page.call.DeletePageRequest;
import com.willshex.blogwt.shared.api.page.call.DeletePageResponse;
import com.willshex.blogwt.shared.api.page.call.event.DeletePageEventHandler;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PagesPage extends com.willshex.blogwt.client.page.Page implements
		DeletePageEventHandler {

	private static PagesPageUiBinder uiBinder = GWT
			.create(PagesPageUiBinder.class);

	interface PagesPageUiBinder extends UiBinder<Widget, PagesPage> {}

	@UiField(provided = true) CellTable<Page> tblPages = new CellTable<Page>(
			PagerHelper.DEFAULT_COUNT.intValue(),
			BootstrapGwtCellTable.INSTANCE);
	@UiField SimplePager pgrPages;
	@UiField NoneFoundPanel pnlNoPages;
	private SafeHtmlCell safeHtmlPrototype = new SafeHtmlCell();
	private ButtonCell actionButtonPrototype = new PrettyButtonCell();

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		register(DefaultEventBus.get().addHandlerToSource(
				DeletePageEventHandler.TYPE, PageController.get(), this));
	}

	public PagesPage () {
		super(PageType.PagesPageType);
		initWidget(uiBinder.createAndBindUi(this));

		createColumns();

		tblPages.setEmptyTableWidget(pnlNoPages);
		PageController.get().addDataDisplay(tblPages);
		pgrPages.setDisplay(tblPages);
	}

	private void createColumns () {
		TextColumn<Page> title = new TextColumn<Page>() {

			@Override
			public String getValue (Page object) {
				return object.title;
			}
		};

		TextColumn<Page> owner = new TextColumn<Page>() {

			@Override
			public String getValue (Page object) {
				return UserHelper.name(object.owner);
			}
		};

		Column<Page, SafeHtml> hasChildren = new Column<Page, SafeHtml>(
				safeHtmlPrototype) {

			@Override
			public SafeHtml getValue (Page object) {
				return SafeHtmlUtils
						.fromSafeConstant("<span class=\"glyphicon glyphicon-"
								+ (object.hasChildren != null
										&& object.hasChildren.booleanValue() ? "ok"
										: "remove") + "\"></span>");
			}
		};

		Column<Page, SafeHtml> isInHeader = new Column<Page, SafeHtml>(
				safeHtmlPrototype) {

			@Override
			public SafeHtml getValue (Page object) {
				return SafeHtmlUtils
						.fromSafeConstant("<span class=\"glyphicon glyphicon-"
								+ (object.parent == null
										&& object.priority != null ? "ok"
										: "remove") + "\"></span>");
			}
		};

		TextColumn<Page> created = new TextColumn<Page>() {

			@Override
			public String getValue (Page object) {
				return DateTimeHelper.ago(object.created);
			}
		};

		TextColumn<Page> priority = new TextColumn<Page>() {

			@Override
			public String getValue (Page object) {
				return object.priority == null ? "not listed" : object.priority
						.toString();
			}
		};

		Column<Page, SafeHtml> edit = new Column<Page, SafeHtml>(
				safeHtmlPrototype) {

			@Override
			public SafeHtml getValue (Page object) {
				return SafeHtmlUtils
						.fromSafeConstant("<a class=\"btn btn-default btn-xs\" href=\""
								+ PageType.EditPagePageType.asHref("id",
										object.id.toString()).asString()
								+ "\" ><span class=\"glyphicon glyphicon-edit\"></span> edit<a>");
			}
		};

		Column<Page, String> delete = new Column<Page, String>(
				actionButtonPrototype) {

			@Override
			public String getValue (Page object) {
				return "delete";
			}
		};
		delete.setFieldUpdater(new FieldUpdater<Page, String>() {

			@Override
			public void update (int index, Page object, String value) {
				PageController.get().deletePage(object);
			}
		});

		tblPages.addColumn(title, "Title");
		tblPages.addColumn(owner, "Owner");
		tblPages.addColumn(hasChildren, "Children");
		tblPages.addColumn(isInHeader, "Header");
		tblPages.addColumn(created, "Created");
		tblPages.addColumn(priority, "Priority");
		tblPages.addColumn(edit);
		tblPages.addColumn(delete);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.page.call.event.DeletePageEventHandler
	 * #deletePageSuccess
	 * (com.willshex.blogwt.shared.api.page.call.DeletePageRequest,
	 * com.willshex.blogwt.shared.api.page.call.DeletePageResponse) */
	@Override
	public void deletePageSuccess (DeletePageRequest input,
			DeletePageResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			tblPages.setVisibleRangeAndClearData(tblPages.getVisibleRange(),
					true);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.page.call.event.DeletePageEventHandler
	 * #deletePageFailure
	 * (com.willshex.blogwt.shared.api.page.call.DeletePageRequest,
	 * java.lang.Throwable) */
	@Override
	public void deletePageFailure (DeletePageRequest input, Throwable caught) {}

}
