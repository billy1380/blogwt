package com.willshex.blogwt.server.dev.actions;

import com.willshex.blogwt.server.dev.DevAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.willshex.blogwt.server.service.post.PostServiceProvider;

public class ClearLinksAction implements DevAction {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        PostServiceProvider.provide().clearLinks();
    }

}
