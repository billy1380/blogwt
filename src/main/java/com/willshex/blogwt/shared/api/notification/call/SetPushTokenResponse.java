// 
//  SetPushTokenResponse.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.shared.api.notification.call;

import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Response;

public class SetPushTokenResponse extends Response {
@Override
public JsonObject toJson() {
JsonObject object = super.toJson();
return object;
}
@Override
public void fromJson(JsonObject jsonObject) {
super.fromJson(jsonObject);
}
}