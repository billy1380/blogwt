//
//  PropertiesPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 22 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.admin;

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
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.part.BootstrapGwtCellTable;
import com.willshex.blogwt.client.part.NoneFoundPanel;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.helper.PagerHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PropertiesPage extends Page {

	private static PropertiesPageUiBinder uiBinder = GWT
			.create(PropertiesPageUiBinder.class);

	interface PropertiesPageUiBinder extends UiBinder<Widget, PropertiesPage> {}

	@UiField(provided = true) CellTable<Property> tblProperties = new CellTable<Property>(
			PagerHelper.DEFAULT_COUNT.intValue(),
			BootstrapGwtCellTable.INSTANCE);
	@UiField SimplePager pgrProperties;
	@UiField NoneFoundPanel pnlNoProperties;
	private SafeHtmlCell safeHtmlPrototype = new SafeHtmlCell();

	public PropertiesPage () {
		super(PageType.PropertiesPageType);
		initWidget(uiBinder.createAndBindUi(this));

		createColumns();

		tblProperties.setEmptyTableWidget(pnlNoProperties);
		PropertyController.get().addDataDisplay(tblProperties);
		pgrProperties.setDisplay(tblProperties);
	}

	/**
	 * 
	 */
	private void createColumns () {
		TextColumn<Property> code = new TextColumn<Property>() {

			@Override
			public String getValue (Property object) {
				return object.group;
			}
		};

		TextColumn<Property> type = new TextColumn<Property>() {

			@Override
			public String getValue (Property object) {
				return object.type.toString();
			}
		};

		TextColumn<Property> name = new TextColumn<Property>() {

			@Override
			public String getValue (Property object) {
				return object.name;
			}
		};

		TextColumn<Property> value = new TextColumn<Property>() {

			@Override
			public String getValue (Property object) {
				return object.value;
			}
		};

		TextColumn<Property> description = new TextColumn<Property>() {

			@Override
			public String getValue (Property object) {
				return object.description;
			}
		};

		Column<Property, SafeHtml> edit = new Column<Property, SafeHtml>(
				safeHtmlPrototype) {

			@Override
			public SafeHtml getValue (Property object) {
				return SafeHtmlUtils
						.fromSafeConstant("<a class=\"btn btn-default btn-xs\" href=\""
								+ PageType.EditPropertyPageType.asHref("id",
										object.id.toString()).asString()
								+ "\" ><span class=\"glyphicon glyphicon-edit\"></span> edit<a>");
			}
		};

		tblProperties.addColumn(code, "Group");
		tblProperties.addColumn(type, "Type");
		tblProperties.addColumn(name, "Name");
		tblProperties.addColumn(value, "Value");
		tblProperties.addColumn(description, "Description");
		tblProperties.addColumn(edit);
	}
}
