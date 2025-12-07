package com.willshex.blogwt.server.dev.actions;

import com.willshex.blogwt.server.dev.DevAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.willshex.blogwt.server.api.blog.action.GetPostsActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.shared.api.blog.call.GetPostsRequest;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.utility.JsonUtils;

public class GetPostsAction implements DevAction {

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response)
                        throws Exception {
                response.getOutputStream()
                                .print(JsonUtils.beautifyJson((new GetPostsActionHandler())
                                                .handle((GetPostsRequest) new GetPostsRequest()
                                                                .showAll(Boolean.TRUE)
                                                                .pager(PagerHelper.createDefaultPager())
                                                                .accessCode(ApiValidator.DEV_ACCESS_CODE))
                                                .toString()));
        }

}
