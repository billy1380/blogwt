// 
//  QueueForResavingAction.java
//  blogwt
// 
//  Created by William Shakour on July 9, 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.background.resave.input;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.gson.web.service.shared.Request;

public class QueueForResavingAction extends Request {
	public String typeName;
	public Pager pager;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonTypeName = typeName == null ? JsonNull.INSTANCE
				: new JsonPrimitive(typeName);
		object.add("typeName", jsonTypeName);
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE
				: pager.toJson();
		object.add("pager", jsonPager);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("typeName")) {
			JsonElement jsonTypeName = jsonObject.get("typeName");
			if (jsonTypeName != null) {
				typeName = jsonTypeName.getAsString();
			}
		}
		if (jsonObject.has("pager")) {
			JsonElement jsonPager = jsonObject.get("pager");
			if (jsonPager != null) {
				pager = new Pager();
				pager.fromJson(jsonPager.getAsJsonObject());
			}
		}
	}

	/**
	  * Fluent setter for typeName.
	  * 
	  * @param parameters
	  * @return
	  */
	public QueueForResavingAction typeName (String typeName) {
		this.typeName = typeName;
		return this;
	}

	/**
	  * Fluent setter for pager.
	  * 
	  * @param parameters
	  * @return
	  */
	public QueueForResavingAction pager (Pager pager) {
		this.pager = pager;
		return this;
	}
}