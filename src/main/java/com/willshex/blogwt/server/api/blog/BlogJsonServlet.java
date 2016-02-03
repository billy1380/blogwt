//  
//  BlogJsonServlet.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog;

import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.blog.call.CreatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.DeletePostRequest;
import com.willshex.blogwt.shared.api.blog.call.DeleteResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.GetArchiveEntriesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetRelatedPostsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.GetResourcesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetTagsRequest;
import com.willshex.blogwt.shared.api.blog.call.SetupBlogRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdatePropertiesRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdateResourceRequest;
import com.willshex.gson.web.service.server.JsonServlet;

@SuppressWarnings("serial")
public final class BlogJsonServlet extends JsonServlet {
	@Override
	protected String processAction (String action, JsonObject request) {
		String output = "null";
		BlogApi service = new BlogApi();
		if ("UpdateResource".equals(action)) {
			UpdateResourceRequest input = new UpdateResourceRequest();
			input.fromJson(request);
			output = service.updateResource(input).toString();
		} else if ("GetResource".equals(action)) {
			GetResourceRequest input = new GetResourceRequest();
			input.fromJson(request);
			output = service.getResource(input).toString();
		}
		if ("GetArchiveEntries".equals(action)) {
			GetArchiveEntriesRequest input = new GetArchiveEntriesRequest();
			input.fromJson(request);
			output = service.getArchiveEntries(input).toString();
		} else if ("DeleteResource".equals(action)) {
			DeleteResourceRequest input = new DeleteResourceRequest();
			input.fromJson(request);
			output = service.deleteResource(input).toString();
		} else if ("GetResources".equals(action)) {
			GetResourcesRequest input = new GetResourcesRequest();
			input.fromJson(request);
			output = service.getResources(input).toString();
		} else if ("GetRelatedPosts".equals(action)) {
			GetRelatedPostsRequest input = new GetRelatedPostsRequest();
			input.fromJson(request);
			output = service.getRelatedPosts(input).toString();
		} else if ("UpdateProperties".equals(action)) {
			UpdatePropertiesRequest input = new UpdatePropertiesRequest();
			input.fromJson(request);
			output = service.updateProperties(input).toString();
		} else if ("GetPosts".equals(action)) {
			GetPostsRequest input = new GetPostsRequest();
			input.fromJson(request);
			output = service.getPosts(input).toString();
		} else if ("GetTags".equals(action)) {
			GetTagsRequest input = new GetTagsRequest();
			input.fromJson(request);
			output = service.getTags(input).toString();
		} else if ("GetPost".equals(action)) {
			GetPostRequest input = new GetPostRequest();
			input.fromJson(request);
			output = service.getPost(input).toString();
		} else if ("DeletePost".equals(action)) {
			DeletePostRequest input = new DeletePostRequest();
			input.fromJson(request);
			output = service.deletePost(input).toString();
		} else if ("SetupBlog".equals(action)) {
			SetupBlogRequest input = new SetupBlogRequest();
			input.fromJson(request);
			output = service.setupBlog(input).toString();
		} else if ("CreatePost".equals(action)) {
			CreatePostRequest input = new CreatePostRequest();
			input.fromJson(request);
			output = service.createPost(input).toString();
		} else if ("UpdatePost".equals(action)) {
			UpdatePostRequest input = new UpdatePostRequest();
			input.fromJson(request);
			output = service.updatePost(input).toString();
		}
		return output;
	}
}