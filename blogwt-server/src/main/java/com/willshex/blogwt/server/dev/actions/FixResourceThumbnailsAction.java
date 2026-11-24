package com.willshex.blogwt.server.dev.actions;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.willshex.blogwt.server.background.thumbnail.ThumbnailServlet;
import com.willshex.blogwt.server.dev.DevAction;
import com.willshex.blogwt.server.helper.ThumbnailHelper;
import com.willshex.blogwt.server.service.resource.ResourceServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Resource;

/**
 * Puts a thumbnail message on the queue for every resource that has a stored
 * image and no thumbnail. Without <code>apply=true</code> it only lists them.
 */
public class FixResourceThumbnailsAction implements DevAction {
	private static final Logger LOG = Logger
			.getLogger(FixResourceThumbnailsAction.class.getName());

	@Override
	public void handle (HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean apply = "true".equals(request.getParameter("apply"));
		List<Resource> resources = ResourceServiceProvider.provide()
				.getResources(Integer.valueOf(0),
						Integer.valueOf(Integer.MAX_VALUE), null, null);
		StringBuilder out = new StringBuilder();
		int wanted = 0;

		for (Resource resource : resources) {
			if (resource.data == null || !resource.data.startsWith("gs://")) {
				continue;
			}

			JsonObject properties = ThumbnailHelper.propertiesOf(resource);

			if (properties.has(ThumbnailHelper.THUMBNAIL_STATIC_URL)) {
				continue;
			}

			wanted++;
			out.append(resource.id).append(' ').append(resource.name)
					.append('\n');

			if (apply) {
				ThumbnailServlet.queueCreate(resource.id);
			}
		}

		out.insert(0, (apply ? "Queued " : "Would queue ") + wanted
				+ " thumbnail(s)" + (apply ? "" : " (add apply=true)") + "\n");
		LOG.info(out.toString());
		response.setContentType("text/plain");
		response.getWriter().print(out);
	}

}
