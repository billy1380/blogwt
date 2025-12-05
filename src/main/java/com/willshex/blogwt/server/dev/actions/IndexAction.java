package com.willshex.blogwt.server.dev.actions;

import com.willshex.blogwt.server.dev.DevAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.willshex.blogwt.server.helper.SearchHelper;
import com.willshex.blogwt.server.service.page.PageServiceProvider;
import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.server.service.search.ISearch;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.service.ServiceDiscovery;

public class IndexAction implements DevAction {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String action = request.getParameter("action");
        String group = request.getParameter("group");

        if (group == null || group.trim().isEmpty()) {
            group = "blogwt";
        }

        PageServiceProvider.provide();
        PostServiceProvider.provide();
        UserServiceProvider.provide();

        SearchHelper.indexAll((ISearch<?>) ServiceDiscovery
                .getService(group + "." + action.replace("index", "")));
    }

}
