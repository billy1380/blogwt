package com.willshex.blogwt.server.dev.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.willshex.blogwt.server.dev.DevAction;
import com.willshex.blogwt.server.service.generateddownload.GeneratedDownloadServiceProvider;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownload;
import com.willshex.blogwt.shared.page.Stack;
import com.willshex.blogwt.shared.page.search.Filter;

public class GenAndShowDownloadAction implements DevAction {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String idParam = request.getParameter("id");
        Long id = Long.valueOf(idParam);
        GeneratedDownload d = GeneratedDownloadServiceProvider.provide()
                .getGeneratedDownload(id);
        Stack stack = Stack.parse(d.parameters);
        Filter filter = Filter.fromStack(stack);
        switch (filter.type) {
            default:
                break;
        }
    }

}
