package com.willshex.blogwt.server.dev.actions;

import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.willshex.blogwt.server.dev.DevAction;
import com.willshex.blogwt.server.service.property.IPropertyService;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.helper.PropertyHelper;

public class FixPropertiesAction implements DevAction {
    private static final Logger LOG = Logger
            .getLogger(FixPropertiesAction.class.getName());

    private static final String POST_ENABLE_EMOJI = "post.enable.emoji";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IPropertyService service = PropertyServiceProvider.provide();

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        out.println(fixComments(service));
        out.println(removeEmoji(service));
    }

    private String fixComments(IPropertyService service) {
        Property comments = service
                .getNamedProperty(PropertyHelper.POST_COMMENTS_ENABLED);

        if (comments == null) {
            service.addProperty(PropertyHelper
                    .createPostCommentsEnabled(Boolean.TRUE.toString()));

            return log("added [" + PropertyHelper.POST_COMMENTS_ENABLED
                    + "] as [true]");
        }

        String before = comments.value;
        String after = PropertyHelper.normaliseCommentsEnabled(before);

        if (after.equals(before) && "boolean".equals(comments.type)) {
            return log("[" + PropertyHelper.POST_COMMENTS_ENABLED
                    + "] already [" + after + "]");
        }

        comments.value = after;
        comments.type = "boolean";
        service.updateProperty(comments);

        return log("changed [" + PropertyHelper.POST_COMMENTS_ENABLED
                + "] from [" + before + "] to [" + after + "]");
    }

    private String removeEmoji(IPropertyService service) {
        Property emoji = service.getNamedProperty(POST_ENABLE_EMOJI);

        if (emoji == null) {
            return log("[" + POST_ENABLE_EMOJI + "] not present");
        }

        service.deleteProperty(emoji);

        return log("deleted [" + POST_ENABLE_EMOJI + "]");
    }

    private String log(String message) {
        LOG.info(message);

        return message;
    }
}
