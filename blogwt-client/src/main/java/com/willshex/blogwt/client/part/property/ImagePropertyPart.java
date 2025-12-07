//
//  ImagePropertyPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 31 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.property;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.helper.PropertyHelper;

import elemental2.dom.File;
import elemental2.dom.FormData;
import elemental2.dom.HTMLInputElement;
import elemental2.dom.XMLHttpRequest;
import jsinterop.base.Js;

/**
 * @author billy1380
 *
 */
public class ImagePropertyPart extends AbstractPropertyPart {

	private static ImagePropertyPartUiBinder uiBinder = GWT
			.create(ImagePropertyPartUiBinder.class);

	interface ImagePropertyPartUiBinder
			extends UiBinder<Widget, ImagePropertyPart> {
	}

	@UiField
	Element elDescription;
	@UiField
	Element elName;
	@UiField
	TextBox txtValue;
	@UiField
	FileUpload uplDragAndDrop;
	@UiField
	HTMLPanel pnlValueNote;
	@UiField
	HTMLPanel pnlImagePreviews;
	@UiField
	Button btnClear;

	private Map<String, Resource> resources;
	private HTMLPanel currentResourceRow;

	private static final int IMAGES_PER_ROW = 6;

	public ImagePropertyPart() {
		initWidget(uiBinder.createAndBindUi(this));

		uplDragAndDrop.addChangeHandler(e -> {
			HTMLInputElement input = Js.cast(uplDragAndDrop.getElement());

			if (input.files.length > 0) {
				File file = input.files.item(0);

				RequestBuilder b = new RequestBuilder(RequestBuilder.GET,
						ApiHelper.UPLOAD_END_POINT);
				try {
					b.sendRequest(null, new RequestCallback() {

						@Override
						public void onResponseReceived(Request request,
								Response response) {
							if (200 == response.getStatusCode()) {
								String url = response.getText();

								FormData formData = new FormData();
								formData.append("image", file);

								XMLHttpRequest xhr = new XMLHttpRequest();
								xhr.open("POST", url);
								xhr.onload = v -> {
									if (xhr.status == 200) {
										String json = xhr.responseText;
										if (json != null && !json.isEmpty()) {
											try {
												com.google.gwt.json.client.JSONArray array = com.google.gwt.json.client.JSONParser
														.parseStrict(json).isArray();

												if (array != null && array.size() > 0) {
													for (int i = 0; i < array.size(); i++) {
														com.google.gwt.json.client.JSONObject jsonResource = array
																.get(i).isObject();
														if (jsonResource.containsKey("id")) {
															Resource resource = new Resource();
															resource.id = (long) jsonResource.get("id").isNumber()
																	.doubleValue();

															if (jsonResource.containsKey("name")) {
																resource.name = jsonResource.get("name").isString()
																		.stringValue();
															}

															if (jsonResource.containsKey("data")) {
																resource.data = jsonResource.get("data").isString()
																		.stringValue();
															}

															if (resource != null) {
																ensureResources().put(resource.name, resource);
																addImage(resource);
																setValue(resource.data, true);
															}
														}
													}
												}

											} catch (Exception e1) {
												GWT.log("Error parsing upload response", e1);
											}
										}
									}
									// cleanup
									input.value = "";
								};
								xhr.send(formData);
							}
						}

						@Override
						public void onError(Request request,
								Throwable exception) {
						}
					});
				} catch (RequestException e1) {
				}
			}
		});

		btnClear.getElement().setInnerHTML(
				"<span class=\"glyphicon glyphicon-remove\"></span>");
	}

	private void addImage(Resource resource) {
		Image image = new Image(resource.data);
		image.addStyleName("img-rounded");
		image.addStyleName("col-xs-" + (int) (12 / IMAGES_PER_ROW));

		if ((resources.size() - 1) % IMAGES_PER_ROW == 0) {
			currentResourceRow = new HTMLPanel(SafeHtmlUtils.EMPTY_SAFE_HTML);
			currentResourceRow.addStyleName("row");
			pnlImagePreviews.add(currentResourceRow);
		}

		currentResourceRow.add(image);
	}

	private Map<String, Resource> ensureResources() {
		return resources == null ? resources = new HashMap<String, Resource>()
				: resources;
	}

	/**
	 * @param description
	 */
	public void setDescription(String description) {
		elDescription.setInnerText(description);
		UiHelper.addPlaceholder(txtValue, description);
	}

	/**
	 */
	public void setAutofocus() {
		UiHelper.autoFocus(txtValue);
	}

	@UiHandler("txtValue")
	void onTextValueChanged(ValueChangeEvent<String> vce) {
		setValue(vce.getValue(), true);
	}

	@UiHandler("btnClear")
	void onBtnClear(ClickEvent ce) {
		setValue(PropertyHelper.NONE_VALUE, true);
	}
	@Override
	public void setValue(String value, boolean fireEvents) {
		if (value == null) {
			value = "";
		}

		String oldValue = getValue();

		txtValue.setValue(value);
		this.value = value;

		if (value.equals(oldValue)) {
			return;
		}
		if (fireEvents) {
			ValueChangeEvent.fire(this, value);
		}
	}
	@Override
	public String getName() {
		return elName.getInnerText();
	}
	@Override
	public void setName(String name) {
		elName.setInnerText(name);
	}

	public void setValidExtensions(String extensions) {
		uplDragAndDrop.getElement().setAttribute("accept",
				extensions.replaceAll(",", ",."));
	}
}
