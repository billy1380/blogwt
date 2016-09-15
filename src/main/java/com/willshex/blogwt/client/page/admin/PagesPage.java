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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.page.event.DeletePageEventHandler;
import com.willshex.blogwt.client.cell.PrettyButtonCell;
import com.willshex.blogwt.client.controller.PageController;
import com.willshex.blogwt.client.helper.PageTypeHelper;
import com.willshex.blogwt.client.part.BootstrapGwtCellTable;
import com.willshex.blogwt.client.part.NoneFoundPanel;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.page.call.DeletePageRequest;
import com.willshex.blogwt.shared.api.page.call.DeletePageResponse;
import com.willshex.blogwt.shared.helper.DateTimeHelper;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PagesPage extends com.willshex.blogwt.client.page.Page
		implements DeletePageEventHandler {

	private static PagesPageUiBinder uiBinder = GWT
			.create(PagesPageUiBinder.class);

	interface PagesPageUiBinder extends UiBinder<Widget, PagesPage> {}

	@UiField(provided = true) CellTable<Page> tblPages = new CellTable<Page>(
			PagerHelper.DEFAULT_COUNT.intValue(),
			BootstrapGwtCellTable.INSTANCE);
	@UiField SimplePager pgrPages;
	@UiField NoneFoundPanel pnlNoPages;
	@UiField Button btnRefresh;
	private SafeHtmlCell safeHtmlPrototype = new SafeHtmlCell();
	private ButtonCell actionButtonPrototype = new PrettyButtonCell();

	public interface PagesPageTemplates extends SafeHtmlTemplates {
		public PagesPageTemplates INSTANCE = GWT
				.create(PagesPageTemplates.class);

		@Template("<a href=\"{0}\">{1}<a>")
		SafeHtml title (SafeUri slug, SafeHtml title);

		@Template("<span class=\"glyphicon glyphicon-ok\"></span>")
		SafeHtml yes ();

		@Template("<span class=\"glyphicon glyphicon-remove\"></span>")
		SafeHtml no ();

		@Template("<a class=\"btn btn-default btn-xs\" href=\"{0}\" ><span class=\"glyphicon glyphicon-edit\"></span> edit<a>")
		SafeHtml edit (SafeUri href);

	}

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
		initWidget(uiBinder.createAndBindUi(this));

		createColumns();

		tblPages.setEmptyTableWidget(pnlNoPages);
		PageController.get().addDataDisplay(tblPages);
		pgrPages.setDisplay(tblPages);
	}

	private void createColumns () {
		Column<Page, SafeHtml> title = new Column<Page, SafeHtml>(
				safeHtmlPrototype) {

			@Override
			public SafeHtml getValue (Page object) {
				return PagesPageTemplates.INSTANCE.title(
						PageTypeHelper.slugToHref(object.slug),
						SafeHtmlUtils.fromString(object.title));
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
				return object.hasChildren != null
						&& object.hasChildren.booleanValue()
								? PagesPageTemplates.INSTANCE.yes()
								: PagesPageTemplates.INSTANCE.no();
			}
		};

		Column<Page, SafeHtml> isInHeader = new Column<Page, SafeHtml>(
				safeHtmlPrototype) {

			@Override
			public SafeHtml getValue (Page object) {
				return object.parent == null && object.priority != null
						? PagesPageTemplates.INSTANCE.yes()
						: PagesPageTemplates.INSTANCE.no();
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
				return object.priority == null ? "not listed"
						: object.priority.toString();
			}
		};

		Column<Page, SafeHtml> edit = new Column<Page, SafeHtml>(
				safeHtmlPrototype) {

			@Override
			public SafeHtml getValue (Page object) {
				return PagesPageTemplates.INSTANCE.edit(PageTypeHelper
						.asHref(PageType.EditPagePageType, object.slug));
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
				if (Window.confirm("Are you sure you want to delete page: "
						+ object.title + "?")) {
					PageController.get().deletePage(object);
				}
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

	@UiHandler("btnRefresh")
	void onRefreshClicked (ClickEvent ce) {
		tblPages.setVisibleRangeAndClearData(tblPages.getVisibleRange(), true);
	}

}
