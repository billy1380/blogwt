//  
//  BlogJsonServlet.java
//  xsdwsdl2code
//
//  Created by William Shakour on May 12, 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS. All rights reserved.
//
package com.willshex.blogwt.server.api.blog;

import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.blog.call.CreatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.DeletePostRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostsRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdatePostRequest;
import com.willshex.gson.json.service.server.JsonServlet;

@SuppressWarnings("serial")
public final class BlogJsonServlet extends JsonServlet {
	@Override
	protected String processAction (String action, JsonObject request) {
		String output = "null";
		BlogService service = new BlogService();
		if ("GetPost".equals(action)) {
			GetPostRequest input = new GetPostRequest();
			input.fromJson(request);
			output = service.getPost(input).toString();
		} else if ("UpdatePost".equals(action)) {
			UpdatePostRequest input = new UpdatePostRequest();
			input.fromJson(request);
			output = service.updatePost(input).toString();
		} else if ("CreatePost".equals(action)) {
			CreatePostRequest input = new CreatePostRequest();
			input.fromJson(request);
			output = service.createPost(input).toString();
		} else if ("DeletePost".equals(action)) {
			DeletePostRequest input = new DeletePostRequest();
			input.fromJson(request);
			output = service.deletePost(input).toString();
		} else if ("GetPosts".equals(action)) {
			GetPostsRequest input = new GetPostsRequest();
			input.fromJson(request);
			output = service.getPosts(input).toString();
		}
		return output;
	}
}