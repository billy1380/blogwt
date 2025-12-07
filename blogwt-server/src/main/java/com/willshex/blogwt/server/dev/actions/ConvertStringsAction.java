package com.willshex.blogwt.server.dev.actions;

import com.willshex.blogwt.server.dev.DevAction;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class ConvertStringsAction implements DevAction {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        List<String> kinds = Arrays.asList("ArchiveEntry", "Comment", "Field",
                "Form", "GeneratedDownload", "MetaNotification", "Notification",
                "NotificationSetting", "Page", "Permission", "Post",
                "PostContent", "Property", "PushToken", "Rating", "Reaction",
                "Relationship", "Resource", "Role", "Session", "Tag", "User");

        for (String kind : kinds) {
            Query q = new Query(kind);
            PreparedQuery pq = ds.prepare(q);
            for (Entity entity : pq.asIterable()) {
                boolean changed = false;
                Entity newEntity = null;

                // Handle ID
                Key key = entity.getKey();
                if (key.getId() != 0) {
                    newEntity = new Entity(kind, String.valueOf(key.getId()));
                    newEntity.setPropertiesFrom(entity);
                    changed = true;
                } else {
                    newEntity = entity;
                }

                // Handle Date properties
                Map<String, Object> properties = newEntity.getProperties();
                for (Map.Entry<String, Object> entry : properties.entrySet()) {
                    if (entry.getValue() instanceof Date) {
                        newEntity.setProperty(entry.getKey(),
                                ((Date) entry.getValue()).toInstant()
                                        .toString());
                        changed = true;
                    }
                }

                if (changed) {
                    ds.put(newEntity);
                    if (key.getId() != 0) {
                        ds.delete(key);
                    }
                }
            }
        }
    }

}
