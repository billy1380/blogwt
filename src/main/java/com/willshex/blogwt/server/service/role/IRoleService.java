//  
//  IRoleService.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//  
//
package com.willshex.blogwt.server.service.role;

import java.util.List;

import com.willshex.blogwt.server.service.ISortable;
import com.willshex.blogwt.server.service.persistence.batch.Batcher.BatchGetter;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.RoleSortType;
import com.willshex.service.IService;

public interface IRoleService extends IService, BatchGetter<Role>, ISortable<RoleSortType> {

	public static final String NAME = "blogwt.role";

	/**
	 * @param id
	 * @return
	 */
	public Role getRole (Long id);

	/**
	 * @param role
	 * @return
	 */
	public Role addRole (Role role);

	/**
	 * @param role
	 * @return
	 */
	public Role updateRole (Role role);

	/**
	 * @param role
	 */
	public void deleteRole (Role role);

	/**
	 * Gets the role using a lookup code
	 * 
	 * @param code
	 *            the code name for the role
	 * @return
	 */
	public Role getCodeRole (String code);

	/**
	 * @param pager
	 * @return
	 */
	public List<Role> getRoles (Integer start, Integer count,
			RoleSortType sortBy, SortDirectionType sortDirection);

	/**
	 * @return
	 */
	public Long getRolesCount ();

	/**
	 * @param roles
	 * @return
	 */
	public List<Role> getIdRoleBatch (Iterable<Long> roleIds);

	/**
	 * 
	 * @param query
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public List<Role> getPartialNameRoles (String partialName, Integer start,
			Integer count, RoleSortType sortBy,
			SortDirectionType sortDirection);

}
