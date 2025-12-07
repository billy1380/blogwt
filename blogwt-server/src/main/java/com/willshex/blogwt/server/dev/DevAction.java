package com.willshex.blogwt.server.dev;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface DevAction {
    void handle(HttpServletRequest request, HttpServletResponse response)
            throws Exception;
}
