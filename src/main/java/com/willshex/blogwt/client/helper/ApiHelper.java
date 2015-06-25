//
//  RemoteDataHelper.java
//  blogwt
//
//  Created by billy1380 on 1 Aug 2013.
//  Copyright © 2013 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.helper;

import static com.google.gwt.user.client.Window.Location.getHost;

import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.blog.BlogService;
import com.willshex.blogwt.client.api.page.PageService;
import com.willshex.blogwt.client.api.user.UserService;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.json.service.shared.Error;

/**
 * @author billy1380
 * 
 */
public class ApiHelper {

	public static final String ACCESS_CODE = "2bfe5f0e-9138-401c-8619-9a66f6367c9a";
	public static final String BLOG_END_POINT = "//" + getHost() + "/blog";
	public static final String USER_END_POINT = "//" + getHost() + "/user";
	public static final String PAGE_END_POINT = "//" + getHost() + "/page";
	public static final String UPLOAD_END_POINT = "//" + getHost() + "/upload";

	public static BlogService createBlogClient () {
		BlogService service = new BlogService();
		service.setUrl(BLOG_END_POINT);
		service.setBus(DefaultEventBus.get());
		return service;
	}

	public static UserService createUserClient () {
		UserService service = new UserService();
		service.setUrl(USER_END_POINT);
		service.setBus(DefaultEventBus.get());
		return service;
	}
	
	public static PageService createPageClient () {
		PageService service = new PageService();
		service.setUrl(PAGE_END_POINT);
		service.setBus(DefaultEventBus.get());
		return service;
	}

	public static <T extends Request> T setAccessCode (T input) {
		input.accessCode = ACCESS_CODE;
		return input;
	}

	/**
	 * @param error
	 * @param apiError
	 * @return
	 */
	public static boolean isError (Error error, ApiError apiError) {
		return error != null && error.code != null
				&& error.code.intValue() == apiError.getCode();
	}

}
