// 
//  ResaveRequest.java
//  blogwt
// 
//  Created by William Shakour on July 9, 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.background.resave.input;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.datatype.DataType;
import com.willshex.gson.web.service.shared.Request;

public class ResaveAction extends Request {
	public String typeName;
	public Pager pager;
	public List<DataType> data;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonTypeName = typeName == null ? JsonNull.INSTANCE
				: new JsonPrimitive(typeName);
		object.add("typeName", jsonTypeName);
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE
				: pager.toJson();
		object.add("pager", jsonPager);
		JsonElement jsonData = JsonNull.INSTANCE;
		if (data != null) {
			jsonData = new JsonArray();
			for (int i = 0; i < data.size(); i++) {
				JsonElement jsonDataItem = data.get(i) == null
						? JsonNull.INSTANCE
						: data.get(i).toJson();
				((JsonArray) jsonData).add(jsonDataItem);
			}
		}
		object.add("data", jsonData);
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
		if (jsonObject.has("data")) {
			JsonElement jsonData = jsonObject.get("data");
			if (jsonData != null) {
				data = new ArrayList<DataType>();
				DataType item = null;
				for (int i = 0; i < jsonData.getAsJsonArray().size(); i++) {
					if (jsonData.getAsJsonArray().get(i) != null) {
						(item = new DataType()).fromJson(jsonData
								.getAsJsonArray().get(i).getAsJsonObject());
						data.add(item);
					}
				}
			}
		}

	}

	/**
	  * Fluent setter for typeName.
	  * 
	  * @param parameters
	  * @return
	  */
	public ResaveAction typeName (String typeName) {
		this.typeName = typeName;
		return this;
	}

	/**
	  * Fluent setter for pager.
	  * 
	  * @param parameters
	  * @return
	  */
	public ResaveAction pager (Pager pager) {
		this.pager = pager;
		return this;
	}

	/**
	  * Fluent setter for data.
	  * 
	  * @param parameters
	  * @return
	  */
	public ResaveAction data (List<DataType> data) {
		this.data = data;
		return this;
	}
}