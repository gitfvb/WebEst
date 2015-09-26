package com.ibm.webest.persistence.service.impl;

import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.webest.persistence.model.User;
import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.AuthenticationRoles;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.persistence.service.UserFacadeLocal;
import com.ibm.webest.persistence.service.UserFacadeRemote;

/**
 * Facade for entity User
 * 
 * @author Dirk Kohlweyer
 */
@DeclareRoles({ AuthenticationRoles.ROLE_ADMINISTRATOR, AuthenticationRoles.ROLE_ESTIMATOR,
		AuthenticationRoles.ROLE_MANAGER })
@RolesAllowed({ AuthenticationRoles.ROLE_ADMINISTRATOR, AuthenticationRoles.ROLE_ESTIMATOR,
		AuthenticationRoles.ROLE_MANAGER })
@Stateless
public class UserFacade implements UserFacadeRemote, UserFacadeLocal {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public User getUserById(String id) throws EntityNotFoundException,
			DatabaseConnectionException {
		User user;
		try {
			user = entityManager.find(User.class, id);
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
		if (user == null)
			throw new EntityNotFoundException("User <" + id + "> not found");

		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUsersByRole(String... roles)
			throws EntityNotFoundException, DatabaseConnectionException {
		if (roles.length == 0)
			throw new EntityNotFoundException("No role names given.");
		try {
			StringBuffer sql = new StringBuffer("select o from User o where ");
			for (int i = 0; i < roles.length; i++) {
				if (i > 0)
					sql.append(" or ");
				sql.append("o.role.id = :role");
				sql.append(i);
			}
			sql.append(" order by o.lastName, o.firstName");
			Query q = entityManager
					.createQuery(sql.toString());
			for (int i = 0; i < roles.length; i++) {
				q.setParameter("role"+i, roles[i]);
			}
			return q.getResultList();
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

}
