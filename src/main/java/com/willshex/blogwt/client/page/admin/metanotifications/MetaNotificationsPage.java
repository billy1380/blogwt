//
//  MetaNotificationsPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 16 Mar 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.admin.metanotifications;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.controller.MetaNotificationController;
import com.willshex.blogwt.client.helper.PageTypeHelper;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.admin.PagesPage.PagesPageTemplates;
import com.willshex.blogwt.client.part.BootstrapGwtCellTable;
import com.willshex.blogwt.client.part.LoadingPanel;
import com.willshex.blogwt.client.part.NoneFoundPanel;
import com.willshex.blogwt.client.part.SimplePager;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;
import com.willshex.blogwt.shared.api.datatype.NotificationModeType;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.page.PageType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class MetaNotificationsPage extends Page {

	private static MetaNotificationsPageUiBinder uiBinder = GWT
			.create(MetaNotificationsPageUiBinder.class);

	interface MetaNotificationsPageUiBinder
			extends UiBinder<Widget, MetaNotificationsPage> {}

	@UiField(provided = true) CellTable<MetaNotification> tblNotifications = new CellTable<MetaNotification>(
			PagerHelper.DEFAULT_COUNT.intValue(),
			BootstrapGwtCellTable.INSTANCE);
	@UiField SimplePager pgrNotifications;
	@UiField NoneFoundPanel pnlNoNotifications;
	@UiField Button btnRefresh;
	@UiField LoadingPanel pnlLoading;

	//	ListDataProvider<MetaNotification> test = new ListDataProvider<>(
	//			Arrays.asList((MetaNotification) new MetaNotification().code("ABC")
	//					.modes(Arrays.asList(
	//							NotificationModeType.NotificationModeTypeSms))
	//					.name("ABC name").id(1L)),
	//			DataTypeHelper::id);

	public MetaNotificationsPage () {
		initWidget(uiBinder.createAndBindUi(this));

		createColumns();

		tblNotifications.setEmptyTableWidget(pnlNoNotifications);
		tblNotifications.setLoadingIndicator(pnlLoading);
		MetaNotificationController.get().addDataDisplay(tblNotifications);
		// test.addDataDisplay(tblNotifications);
		pgrNotifications.setDisplay(tblNotifications);
	}

	private void createColumns () {

		/* @Index public String code;
		 * 
		 * public String content; public String name; public String group; */

		TextColumn<MetaNotification> code = new TextColumn<MetaNotification>() {

			@Override
			public String getValue (MetaNotification object) {
				return object.code;
			}
		};

		TextColumn<MetaNotification> name = new TextColumn<MetaNotification>() {

			@Override
			public String getValue (MetaNotification object) {
				return object.name == null ? "-" : object.name;
			}
		};

		TextColumn<MetaNotification> group = new TextColumn<MetaNotification>() {

			@Override
			public String getValue (MetaNotification object) {
				return object.group == null ? "-" : object.group;
			}
		};

		Column<MetaNotification, SafeHtml> modes = new Column<MetaNotification, SafeHtml>(
				UiHelper.SAFE_HTML_PROTOTYPE) {

			@Override
			public SafeHtml getValue (MetaNotification object) {
				return modes(object.modes);
			}
		};

		Column<MetaNotification, SafeHtml> defaults = new Column<MetaNotification, SafeHtml>(
				UiHelper.SAFE_HTML_PROTOTYPE) {

			@Override
			public SafeHtml getValue (MetaNotification object) {
				return modes(object.defaults);
			}
		};

		Column<MetaNotification, SafeHtml> edit = new Column<MetaNotification, SafeHtml>(
				UiHelper.SAFE_HTML_PROTOTYPE) {

			@Override
			public SafeHtml getValue (MetaNotification object) {
				return UiHelper.TEMPLATES.xsEdit(PageTypeHelper.asHref(
						PageType.MetaNotificationDetailPageType, "id",
						object.id.toString()));
			}
		};

		tblNotifications.addColumn(code, "Code");
		tblNotifications.addColumn(name, "Name");
		tblNotifications.addColumn(group, "Group");
		tblNotifications.addColumn(modes, "Modes");
		tblNotifications.addColumn(defaults, "Defaults");
		tblNotifications.addColumn(edit);
	}

	@UiHandler("btnRefresh")
	void onRefreshClicked (ClickEvent ce) {
		refresh();
	}

	private void refresh () {
		tblNotifications.setVisibleRangeAndClearData(
				tblNotifications.getVisibleRange(), true);
	}

	SafeHtml modes (List<NotificationModeType> modes) {
		SafeHtmlBuilder b = new SafeHtmlBuilder();
		boolean first = true;

		for (NotificationModeType t : Arrays
				.stream(NotificationModeType.values())
				.sorted( (l, r) -> l.toString().compareTo(r.toString()))
				.collect(Collectors.toList())) {
			if (!first) {
				b.appendEscaped(" ");
			} else {
				first = false;
			}

			b.appendEscaped(t.toString() + "(");
			if (modes == null || !modes.contains(t)) {
				b.append(PagesPageTemplates.INSTANCE.no());
			} else {
				b.append(PagesPageTemplates.INSTANCE.yes());
			}
			b.appendEscaped(")");
		}

		return b.toSafeHtml();
	}

}
