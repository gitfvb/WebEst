package com.ibm.webest.persistence.service;

import java.util.List;

import javax.ejb.Remote;

import com.ibm.webest.persistence.model.User;

/**
 * Remote Facade Interface for entity User.
 * @author Dirk Kohlweyer
 * 
 */
@Remote
public interface UserFacadeRemote {
	/**
	 * @return the user object related to the passed id
	 * @throws EntityNotFoundException there is no user related to the passed id
	 * @throws DatabaseConnectionException an error occurred while connecting to the database
	 */
	public User getUserById(String id) throws EntityNotFoundException, DatabaseConnectionException;
	/**
	 * @return all users that have the given roles
	 * @throws DatabaseConnectionException an error occurred while connecting to the database
	 */
	public List<User> getUsersByRole(String... role) throws EntityNotFoundException, DatabaseConnectionException;;
}
