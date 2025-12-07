//  
//  Blog/BlogService.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.client.api.blog;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.willshex.blogwt.shared.api.blog.call.CreatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.CreatePostResponse;
import com.willshex.blogwt.shared.api.blog.call.DeletePostRequest;
import com.willshex.blogwt.shared.api.blog.call.DeletePostResponse;
import com.willshex.blogwt.shared.api.blog.call.DeleteResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.DeleteResourceResponse;
import com.willshex.blogwt.shared.api.blog.call.GetArchiveEntriesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetArchiveEntriesResponse;
import com.willshex.blogwt.shared.api.blog.call.GetPostRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostResponse;
import com.willshex.blogwt.shared.api.blog.call.GetPostsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostsResponse;
import com.willshex.blogwt.shared.api.blog.call.GetPropertiesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPropertiesResponse;
import com.willshex.blogwt.shared.api.blog.call.GetRatingsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetRatingsResponse;
import com.willshex.blogwt.shared.api.blog.call.GetRelatedPostsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetRelatedPostsResponse;
import com.willshex.blogwt.shared.api.blog.call.GetResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.GetResourceResponse;
import com.willshex.blogwt.shared.api.blog.call.GetResourcesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetResourcesResponse;
import com.willshex.blogwt.shared.api.blog.call.GetTagsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetTagsResponse;
import com.willshex.blogwt.shared.api.blog.call.SetupBlogRequest;
import com.willshex.blogwt.shared.api.blog.call.SetupBlogResponse;
import com.willshex.blogwt.shared.api.blog.call.SubmitRatingRequest;
import com.willshex.blogwt.shared.api.blog.call.SubmitRatingResponse;
import com.willshex.blogwt.shared.api.blog.call.UpdatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdatePostResponse;
import com.willshex.blogwt.shared.api.blog.call.UpdatePropertiesRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdatePropertiesResponse;
import com.willshex.blogwt.shared.api.blog.call.UpdateResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdateResourceResponse;
import com.willshex.gson.web.service.client.HttpException;
import com.willshex.gson.web.service.client.JsonService;

public final class BlogService extends JsonService {
	public static final String BlogMethodGetProperties = "GetProperties";

	public Request getProperties (GetPropertiesRequest input) {
		return getProperties(input, null, null);
	}

	public Request getProperties (GetPropertiesRequest input,
			AsyncSuccess<GetPropertiesRequest, GetPropertiesResponse> onSuccess) {
		return getProperties(input, onSuccess, null);
	}

