//  
//  RoleService.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//  
//
package com.willshex.blogwt.server.service.role;

import static com.willshex.blogwt.server.helper.PersistenceHelper.id;
import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;
import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.LoadType;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.server.helper.SearchHelper;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.RoleSortType;

final class RoleService implements IRoleService {

	public String getName() {
		return NAME;
	}

	@Override
	public Role getRole(Long id) {
		return id(load(), id);
	}

	private LoadType<Role> load() {
		return provide().load().type(Role.class);
	}

	@Override
	public Role addRole(Role role) {
		if (role.created == null) {
			role.created = new Date();
		}

		Key<Role> key = provide().save().entity(role).now();
		role.id = keyToId(key);

		return role;
	}

	@Override
	public Role updateRole(Role role) {
		if (role.permissions != null) {
			role.permissionKeys = new ArrayList<>();

			for (Permission permission : role.permissions) {
				role.permissionKeys.add(ObjectifyService.key(permission));
			}
		}

		provide().save().entity(role).now();
		return role;
	}

	@Override
	public void deleteRole(Role role) {
		provide().delete().entity(role);
	}

	@Override
	public List<Role> getRoles(Integer start, Integer count,
			RoleSortType sortBy, SortDirectionType sortDirection) {
		return PersistenceHelper.pagedAndSorted(load(), start, count, sortBy,
				this, sortDirection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.role.IRoleService#getRolesCount()
	 */
	@Override
	public Long getRolesCount() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.role.IRoleService#getIdRoleBatch(java.
	 * lang.Iterable)
	 */
	@Override
	public List<Role> getIdRoleBatch(Iterable<Long> roleIds) {
		return new ArrayList<Role>(load().ids(roleIds).values());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.role.IRoleService#getCodeRole(java
	 * .lang.String)
	 */
	@Override
	public Role getCodeRole(String code) {
		return PersistenceHelper
				.one(load().filter(map(RoleSortType.RoleSortTypeCode), code));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.role.IRoleService#getPartialNameRoles
	 * (java.lang.String, java.lang.Integer, java.lang.Integer,
	 * com.willshex.blogwt.shared.api.datatype.RoleSortType,
	 * com.willshex.blogwt.shared.api.SortDirectionType)
	 */
	@Override
	public List<Role> getPartialNameRoles(String partialName, Integer start,
			Integer count, RoleSortType sortBy,
			SortDirectionType sortDirection) {
		// FIXME: this will not work because name is not indexed
		return PersistenceHelper.pagedAndSorted(
				SearchHelper.addStartsWith("name", partialName, load()), start,
				count, sortBy, this, sortDirection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.persistence.batch.Batcher.BatchGetter#
	 * get(java.lang.Iterable)
	 */
	@Override
	public List<Role> get(Iterable<Long> ids) {
		return getIdRoleBatch(ids);
	}

}
