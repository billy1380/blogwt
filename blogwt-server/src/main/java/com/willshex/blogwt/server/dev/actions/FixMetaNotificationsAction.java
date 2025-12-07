package com.willshex.blogwt.server.dev.actions;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.willshex.blogwt.server.dev.DevAction;
import com.willshex.blogwt.server.service.metanotification.MetaNotificationServiceProvider;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;
import com.willshex.blogwt.shared.helper.MetaNotificationHelper;

public class FixMetaNotificationsAction implements DevAction {
    private static final Logger LOG = Logger
            .getLogger(FixMetaNotificationsAction.class.getName());

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<MetaNotification> metas = MetaNotificationHelper.createAll();

        for (MetaNotification meta : metas) {
            if (MetaNotificationServiceProvider.provide()
                    .getCodeMetaNotification(meta.code) == null) {
                meta = MetaNotificationServiceProvider.provide()
                        .addMetaNotification(meta);
                LOG.info("added meta notification [" + meta.code + "] with id ["
                        + meta.id + "]");
            } else {
                LOG.info("Meta notification [" + meta.code + "] already exists");
            }
        }
    }

}
