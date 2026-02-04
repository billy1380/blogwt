package com.willshex.blogwt.server.dev.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import com.willshex.blogwt.server.dev.DevAction;
import com.willshex.blogwt.server.tool.Exporter;

public class ExportAction implements DevAction {

	@Override
	public void handle (HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String path = request.getParameter("path");
		if (path == null) {
			path = "export";
		}

		new Exporter(path).exportAll();
		response.getWriter()
				.write("Exported to " + new File(path).getAbsolutePath());
	}

}
