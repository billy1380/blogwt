//
//  DownloadsPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 13 Apr 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.user.downloads;

import java.util.Date;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.URL;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.download.event.DeleteGeneratedDownloadsEventHandler;
import com.willshex.blogwt.client.api.download.event.GetGeneratedDownloadsEventHandler;
import com.willshex.blogwt.client.cell.PrettyButtonCell;
import com.willshex.blogwt.client.controller.GeneratedDownloadController;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.PageTypeHelper;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.part.BootstrapGwtCellTable;
import com.willshex.blogwt.client.part.LoadingPanel;
import com.willshex.blogwt.client.part.NoneFoundPanel;
import com.willshex.blogwt.client.part.SimplePager;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownload;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownloadStatusType;
import com.willshex.blogwt.shared.api.download.call.DeleteGeneratedDownloadsRequest;
import com.willshex.blogwt.shared.api.download.call.DeleteGeneratedDownloadsResponse;
import com.willshex.blogwt.shared.api.download.call.GetGeneratedDownloadsRequest;
import com.willshex.blogwt.shared.api.download.call.GetGeneratedDownloadsResponse;
import com.willshex.blogwt.shared.helper.DateTimeHelper;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.page.Stack;
import com.willshex.blogwt.shared.page.search.Filter;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class DownloadsPage extends Page implements
		NavigationChangedEventHandler, GetGeneratedDownloadsEventHandler,
		DeleteGeneratedDownloadsEventHandler {

	private static DownloadsPageUiBinder uiBinder = GWT
			.create(DownloadsPageUiBinder.class);

	interface DownloadsPageUiBinder extends UiBinder<Widget, DownloadsPage> {}

	public interface Templates extends SafeHtmlTemplates {
		Templates INSTANCE = GWT.create(Templates.class);

		@Template("<a href=\"{1}\">{0}</a>")
		SafeHtml label (String parameters, SafeUri uri);

		@Template("{0}")
		SafeHtml label (String parameters);

		@Template("<a href=\"{0}\" target=\"_blank\" class=\"btn btn-xs btn-primary\"><span class=\"glyphicon glyphicon-download-alt\"></span> download</a>")
		SafeHtml download (SafeUri uri);
	}

	private static final Cell<String> ACTION_BUTTON_PROTOTYPE = new PrettyButtonCell(
			new PrettyButtonCell.Button("resume", "btn-success", null));

	@UiField(provided = true) CellTable<GeneratedDownload> tblDownloads = new CellTable<GeneratedDownload>(
			PagerHelper.DEFAULT_COUNT.intValue(),
			BootstrapGwtCellTable.INSTANCE);
	@UiField SimplePager pgrDownloads;
	@UiField NoneFoundPanel pnlNoDownloads;
	@UiField Button btnRefresh;
	@UiField LoadingPanel pnlLoading;

	private Timer timer = new Timer() {

		@Override
		public void run () {
			refresh();
		}
	};

	public DownloadsPage () {
		initWidget(uiBinder.createAndBindUi(this));

		createColumns();

		tblDownloads.setEmptyTableWidget(pnlNoDownloads);
		tblDownloads.setLoadingIndicator(pnlLoading);

		GeneratedDownloadController.get().addDataDisplay(tblDownloads);

		pgrDownloads.setDisplay(tblDownloads);
	}
	@Override
	protected void onAttach () {
		super.onAttach();
		register(DefaultEventBus.get().addHandlerToSource(
				DeleteGeneratedDownloadsEventHandler.TYPE,
				GeneratedDownloadController.get(), this));
		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				this));
		register(DefaultEventBus.get().addHandlerToSource(
				GetGeneratedDownloadsEventHandler.TYPE,
				GeneratedDownloadController.get(), this));
	}

	/**
	 * 
	 */
	private void createColumns () {
		Column<GeneratedDownload, SafeHtml> name = new Column<GeneratedDownload, SafeHtml>(
				UiHelper.SAFE_HTML_PROTOTYPE) {
			@Override
			public SafeHtml getValue (GeneratedDownload object) {
				String parameters = object.parameters.replace("/send", "");

				String label = parameters.replace(Filter.QUERY + "/", "")
						.replace("/", " > ");
				label = URL.decodeQueryString(label);

				return parameters.startsWith("proforma")
						|| parameters.startsWith("invoice")
								? Templates.INSTANCE.label(label)
								: Templates.INSTANCE.label(label,
										PageTypeHelper.slugToHref(parameters));
			}
		};

		TextColumn<GeneratedDownload> date = new TextColumn<GeneratedDownload>() {
			@Override
			public String getValue (GeneratedDownload object) {
				Date date;

				if (object.created == null) {
					date = DateTimeHelper.now();
				} else {
					date = object.created;
				}

				return DateTimeHelper.ago(date);
			}
		};

		TextColumn<GeneratedDownload> status = new TextColumn<GeneratedDownload>() {
			@Override
			public String getValue (GeneratedDownload object) {
				return object.status.toString();
			}
		};

		Column<GeneratedDownload, SafeHtml> download = new Column<GeneratedDownload, SafeHtml>(
				UiHelper.SAFE_HTML_PROTOTYPE) {
			@Override
			public SafeHtml getValue (GeneratedDownload object) {
				return object.status == GeneratedDownloadStatusType.GeneratedDownloadStatusTypeReady
						? Templates.INSTANCE
								.download(UriUtils.fromString(object.url))
						: SafeHtmlUtils.EMPTY_SAFE_HTML;
			}
		};
		tblDownloads.setColumnWidth(download, 1.0, Unit.PX);

		Column<GeneratedDownload, String> delete = new Column<GeneratedDownload, String>(
				ACTION_BUTTON_PROTOTYPE) {
			@Override
			public String getValue (GeneratedDownload object) {
				return "delete";
			}
		};
		delete.setFieldUpdater(new FieldUpdater<GeneratedDownload, String>() {
			@Override
			public void update (int index, GeneratedDownload object,
					String value) {
				if (Window.confirm(
						"Are you sure you want to delete export with parameters "
								+ URL.decodeQueryString(object.parameters)
								+ "?")) {
					GeneratedDownloadController.get()
							.deleteGeneratedDownload(object);
				}
			}
		});
		tblDownloads.setColumnWidth(delete, 1.0, Unit.PX);

		tblDownloads.addColumn(name, "Type");
		tblDownloads.addColumn(date, "Date");
		tblDownloads.addColumn(status, "Completion");

		// actions
		tblDownloads.addColumn(download);
		tblDownloads.addColumn(delete);
	}
	@Override
	public void navigationChanged (Stack previous, Stack current) {
		if (current.getParameterCount() > 0) {
			refresh();
		}

		startTimer();
	}

	private void refresh () {
		tblDownloads.setVisibleRangeAndClearData(tblDownloads.getVisibleRange(),
				true);
	}

	@UiHandler("btnRefresh")
	void onRefreshClicked (ClickEvent ce) {
		refresh();
	}
	@Override
	protected void reset () {
		super.reset();

		stopTimer();
	}
	@Override
	public void getGeneratedDownloadsSuccess (
			GetGeneratedDownloadsRequest input,
			GetGeneratedDownloadsResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			boolean foundGeneratingDownload = false;

			if (output.downloads != null) {
				for (GeneratedDownload download : output.downloads) {
					if (download.status == GeneratedDownloadStatusType.GeneratedDownloadStatusTypeGenerating) {
						foundGeneratingDownload = true;
						break;
					}
				}
			}

			if (foundGeneratingDownload) {
				startTimer();
			} else {
				stopTimer();
			}
		}
	}
	@Override
	public void getGeneratedDownloadsFailure (
			GetGeneratedDownloadsRequest input, Throwable caught) {}

	private void startTimer () {
		if (!timer.isRunning()) {
			timer.scheduleRepeating(5000);
		}
	}

	private void stopTimer () {
		if (timer.isRunning()) {
			timer.cancel();
		}
	}
	@Override
	public void deleteGeneratedDownloadsSuccess (
			DeleteGeneratedDownloadsRequest input,
			DeleteGeneratedDownloadsResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			refresh();
		}
	}
	@Override
	public void deleteGeneratedDownloadsFailure (
			DeleteGeneratedDownloadsRequest input, Throwable caught) {}

}
