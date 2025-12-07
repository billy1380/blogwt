package com.willshex.blogwt.server.dev.actions;

import com.willshex.blogwt.server.dev.DevAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.helper.RoleHelper;

public class FixRolesAction implements DevAction {
    private static final Logger LOG = Logger
            .getLogger(FixRolesAction.class.getName());

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Collection<Role> all = RoleHelper.createAll();
        all.stream().forEach(role -> {
            Role loaded = RoleServiceProvider.provide().getCodeRole(role.code);

            if (loaded == null || loaded.id == null) {
                RoleServiceProvider.provide().addRole(role);
            }

            if (role.permissions != null) {
                role.permissions.stream().forEach(i -> {
                    Permission lp = PermissionServiceProvider.provide()
                            .getCodePermission(i.code);

                    if (lp == null) {
                        if (LOG.isLoggable(Level.WARNING)) {
                            LOG.warning("Could not find permission with code ["
                                    + i.code
                                    + "], might want to run [fixpermissions] action");
                        }
                    } else {
                        if (loaded.permissions == null) {
                            loaded.permissions = new ArrayList<>();
                        }

                        loaded.permissions.add(lp);
                    }
                });
                RoleServiceProvider.provide().updateRole(loaded);
            }
        });
    }

}
