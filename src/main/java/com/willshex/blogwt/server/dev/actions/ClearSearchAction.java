package com.willshex.blogwt.server.dev.actions;

import com.willshex.blogwt.server.dev.DevAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.willshex.blogwt.server.helper.SearchHelper;
import com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider;

public class ClearSearchAction implements DevAction {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        PersistenceServiceProvider.provide();

        String name = request.getParameter("index");
        String ids = request.getParameter("ids");

        String namespace = request.getParameter("ns");

        String[] split = ids.split(",");

        for (String id : split) {
            SearchHelper.deleteSearch(
                    () -> namespace == null ? false
                            : Boolean.valueOf(namespace).booleanValue(),
                    name, id);
        }
    }

}
