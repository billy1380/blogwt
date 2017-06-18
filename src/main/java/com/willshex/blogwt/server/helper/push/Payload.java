// 
//  Payload.java
//  blogwt
// 
//  Created by William Shakour on June 16, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.helper.push;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.datatype.DataType;
import com.willshex.gson.shared.Jsonable;

public class Payload extends Jsonable {
	public String to;
	public List<String> registration_ids;
	public String condition;
	public String collapse_key;
	public PayloadPrioriyType priority;
	public Boolean content_available;
	public String mutable_content;
	public Double time_to_live;
	public String restricted_package_name;
	public Boolean dry_run;
	public Jsonable data;
	public Message notification;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonTo = to == null ? JsonNull.INSTANCE
				: new JsonPrimitive(to);
		object.add("to", jsonTo);
		JsonElement jsonRegistration_ids = JsonNull.INSTANCE;
		if (registration_ids != null) {
			jsonRegistration_ids = new JsonArray();
			for (int i = 0; i < registration_ids.size(); i++) {
				JsonElement jsonRegistration_idsItem = registration_ids
						.get(i) == null ? JsonNull.INSTANCE
								: new JsonPrimitive(registration_ids.get(i));
				((JsonArray) jsonRegistration_ids)
						.add(jsonRegistration_idsItem);
			}
		}
		object.add("registration_ids", jsonRegistration_ids);
		JsonElement jsonCondition = condition == null ? JsonNull.INSTANCE
				: new JsonPrimitive(condition);
		object.add("condition", jsonCondition);
		JsonElement jsonCollapse_key = collapse_key == null ? JsonNull.INSTANCE
				: new JsonPrimitive(collapse_key);
		object.add("collapse_key", jsonCollapse_key);
		JsonElement jsonPriority = priority == null ? JsonNull.INSTANCE
				: new JsonPrimitive(priority.toString());
		object.add("priority", jsonPriority);
		JsonElement jsonContent_available = content_available == null
				? JsonNull.INSTANCE : new JsonPrimitive(content_available);
		object.add("content_available", jsonContent_available);
		JsonElement jsonMutable_content = mutable_content == null
				? JsonNull.INSTANCE : new JsonPrimitive(mutable_content);
		object.add("mutable_content", jsonMutable_content);
		JsonElement jsonTime_to_live = time_to_live == null ? JsonNull.INSTANCE
				: new JsonPrimitive(time_to_live);
		object.add("time_to_live", jsonTime_to_live);
		JsonElement jsonRestricted_package_name = restricted_package_name == null
				? JsonNull.INSTANCE
				: new JsonPrimitive(restricted_package_name);
		object.add("restricted_package_name", jsonRestricted_package_name);
		JsonElement jsonDry_run = dry_run == null ? JsonNull.INSTANCE
				: new JsonPrimitive(dry_run);
		object.add("dry_run", jsonDry_run);
		JsonElement jsonData = data == null ? JsonNull.INSTANCE : data.toJson();
		object.add("data", jsonData);
		JsonElement jsonNotification = notification == null ? JsonNull.INSTANCE
				: notification.toJson();
		object.add("notification", jsonNotification);
		return object;
	}

	public Payload to (String to) {
		this.to = to;
		return this;
	}

	public Payload registration_ids (List<String> registration_ids) {
		this.registration_ids = registration_ids;
		return this;
	}

	public Payload condition (String condition) {
		this.condition = condition;
		return this;
	}

	public Payload collapse_key (String collapse_key) {
		this.collapse_key = collapse_key;
		return this;
	}

	public Payload priority (PayloadPrioriyType priority) {
		this.priority = priority;
		return this;
	}

	public Payload content_available (Boolean content_available) {
		this.content_available = content_available;
		return this;
	}

	public Payload mutable_content (String mutable_content) {
		this.mutable_content = mutable_content;
		return this;
	}

	public Payload time_to_live (Double time_to_live) {
		this.time_to_live = time_to_live;
		return this;
	}

	public Payload restricted_package_name (String restricted_package_name) {
		this.restricted_package_name = restricted_package_name;
		return this;
	}

	public Payload dry_run (Boolean dry_run) {
		this.dry_run = dry_run;
		return this;
	}

	public Payload data (DataType data) {
		this.data = data;
		return this;
	}

	public Payload notification (Message notification) {
		this.notification = notification;
		return this;
	}
}