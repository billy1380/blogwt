//  
//  BlogJsonServlet.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog;

import com.google.gson.JsonObject;
import com.willshex.blogwt.server.api.blog.action.CreatePostActionHandler;
import com.willshex.blogwt.server.api.blog.action.DeletePostActionHandler;
import com.willshex.blogwt.server.api.blog.action.DeleteResourceActionHandler;
import com.willshex.blogwt.server.api.blog.action.GetArchiveEntriesActionHandler;
import com.willshex.blogwt.server.api.blog.action.GetPostActionHandler;
import com.willshex.blogwt.server.api.blog.action.GetPostsActionHandler;
import com.willshex.blogwt.server.api.blog.action.GetRatingsActionHandler;
import com.willshex.blogwt.server.api.blog.action.GetRelatedPostsActionHandler;
import com.willshex.blogwt.server.api.blog.action.GetResourceActionHandler;
import com.willshex.blogwt.server.api.blog.action.GetResourcesActionHandler;
import com.willshex.blogwt.server.api.blog.action.GetTagsActionHandler;
import com.willshex.blogwt.server.api.blog.action.SetupBlogActionHandler;
import com.willshex.blogwt.server.api.blog.action.SubmitRatingActionHandler;
import com.willshex.blogwt.server.api.blog.action.UpdatePostActionHandler;
import com.willshex.blogwt.server.api.blog.action.UpdatePropertiesActionHandler;
import com.willshex.blogwt.server.api.blog.action.UpdateResourceActionHandler;
import com.willshex.blogwt.shared.api.blog.call.CreatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.DeletePostRequest;
import com.willshex.blogwt.shared.api.blog.call.DeleteResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.GetArchiveEntriesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetRatingsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetRelatedPostsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.GetResourcesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetTagsRequest;
import com.willshex.blogwt.shared.api.blog.call.SetupBlogRequest;
import com.willshex.blogwt.shared.api.blog.call.SubmitRatingRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdatePropertiesRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdateResourceRequest;
import com.willshex.gson.web.service.server.JsonServlet;

@SuppressWarnings("serial")
public final class BlogJsonServlet extends JsonServlet {
	@Override
	protected String processAction (String action, JsonObject request) {
		String output = "null";
		if ("GetRatings".equals(action)) {
			GetRatingsRequest input = new GetRatingsRequest();
			input.fromJson(request);
			output = new GetRatingsActionHandler().handle(input).toString();
		} else if ("SubmitRating".equals(action)) {
			SubmitRatingRequest input = new SubmitRatingRequest();
			input.fromJson(request);
			output = new SubmitRatingActionHandler().handle(input).toString();
		} else if ("UpdateResource".equals(action)) {
			UpdateResourceRequest input = new UpdateResourceRequest();
			input.fromJson(request);
			output = new UpdateResourceActionHandler().handle(input).toString();
		} else if ("GetResource".equals(action)) {
			GetResourceRequest input = new GetResourceRequest();
			input.fromJson(request);
			output = new GetResourceActionHandler().handle(input).toString();
		} else if ("GetPosts".equals(action)) {
			GetPostsRequest input = new GetPostsRequest();
			input.fromJson(request);
			output = new GetPostsActionHandler().handle(input).toString();
		} else if ("GetArchiveEntries".equals(action)) {
			GetArchiveEntriesRequest input = new GetArchiveEntriesRequest();
			input.fromJson(request);
			output = new GetArchiveEntriesActionHandler().handle(input)
					.toString();
		} else if ("DeleteResource".equals(action)) {
			DeleteResourceRequest input = new DeleteResourceRequest();
			input.fromJson(request);
			output = new DeleteResourceActionHandler().handle(input).toString();
		} else if ("GetResources".equals(action)) {
			GetResourcesRequest input = new GetResourcesRequest();
			input.fromJson(request);
			output = new GetResourcesActionHandler().handle(input).toString();
		} else if ("GetRelatedPosts".equals(action)) {
			GetRelatedPostsRequest input = new GetRelatedPostsRequest();
			input.fromJson(request);
			output = new GetRelatedPostsActionHandler().handle(input)
					.toString();
		} else if ("UpdateProperties".equals(action)) {
			UpdatePropertiesRequest input = new UpdatePropertiesRequest();
			input.fromJson(request);
			output = new UpdatePropertiesActionHandler().handle(input)
					.toString();
		} else if ("GetTags".equals(action)) {
			GetTagsRequest input = new GetTagsRequest();
			input.fromJson(request);
			output = new GetTagsActionHandler().handle(input).toString();
		} else if ("GetPost".equals(action)) {
			GetPostRequest input = new GetPostRequest();
			input.fromJson(request);
			output = new GetPostActionHandler().handle(input).toString();
		} else if ("DeletePost".equals(action)) {
			DeletePostRequest input = new DeletePostRequest();
			input.fromJson(request);
			output = new DeletePostActionHandler().handle(input).toString();
		} else if ("SetupBlog".equals(action)) {
			SetupBlogRequest input = new SetupBlogRequest();
			input.fromJson(request);
			output = new SetupBlogActionHandler().handle(input).toString();
		} else if ("CreatePost".equals(action)) {
			CreatePostRequest input = new CreatePostRequest();
			input.fromJson(request);
			output = new CreatePostActionHandler().handle(input).toString();
		} else if ("UpdatePost".equals(action)) {
			UpdatePostRequest input = new UpdatePostRequest();
			input.fromJson(request);
			output = new UpdatePostActionHandler().handle(input).toString();
		}
		return output;
	}
}