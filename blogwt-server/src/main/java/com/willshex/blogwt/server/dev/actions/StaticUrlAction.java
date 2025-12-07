package com.willshex.blogwt.server.dev.actions;

import com.willshex.blogwt.server.dev.DevAction;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.willshex.blogwt.server.service.resource.ResourceServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Resource;

public class StaticUrlAction implements DevAction {
    private static final Logger LOG = Logger
            .getLogger(StaticUrlAction.class.getName());

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<Resource> resources = ResourceServiceProvider.provide()
                .getResources(Integer.valueOf(0),
                        Integer.valueOf(Integer.MAX_VALUE), null, null);
        JsonObject object;
        for (Resource resource : resources) {
            if (resource.properties != null) {
                if (resource.properties.contains(":image")) {
                    resource.properties = resource.properties
                            .replace(":image", ":\"image").replace("}", "\"}");
                }

                object = JsonParser.parseString(resource.properties)
                        .getAsJsonObject();
            } else {
                object = new JsonObject();
            }

            if (!object.has("staticUrl") || object.get("staticUrl").getAsString()
                    .startsWith("http")) {
                try {
                    object.addProperty("staticUrl", ImagesServiceFactory
                            .getImagesService()
                            .getServingUrl(ServingUrlOptions.Builder
                                    .withBlobKey(new BlobKey(resource.data
                                            .replace("gs://", ""))))
                            .replaceFirst("https:\\/\\/", "//")
                            .replaceFirst("http:\\/\\/", "//"));
                } catch (Throwable e) {
                    if (LOG.isLoggable(Level.FINE)) {
                        LOG.fine("Could not update resource");
                    }
                }

                resource.properties = object.toString();

                ResourceServiceProvider.provide().updateResource(resource);
            }
        }
    }

}
