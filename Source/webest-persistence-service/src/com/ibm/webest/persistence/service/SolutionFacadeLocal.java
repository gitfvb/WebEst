package com.ibm.webest.persistence.service;

import javax.ejb.Local;

/**
 * facade local interface for solution <br>
 * SolutionFacadeLocal includes the query to Solution from SolutionFacadeRemote.
 * 
 * @author Wail Shakir
 * 
 */
@Local
public interface SolutionFacadeLocal extends SolutionFacadeRemote {

}
