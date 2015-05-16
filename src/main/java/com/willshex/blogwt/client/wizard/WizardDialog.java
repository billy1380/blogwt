//
//  WizardDialog.java
//  blogwt
//
//  Created by billy1380 on 1 Aug 2013.
//  Copyright © 2013 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.wizard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.Resources;

/**
 * @author billy1380
 * 
 */
public class WizardDialog extends Composite {

	private PagePlan plan = null;
	private int currentPage = 0;

	public interface WizardDialogTemplates extends SafeHtmlTemplates {
		WizardDialogTemplates INSTANCE = GWT.create(WizardDialogTemplates.class);

		@Template("<span class=\"glyphicon glyphicon-arrow-left\" style=\"margin-right:10px\"></span>{0}")
		SafeHtml backButton(String name);

		@Template("{0}<span class=\"glyphicon glyphicon-arrow-right\" style=\"margin-left:10px\"></span>")
		SafeHtml nextButton(String name);

		@Template("{0}<img src=\"{1}\" alt=\"Loading...\">")
		SafeHtml loadingButton(String name, SafeUri imgSrc);
	}

	private static final String ADD_CHILD_BUTTON_SPAN = "<span class=\"glyphicon glyphicon-plus\"></span>";
	private static final String REMOVE_CHILD_BUTTON_SPAN = "<span class=\"glyphicon glyphicon-minus\"></span>";

	private static final String FINISH_BUTTON_SPAN = "Finish<span class=\"glyphicon glyphicon-ok\" style=\"margin-left:10px\"></span>";

	private static final SafeHtml ADD_HTML = SafeHtmlUtils.fromSafeConstant(ADD_CHILD_BUTTON_SPAN);
	private static final SafeHtml REMOVE_HTML = SafeHtmlUtils.fromSafeConstant(REMOVE_CHILD_BUTTON_SPAN);
	private static final SafeHtml FINISH_HTML = SafeHtmlUtils.fromSafeConstant(FINISH_BUTTON_SPAN);

	@UiField Button btnBack;

	@UiField Button btnAddChild;
	@UiField Button btnRemoveChild;

	@UiField SubmitButton btnNext;

	@UiField HeadingElement h2WizardTitle;
	@UiField HeadingElement h3PageTitle;

	@UiField HTMLPanel pnlContents;

	@UiField HTMLPanel pnlRepeatable;

	private static WizardDialogUiBinder uiBinder = GWT.create(WizardDialogUiBinder.class);

	interface WizardDialogUiBinder extends UiBinder<Widget, WizardDialog> {}

	public WizardDialog() {
		this(null);
	}

	public WizardDialog(PagePlan plan) {
		initWidget(uiBinder.createAndBindUi(this));

		setPlan(plan);
	}

	protected void setPlan(PagePlan plan) {
		this.plan = plan;

		if (plan != null) {
			h2WizardTitle.setInnerHTML(plan.getName());

			layout();
		}
	}

	@UiHandler("btnBack")
	void onBtnBackClicked(ClickEvent event) {
		if (currentPage > 0) {
			currentPage--;

			layout();
		}
	}

	@UiHandler("btnNext")
	void onBtnNextClicked(ClickEvent event) {

		if (plan.get(currentPage).validate()) {
			if (plan.count() - 1 == currentPage) {
				btnNext.getElement().setInnerSafeHtml(
						WizardDialogTemplates.INSTANCE.loadingButton("Loading... ", Resources.RES.primaryLoader().getSafeUri()));
				plan.finished();
			} else {
				currentPage++;

				layout();
			}
		}
	}

	@UiHandler("btnAddChild")
	void onBtnAddChild(ClickEvent event) {
		if (currentPage + 1 == plan.count()) {
			plan.add(plan.get(currentPage).another());
		} else {
			plan.addAt(currentPage + 1, plan.get(currentPage).another());
		}

		onBtnNextClicked(event);
	}

	@UiHandler("btnRemoveChild")
	void onBtnRemoveChild(ClickEvent event) {
		if (plan.canRemove(plan.get(currentPage))) {
			plan.remove(plan.get(currentPage));

			onBtnBackClicked(event);
		}
	}

	private void layout() {
		if (currentPage > 0) {
			btnBack.getElement().setInnerSafeHtml(WizardDialogTemplates.INSTANCE.backButton(plan.get(currentPage - 1).getPageTitle()));
			btnBack.setVisible(true);
		} else {
			btnBack.setVisible(false);
		}

		WizardPage<?> page = plan.get(currentPage);

		if (page.isRepeatable()) {
			btnAddChild.getElement().setInnerSafeHtml(ADD_HTML);
			pnlRepeatable.setVisible(true);
			btnRemoveChild.getElement().setInnerSafeHtml(REMOVE_HTML);

			if (plan.canRemove(page)) {
				btnRemoveChild.removeStyleName("disabled");
			} else {
				btnRemoveChild.removeStyleName("disabled");
				btnRemoveChild.addStyleName("disabled");
			}
		} else {
			pnlRepeatable.setVisible(false);
		}

		if (plan.count() - 1 == currentPage) {
			btnNext.getElement().setInnerSafeHtml(FINISH_HTML);
		} else {
			btnNext.getElement().setInnerSafeHtml(WizardDialogTemplates.INSTANCE.nextButton(plan.get(currentPage + 1).getPageTitle()));
		}

		h3PageTitle.setInnerHTML(page.getPageTitle());

		pnlContents.clear();
		pnlContents.add(page.getBody());
	}

}
