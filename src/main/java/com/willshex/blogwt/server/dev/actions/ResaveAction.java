package com.willshex.blogwt.server.dev.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.willshex.blogwt.server.background.resave.ResaveServlet;
import com.willshex.blogwt.server.dev.DevAction;

public class ResaveAction implements DevAction {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String typeName = request.getParameter("type");
        ResaveServlet.queueForResaving(typeName);
    }

}
