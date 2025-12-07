package com.willshex.blogwt.server.dev.actions;

import com.willshex.blogwt.server.dev.DevAction;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.helper.PermissionHelper;

public class FixPermissionsAction implements DevAction {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Collection<Permission> all = PermissionHelper.createAll();

        Permission loaded;
        for (Permission permission : all) {
            loaded = PermissionServiceProvider.provide()
                    .getCodePermission(permission.code);

            if (loaded == null || loaded.id == null) {
                PermissionServiceProvider.provide().addPermission(permission);
            }
        }
    }

}
