//  
//  Pager.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.gson.shared.Jsonable;

public class Pager extends Jsonable {
	public Integer start;
	public Integer count;
	public String sortBy;
	public SortDirectionType sortDirection;
	public Integer totalCount;
	public String next;
	public String previous;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonStart = start == null ? JsonNull.INSTANCE
				: new JsonPrimitive(start);
		object.add("start", jsonStart);
		JsonElement jsonCount = count == null ? JsonNull.INSTANCE
				: new JsonPrimitive(count);
		object.add("count", jsonCount);
		JsonElement jsonSortBy = sortBy == null ? JsonNull.INSTANCE
				: new JsonPrimitive(sortBy);
		object.add("sortBy", jsonSortBy);
		JsonElement jsonSortDirection = sortDirection == null ? JsonNull.INSTANCE
				: new JsonPrimitive(sortDirection.toString());
		object.add("sortDirection", jsonSortDirection);
		JsonElement jsonTotalCount = totalCount == null ? JsonNull.INSTANCE
				: new JsonPrimitive(totalCount);
		object.add("totalCount", jsonTotalCount);
		JsonElement jsonNext = next == null ? JsonNull.INSTANCE
				: new JsonPrimitive(next);
		object.add("next", jsonNext);
		JsonElement jsonPrevious = previous == null ? JsonNull.INSTANCE
				: new JsonPrimitive(previous);
		object.add("previous", jsonPrevious);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("start")) {
			JsonElement jsonStart = jsonObject.get("start");
			if (jsonStart != null) {
				start = Integer.valueOf(jsonStart.getAsInt());
			}
		}
		if (jsonObject.has("count")) {
			JsonElement jsonCount = jsonObject.get("count");
			if (jsonCount != null) {
				count = Integer.valueOf(jsonCount.getAsInt());
			}
		}
		if (jsonObject.has("sortBy")) {
			JsonElement jsonSortBy = jsonObject.get("sortBy");
			if (jsonSortBy != null) {
				sortBy = jsonSortBy.getAsString();
			}
		}
		if (jsonObject.has("sortDirection")) {
			JsonElement jsonSortDirection = jsonObject.get("sortDirection");
			if (jsonSortDirection != null) {
				sortDirection = SortDirectionType.fromString(jsonSortDirection
						.getAsString());
			}
		}
		if (jsonObject.has("totalCount")) {
			JsonElement jsonTotalCount = jsonObject.get("totalCount");
			if (jsonTotalCount != null) {
				totalCount = Integer.valueOf(jsonTotalCount.getAsInt());
			}
		}
		if (jsonObject.has("next")) {
			JsonElement jsonNext = jsonObject.get("next");
			if (jsonNext != null) {
				next = jsonNext.getAsString();
			}
		}
		if (jsonObject.has("previous")) {
			JsonElement jsonPrevious = jsonObject.get("previous");
			if (jsonPrevious != null) {
				previous = jsonPrevious.getAsString();
			}
		}
	}

	public Pager start (Integer start) {
		this.start = start;
		return this;
	}

	public Pager count (Integer count) {
		this.count = count;
		return this;
	}

	public Pager sortBy (String sortBy) {
		this.sortBy = sortBy;
		return this;
	}

	public Pager sortDirection (SortDirectionType sortDirection) {
		this.sortDirection = sortDirection;
		return this;
	}

	public Pager totalCount (Integer totalCount) {
		this.totalCount = totalCount;
		return this;
	}

	public Pager next (String next) {
		this.next = next;
		return this;
	}

	public Pager previous (String previous) {
		this.previous = previous;
		return this;
	}
}