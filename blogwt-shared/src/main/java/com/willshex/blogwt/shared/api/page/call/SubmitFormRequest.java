//  
//  SubmitFormRequest.java
//  blogwt
//
//  Created by William Shakour on November 12, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.page.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.Form;

public class SubmitFormRequest extends Request {
	public Form form;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonForm = form == null ? JsonNull.INSTANCE : form.toJson();
		object.add("form", jsonForm);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("form")) {
			JsonElement jsonForm = jsonObject.get("form");
			if (jsonForm != null) {
				form = new Form();
				form.fromJson(jsonForm.getAsJsonObject());
			}
		}
	}

	public SubmitFormRequest form (Form form) {
		this.form = form;
		return this;
	}
}