//  
//  PermissionService.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//  
//
package com.willshex.blogwt.server.service.permission;

import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;
import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.server.helper.SearchHelper;
import com.willshex.blogwt.server.service.ISortable;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.PermissionSortType;
import com.willshex.blogwt.shared.api.datatype.Role;

final class PermissionService
		implements IPermissionService, ISortable<PermissionSortType> {
	public String getName () {
		return NAME;
	}

	@Override
	public Permission getPermission (Long id) {
		return load().id(id.longValue()).now();
	}

	@Override
	public Permission addPermission (Permission permission) {
		if (permission.created == null) {
			permission.created = new Date();
		}

		Key<Permission> key = provide().save().entity(permission).now();
		permission.id = keyToId(key);

		return permission;
	}

	@Override
	public Permission updatePermission (Permission permission) {
		provide().save().entity(permission).now();
		return permission;
	}

	@Override
	public void deletePermission (Permission permission) {
		provide().delete().entity(permission).now();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.permission.IPermissionService#
	 * getPermissions(java.lang.Integer, java.lang.Integer,
	 * com.willshex.blogwt.shared.api.datatype.PermissionSortType,
	 * com.willshex.blogwt.shared.api.SortDirectionType) */
	@Override
	public List<Permission> getPermissions (Integer start, Integer count,
			PermissionSortType sortBy, SortDirectionType sortDirection) {
		return PersistenceHelper.pagedAndSorted(load(), start, count, sortBy,
				this, sortDirection).list();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.permission.IPermissionService#
	 * getPermissionsCount() */
	@Override
	public Long getPermissionsCount () {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.permission.IPermissionService#
	 * getIdPermissionsBatch(java.util.Collection) */
	@Override
	public List<Permission> getIdPermissionBatch (
			Collection<Long> permissionIds) {
		return new ArrayList<Permission>(load().ids(permissionIds).values());
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.permission.IPermissionService#
	 * getCodePermission(java.lang.String) */
	@Override
	public Permission getCodePermission (String code) {
		return PersistenceHelper.one(load().filter(
				PermissionSortType.PermissionSortTypeCode.toString(), code));
	}

	/**
	 * @return 
	 * @return
	 */
	private LoadType<Permission> load () {
		return provide().load().type(Permission.class);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.permission.IPermissionService#
	 * getRolePermissions(com.willshex.blogwt.shared.api.datatype.Role) */
	@Override
	public List<Permission> getRolePermissions (Role role) {
		if (role.permissionKeys == null) {
			role = RoleServiceProvider.provide().getRole(role.id);
		}

		return getIdPermissionBatch(
				PersistenceHelper.keysToIds(role.permissionKeys));
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.permission.IPermissionService#
	 * getPartialNamePermissions(java.lang.String, java.lang.Integer,
	 * java.lang.Integer,
	 * com.willshex.blogwt.shared.api.datatype.PermissionSortType,
	 * com.willshex.blogwt.shared.api.SortDirectionType) */
	@Override
	public List<Permission> getPartialNamePermissions (String partialName,
			Integer start, Integer count, PermissionSortType sortBy,
			SortDirectionType sortDirection) {
		Query<Permission> query = PersistenceHelper.pagedAndSorted(load(),
				start, count, sortBy, this, sortDirection);

		query = SearchHelper.addStartsWith(
				map(PermissionSortType.PermissionSortTypeCode), partialName,
				query);

		return query.list();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.ISortable#map(java.lang.Enum) */
	@Override
	public String map (PermissionSortType sortBy) {
		return sortBy.toString();
	}

}