	public Request getProperties (GetPropertiesRequest input,
			final AsyncCallback<GetPropertiesResponse> callback) {
		return getProperties(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request getProperties (GetPropertiesRequest input,
			AsyncSuccess<GetPropertiesRequest, GetPropertiesResponse> onSuccess,
			AsyncFailure<GetPropertiesRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(BlogMethodGetProperties, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetPropertiesResponse outputParameter = new GetPropertiesResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(BlogService.this,
										BlogMethodGetProperties, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(BlogService.this,
										BlogMethodGetProperties, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(BlogService.this,
									BlogMethodGetProperties, input, exception);
						}
					});
			onCallStart(BlogService.this, BlogMethodGetProperties, input,
					handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(BlogService.this, BlogMethodGetProperties, input,
					exception);
		}
		return handle;
	}

	public static final String BlogMethodGetRatings = "GetRatings";

	public Request getRatings (GetRatingsRequest input) {
		return getRatings(input, null, null);
	}

	public Request getRatings (GetRatingsRequest input,
			AsyncSuccess<GetRatingsRequest, GetRatingsResponse> onSuccess) {
		return getRatings(input, onSuccess, null);
	}

	public Request getRatings (GetRatingsRequest input,
			final AsyncCallback<GetRatingsResponse> callback) {
		return getRatings(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request getRatings (GetRatingsRequest input,
			AsyncSuccess<GetRatingsRequest, GetRatingsResponse> onSuccess,
			AsyncFailure<GetRatingsRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(BlogMethodGetRatings, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetRatingsResponse outputParameter = new GetRatingsResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(BlogService.this,
										BlogMethodGetRatings, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(BlogService.this,
										BlogMethodGetRatings, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(BlogService.this,
									BlogMethodGetRatings, input, exception);
						}
					});
			onCallStart(BlogService.this, BlogMethodGetRatings, input, handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(BlogService.this, BlogMethodGetRatings, input,
					exception);
		}
		return handle;
	}

	public static final String BlogMethodSubmitRating = "SubmitRating";

	public Request submitRating (SubmitRatingRequest input) {
		return submitRating(input, null, null);
	}

	public Request submitRating (SubmitRatingRequest input,
			AsyncSuccess<SubmitRatingRequest, SubmitRatingResponse> onSuccess) {
		return submitRating(input, onSuccess, null);
	}

	public Request submitRating (SubmitRatingRequest input,
			final AsyncCallback<SubmitRatingResponse> callback) {
		return submitRating(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request submitRating (SubmitRatingRequest input,
			AsyncSuccess<SubmitRatingRequest, SubmitRatingResponse> onSuccess,
			AsyncFailure<SubmitRatingRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(BlogMethodSubmitRating, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								SubmitRatingResponse outputParameter = new SubmitRatingResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(BlogService.this,
										BlogMethodSubmitRating, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(BlogService.this,
										BlogMethodSubmitRating, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(BlogService.this,
									BlogMethodSubmitRating, input, exception);
						}
					});
			onCallStart(BlogService.this, BlogMethodSubmitRating, input,
					handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(BlogService.this, BlogMethodSubmitRating, input,
					exception);
		}
		return handle;
	}

	public static final String BlogMethodUpdateResource = "UpdateResource";

	public Request updateResource (UpdateResourceRequest input) {
		return updateResource(input, null, null);
	}

	public Request updateResource (UpdateResourceRequest input,
			AsyncSuccess<UpdateResourceRequest, UpdateResourceResponse> onSuccess) {
		return updateResource(input, onSuccess, null);
	}

	public Request updateResource (UpdateResourceRequest input,
			final AsyncCallback<UpdateResourceResponse> callback) {
		return updateResource(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request updateResource (UpdateResourceRequest input,
			AsyncSuccess<UpdateResourceRequest, UpdateResourceResponse> onSuccess,
			AsyncFailure<UpdateResourceRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(BlogMethodUpdateResource, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								UpdateResourceResponse outputParameter = new UpdateResourceResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(BlogService.this,
										BlogMethodUpdateResource, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(BlogService.this,
										BlogMethodUpdateResource, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(BlogService.this,
									BlogMethodUpdateResource, input, exception);
						}
					});
			onCallStart(BlogService.this, BlogMethodUpdateResource, input,
					handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(BlogService.this, BlogMethodUpdateResource, input,
					exception);
		}
		return handle;
	}

	public static final String BlogMethodGetResource = "GetResource";

	public Request getResource (GetResourceRequest input) {
		return getResource(input, null, null);
	}

	public Request getResource (GetResourceRequest input,
			AsyncSuccess<GetResourceRequest, GetResourceResponse> onSuccess) {
		return getResource(input, onSuccess, null);
	}

	public Request getResource (GetResourceRequest input,
			final AsyncCallback<GetResourceResponse> callback) {
		return getResource(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request getResource (GetResourceRequest input,
			AsyncSuccess<GetResourceRequest, GetResourceResponse> onSuccess,
			AsyncFailure<GetResourceRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(BlogMethodGetResource, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetResourceResponse outputParameter = new GetResourceResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(BlogService.this,
										BlogMethodGetResource, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(BlogService.this,
										BlogMethodGetResource, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(BlogService.this,
									BlogMethodGetResource, input, exception);
						}
					});
			onCallStart(BlogService.this, BlogMethodGetResource, input, handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(BlogService.this, BlogMethodGetResource, input,
					exception);
		}
		return handle;
	}

	public static final String BlogMethodGetPosts = "GetPosts";

	public Request getPosts (GetPostsRequest input) {
		return getPosts(input, null, null);
	}

	public Request getPosts (GetPostsRequest input,
			AsyncSuccess<GetPostsRequest, GetPostsResponse> onSuccess) {
		return getPosts(input, onSuccess, null);
	}

	public Request getPosts (GetPostsRequest input,
			final AsyncCallback<GetPostsResponse> callback) {
		return getPosts(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request getPosts (GetPostsRequest input,
			AsyncSuccess<GetPostsRequest, GetPostsResponse> onSuccess,
			AsyncFailure<GetPostsRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(BlogMethodGetPosts, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetPostsResponse outputParameter = new GetPostsResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(BlogService.this,
										BlogMethodGetPosts, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(BlogService.this,
										BlogMethodGetPosts, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(BlogService.this, BlogMethodGetPosts,
									input, exception);
						}
					});
			onCallStart(BlogService.this, BlogMethodGetPosts, input, handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(BlogService.this, BlogMethodGetPosts, input,
					exception);
		}
		return handle;
	}

	public static final String BlogMethodGetArchiveEntries = "GetArchiveEntries";

	public Request getArchiveEntries (GetArchiveEntriesRequest input) {
		return getArchiveEntries(input, null, null);
	}

	public Request getArchiveEntries (GetArchiveEntriesRequest input,
			AsyncSuccess<GetArchiveEntriesRequest, GetArchiveEntriesResponse> onSuccess) {
		return getArchiveEntries(input, onSuccess, null);
	}

	public Request getArchiveEntries (GetArchiveEntriesRequest input,
			final AsyncCallback<GetArchiveEntriesResponse> callback) {
		return getArchiveEntries(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request getArchiveEntries (GetArchiveEntriesRequest input,
			AsyncSuccess<GetArchiveEntriesRequest, GetArchiveEntriesResponse> onSuccess,
			AsyncFailure<GetArchiveEntriesRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(BlogMethodGetArchiveEntries, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetArchiveEntriesResponse outputParameter = new GetArchiveEntriesResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(BlogService.this,
										BlogMethodGetArchiveEntries, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(BlogService.this,
										BlogMethodGetArchiveEntries, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(BlogService.this,
									BlogMethodGetArchiveEntries, input,
									exception);
						}
					});
			onCallStart(BlogService.this, BlogMethodGetArchiveEntries, input,
					handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(BlogService.this, BlogMethodGetArchiveEntries, input,
					exception);
		}
		return handle;
	}

	public static final String BlogMethodDeleteResource = "DeleteResource";

	public Request deleteResource (DeleteResourceRequest input) {
		return deleteResource(input, null, null);
	}

	public Request deleteResource (DeleteResourceRequest input,
			AsyncSuccess<DeleteResourceRequest, DeleteResourceResponse> onSuccess) {
		return deleteResource(input, onSuccess, null);
	}

	public Request deleteResource (DeleteResourceRequest input,
			final AsyncCallback<DeleteResourceResponse> callback) {
		return deleteResource(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request deleteResource (DeleteResourceRequest input,
			AsyncSuccess<DeleteResourceRequest, DeleteResourceResponse> onSuccess,
			AsyncFailure<DeleteResourceRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(BlogMethodDeleteResource, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								DeleteResourceResponse outputParameter = new DeleteResourceResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(BlogService.this,
										BlogMethodDeleteResource, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(BlogService.this,
										BlogMethodDeleteResource, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(BlogService.this,
									BlogMethodDeleteResource, input, exception);
						}
					});
			onCallStart(BlogService.this, BlogMethodDeleteResource, input,
					handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(BlogService.this, BlogMethodDeleteResource, input,
					exception);
		}
		return handle;
	}

	public static final String BlogMethodGetResources = "GetResources";

	public Request getResources (GetResourcesRequest input) {
		return getResources(input, null, null);
	}

	public Request getResources (GetResourcesRequest input,
			AsyncSuccess<GetResourcesRequest, GetResourcesResponse> onSuccess) {
		return getResources(input, onSuccess, null);
	}

	public Request getResources (GetResourcesRequest input,
			final AsyncCallback<GetResourcesResponse> callback) {
		return getResources(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request getResources (GetResourcesRequest input,
			AsyncSuccess<GetResourcesRequest, GetResourcesResponse> onSuccess,
			AsyncFailure<GetResourcesRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(BlogMethodGetResources, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetResourcesResponse outputParameter = new GetResourcesResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(BlogService.this,
										BlogMethodGetResources, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(BlogService.this,
										BlogMethodGetResources, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(BlogService.this,
									BlogMethodGetResources, input, exception);
						}
					});
			onCallStart(BlogService.this, BlogMethodGetResources, input,
					handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(BlogService.this, BlogMethodGetResources, input,
					exception);
		}
		return handle;
	}

	public static final String BlogMethodGetRelatedPosts = "GetRelatedPosts";

	public Request getRelatedPosts (GetRelatedPostsRequest input) {
		return getRelatedPosts(input, null, null);
	}

	public Request getRelatedPosts (GetRelatedPostsRequest input,
			AsyncSuccess<GetRelatedPostsRequest, GetRelatedPostsResponse> onSuccess) {
		return getRelatedPosts(input, onSuccess, null);
	}

	public Request getRelatedPosts (GetRelatedPostsRequest input,
			final AsyncCallback<GetRelatedPostsResponse> callback) {
		return getRelatedPosts(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request getRelatedPosts (GetRelatedPostsRequest input,
			AsyncSuccess<GetRelatedPostsRequest, GetRelatedPostsResponse> onSuccess,
			AsyncFailure<GetRelatedPostsRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(BlogMethodGetRelatedPosts, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetRelatedPostsResponse outputParameter = new GetRelatedPostsResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(BlogService.this,
										BlogMethodGetRelatedPosts, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(BlogService.this,
										BlogMethodGetRelatedPosts, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(BlogService.this,
									BlogMethodGetRelatedPosts, input,
									exception);
						}
					});
			onCallStart(BlogService.this, BlogMethodGetRelatedPosts, input,
					handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(BlogService.this, BlogMethodGetRelatedPosts, input,
					exception);
		}
		return handle;
	}

	public static final String BlogMethodUpdateProperties = "UpdateProperties";

	public Request updateProperties (UpdatePropertiesRequest input) {
		return updateProperties(input, null, null);
	}

	public Request updateProperties (UpdatePropertiesRequest input,
			AsyncSuccess<UpdatePropertiesRequest, UpdatePropertiesResponse> onSuccess) {
		return updateProperties(input, onSuccess, null);
	}

	public Request updateProperties (UpdatePropertiesRequest input,
			final AsyncCallback<UpdatePropertiesResponse> callback) {
		return updateProperties(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request updateProperties (UpdatePropertiesRequest input,
			AsyncSuccess<UpdatePropertiesRequest, UpdatePropertiesResponse> onSuccess,
			AsyncFailure<UpdatePropertiesRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(BlogMethodUpdateProperties, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								UpdatePropertiesResponse outputParameter = new UpdatePropertiesResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(BlogService.this,
										BlogMethodUpdateProperties, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(BlogService.this,
										BlogMethodUpdateProperties, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(BlogService.this,
									BlogMethodUpdateProperties, input,
									exception);
						}
					});
			onCallStart(BlogService.this, BlogMethodUpdateProperties, input,
					handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(BlogService.this, BlogMethodUpdateProperties, input,
					exception);
		}
		return handle;
	}

	public static final String BlogMethodGetTags = "GetTags";

	public Request getTags (GetTagsRequest input) {
		return getTags(input, null, null);
	}

	public Request getTags (GetTagsRequest input,
			AsyncSuccess<GetTagsRequest, GetTagsResponse> onSuccess) {
		return getTags(input, onSuccess, null);
	}

	public Request getTags (GetTagsRequest input,
			final AsyncCallback<GetTagsResponse> callback) {
		return getTags(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request getTags (GetTagsRequest input,
			AsyncSuccess<GetTagsRequest, GetTagsResponse> onSuccess,
			AsyncFailure<GetTagsRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(BlogMethodGetTags, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetTagsResponse outputParameter = new GetTagsResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(BlogService.this,
										BlogMethodGetTags, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(BlogService.this,
										BlogMethodGetTags, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(BlogService.this, BlogMethodGetTags,
									input, exception);
						}
					});
			onCallStart(BlogService.this, BlogMethodGetTags, input, handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(BlogService.this, BlogMethodGetTags, input,
					exception);
		}
		return handle;
	}

	public static final String BlogMethodGetPost = "GetPost";

	public Request getPost (GetPostRequest input) {
		return getPost(input, null, null);
	}

	public Request getPost (GetPostRequest input,
			AsyncSuccess<GetPostRequest, GetPostResponse> onSuccess) {
		return getPost(input, onSuccess, null);
	}

	public Request getPost (GetPostRequest input,
			final AsyncCallback<GetPostResponse> callback) {
		return getPost(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request getPost (GetPostRequest input,
			AsyncSuccess<GetPostRequest, GetPostResponse> onSuccess,
			AsyncFailure<GetPostRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(BlogMethodGetPost, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetPostResponse outputParameter = new GetPostResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(BlogService.this,
										BlogMethodGetPost, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(BlogService.this,
										BlogMethodGetPost, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(BlogService.this, BlogMethodGetPost,
									input, exception);
						}
					});
			onCallStart(BlogService.this, BlogMethodGetPost, input, handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(BlogService.this, BlogMethodGetPost, input,
					exception);
		}
		return handle;
	}

	public static final String BlogMethodDeletePost = "DeletePost";

	public Request deletePost (DeletePostRequest input) {
		return deletePost(input, null, null);
	}

	public Request deletePost (DeletePostRequest input,
			AsyncSuccess<DeletePostRequest, DeletePostResponse> onSuccess) {
		return deletePost(input, onSuccess, null);
	}

	public Request deletePost (DeletePostRequest input,
			final AsyncCallback<DeletePostResponse> callback) {
		return deletePost(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request deletePost (DeletePostRequest input,
			AsyncSuccess<DeletePostRequest, DeletePostResponse> onSuccess,
			AsyncFailure<DeletePostRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(BlogMethodDeletePost, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								DeletePostResponse outputParameter = new DeletePostResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(BlogService.this,
										BlogMethodDeletePost, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(BlogService.this,
										BlogMethodDeletePost, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(BlogService.this,
									BlogMethodDeletePost, input, exception);
						}
					});
			onCallStart(BlogService.this, BlogMethodDeletePost, input, handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(BlogService.this, BlogMethodDeletePost, input,
					exception);
		}
		return handle;
	}

	public static final String BlogMethodSetupBlog = "SetupBlog";

	public Request setupBlog (SetupBlogRequest input) {
		return setupBlog(input, null, null);
	}

	public Request setupBlog (SetupBlogRequest input,
			AsyncSuccess<SetupBlogRequest, SetupBlogResponse> onSuccess) {
		return setupBlog(input, onSuccess, null);
	}

	public Request setupBlog (SetupBlogRequest input,
			final AsyncCallback<SetupBlogResponse> callback) {
		return setupBlog(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request setupBlog (SetupBlogRequest input,
			AsyncSuccess<SetupBlogRequest, SetupBlogResponse> onSuccess,
			AsyncFailure<SetupBlogRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(BlogMethodSetupBlog, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								SetupBlogResponse outputParameter = new SetupBlogResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(BlogService.this,
										BlogMethodSetupBlog, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(BlogService.this,
										BlogMethodSetupBlog, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(BlogService.this, BlogMethodSetupBlog,
									input, exception);
						}
					});
			onCallStart(BlogService.this, BlogMethodSetupBlog, input, handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(BlogService.this, BlogMethodSetupBlog, input,
					exception);
		}
		return handle;
	}

	public static final String BlogMethodCreatePost = "CreatePost";

	public Request createPost (CreatePostRequest input) {
		return createPost(input, null, null);
	}

	public Request createPost (CreatePostRequest input,
			AsyncSuccess<CreatePostRequest, CreatePostResponse> onSuccess) {
		return createPost(input, onSuccess, null);
	}

	public Request createPost (CreatePostRequest input,
			final AsyncCallback<CreatePostResponse> callback) {
		return createPost(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request createPost (CreatePostRequest input,
			AsyncSuccess<CreatePostRequest, CreatePostResponse> onSuccess,
			AsyncFailure<CreatePostRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(BlogMethodCreatePost, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								CreatePostResponse outputParameter = new CreatePostResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(BlogService.this,
										BlogMethodCreatePost, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(BlogService.this,
										BlogMethodCreatePost, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(BlogService.this,
									BlogMethodCreatePost, input, exception);
						}
					});
			onCallStart(BlogService.this, BlogMethodCreatePost, input, handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(BlogService.this, BlogMethodCreatePost, input,
					exception);
		}
		return handle;
	}

	public static final String BlogMethodUpdatePost = "UpdatePost";

	public Request updatePost (UpdatePostRequest input) {
		return updatePost(input, null, null);
	}

	public Request updatePost (UpdatePostRequest input,
			AsyncSuccess<UpdatePostRequest, UpdatePostResponse> onSuccess) {
		return updatePost(input, onSuccess, null);
	}

	public Request updatePost (UpdatePostRequest input,
			final AsyncCallback<UpdatePostResponse> callback) {
		return updatePost(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request updatePost (UpdatePostRequest input,
			AsyncSuccess<UpdatePostRequest, UpdatePostResponse> onSuccess,
			AsyncFailure<UpdatePostRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(BlogMethodUpdatePost, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								UpdatePostResponse outputParameter = new UpdatePostResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(BlogService.this,
										BlogMethodUpdatePost, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(BlogService.this,
										BlogMethodUpdatePost, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(BlogService.this,
									BlogMethodUpdatePost, input, exception);
						}
					});
			onCallStart(BlogService.this, BlogMethodUpdatePost, input, handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(BlogService.this, BlogMethodUpdatePost, input,
					exception);
		}
		return handle;
	}
}