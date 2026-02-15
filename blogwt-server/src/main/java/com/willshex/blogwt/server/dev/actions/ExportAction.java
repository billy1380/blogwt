package com.willshex.blogwt.server.dev.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.willshex.blogwt.server.dev.DevAction;
import com.willshex.blogwt.server.tool.Exporter;

public class ExportAction implements DevAction {

	@Override
	public void handle (HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		response.setContentType("application/x-gtar");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"export.tar.gz\"");

		new Exporter(response.getOutputStream()).exportAll();
	}

}
