package com.willshex.blogwt.server.dev.actions;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.willshex.blogwt.server.dev.DevAction;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.helper.RoleHelper;

public class AdminAction implements DevAction {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        User user = UserServiceProvider.provide()
                .getUsernameUser(request.getParameter("user"));
        UserServiceProvider.provide().addUserRolesAndPermissions(user,
                Arrays.asList(RoleServiceProvider.provide()
                        .getCodeRole(RoleHelper.ADMIN)),
                null);
    }

}
