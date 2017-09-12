//  
//  PermissionService.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//  
//
package com.willshex.blogwt.server.service.permission;

import static com.willshex.blogwt.server.helper.PersistenceHelper.id;
import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;
import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.server.helper.SearchHelper;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.PermissionSortType;
import com.willshex.blogwt.shared.api.datatype.Role;

final class PermissionService implements IPermissionService {
	public String getName () {
		return NAME;
	}

	@Override
	public Permission getPermission (Long id) {
		return id(load(), id);
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
				this, sortDirection);
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

		return PersistenceHelper.batchLookup(this, role.permissionKeys);
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
		return PersistenceHelper.pagedAndSorted(
				SearchHelper.addStartsWith(
						map(PermissionSortType.PermissionSortTypeCode),
						partialName, load()),
				start, count, sortBy, this, sortDirection);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.persistence.batch.Batcher.BatchGetter#
	 * get(java.util.Collection) */
	@Override
	public List<Permission> get (Collection<Long> ids) {
		return getIdPermissionBatch(ids);
	}

}
