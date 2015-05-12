//  
//  IPermissionService.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//  
//
package com.willshex.blogwt.server.service.permission;

import java.util.Collection;
import java.util.List;

import com.spacehopperstudios.service.IService;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.PermissionSortType;
import com.willshex.blogwt.shared.api.datatype.Role;

public interface IPermissionService extends IService {

	public static final String NAME = "blogwt.permission";

	/**
	 * @param id
	 * @return
	 */
	public Permission getPermission (Long id);

	/**
	 * @param permission
	 * @return
	 */
	public Permission addPermission (Permission permission);

	/**
	 * @param permission
	 * @return
	 */
	public Permission updatePermission (Permission permission);

	/**
	 * @param permission
	 */
	public void deletePermission (Permission permission);

	/**
	 * Gets the permission using a lookup code
	 * 
	 * @param code
	 *            the code name for the permission
	 * @return
	 */
	public Permission getCodePermission (String code);

	/**
	 * @param pager
	 * @return
	 */
	public List<Permission> getPermissions (Integer start, Integer count,
			PermissionSortType sortBy, SortDirectionType sortDirection);

	/**
	 * @return
	 */
	public Long getPermissionsCount ();

	/**
	 * @param permissions
	 * @return
	 */
	public List<Permission> getIdPermissionsBatch (
			Collection<Long> permissionIds);

	/**
	 * Get permissions in a role
	 * @param role
	 * @return
	 */
	public List<Permission> getRolePermissions (Role role);

}