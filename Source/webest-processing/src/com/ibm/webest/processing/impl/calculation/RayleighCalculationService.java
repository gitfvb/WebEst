package com.ibm.webest.processing.impl.calculation;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.ibm.webest.processing.administration.AuthenticationServiceLocal;
import com.ibm.webest.processing.calculation.RayleighCalculationException;
import com.ibm.webest.processing.calculation.RayleighCalculationServiceLocal;
import com.ibm.webest.processing.calculation.RayleighCalculationServiceRemote;
import com.ibm.webest.processing.model.RayleighResult;

/**
 * RayleighCalculationService
 * Creates rayleigh curves for reports
 * 
 * @author Andre Munzinger
 * @author David Dornseifer
 * @version 1.0
 */
@DeclareRoles({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
	AuthenticationServiceLocal.ROLE_ESTIMATOR,
	AuthenticationServiceLocal.ROLE_MANAGER })
@RolesAllowed({ AuthenticationServiceLocal.ROLE_ADMINISTRATOR,
	AuthenticationServiceLocal.ROLE_ESTIMATOR,
	AuthenticationServiceLocal.ROLE_MANAGER })
@Stateless
public class RayleighCalculationService implements RayleighCalculationServiceLocal, RayleighCalculationServiceRemote{

	//instantiate the logger 
	private Logger logger = Logger.getLogger(RayleighCalculationService.class);
	
	
	/**
	 * calcRayleighSpreading
	 * Calculates the people which are required for the project
	 * Effort_Time = (Putnam Effort(month) / td ^ 2 ) * (Months/12) * EXP((- (Months/12) ^ 2) / (2 * td ^ 2))
	 * @param pEff Putnam-Effort in Months
	 * @param td Implementationtime in months
	 * @param time Elapsed Time for project
	 * @return
	 * @throws RayleighCalculationException 
	 */
	private Float calcRayleighSpreading(float pEff, float td, float time) throws RayleighCalculationException{
		
		if(pEff == 0){
			this.logger.error("calcRayleighSpreading: Putnam effort is zero");
			throw new RayleighCalculationException("Putnam effort is zero.");
		}else if(td == 0){
			this.logger.error("calcRayleighSpreading: Td(COCOMO Time) is zero");
			throw new RayleighCalculationException("Td(COCOMO Time) is zero.");
		}else if(time == 0){
			this.logger.error("calcRayleighSpreading: Iteration time is zero");
			throw new RayleighCalculationException("Iteration time is zero.");
		}
		
		//calculate the rayleigh value step for step
		float result = (float) ((pEff * 12/ Math.pow(td / 12, 2)) * (time / 12) * Math.pow(Math.E, ( - Math.pow(time / 12, 2) / (2 * Math.pow(td / 12, 2)))));
		
		return result;
	}
	
	
	/**
	 * COCOMOResultService
	 * Organizes the whole COCOMO-Calculationprocess
	 * @return endresult of the COCOMO-Calculation
	 * @throws RayleighCalculationException 
	 */
	public RayleighResult calc(float pEff, float td) throws RayleighCalculationException{
			
		if(pEff == 0){
			this.logger.error("calc: Putnam effort is zero");
			throw new RayleighCalculationException("Putnam effort is zero.");
		} else if(td == 0){
			this.logger.error("calc: Td(COCOMO Time) is zero");
			throw new RayleighCalculationException("Td(COCOMO Time) is zero.");
		}
		
		//list objects
		List<List<Double>> xResult = new ArrayList<List<Double>>();
		List<List<Double>> yResult =  new ArrayList<List<Double>>();
		List<Double> xTmp;
		List<Double> yTmp;
		
		List<String> xString = new ArrayList<String>();
		String tmpString;
		
		//create new data object, which keeps values for charts 
		RayleighResult result;
		
		//calculate the value until td * 3 is reached 
		for(int i = 1; i <= 3 * td; i++ ){
			
			//add new month
			xTmp = new ArrayList<Double>();
			xTmp.add((double) i);
			xTmp.add((double) td);
			xResult.add(xTmp);
			
			//put in the resulting value
			yTmp = new ArrayList<Double>(); 
			yTmp.add((double)calcRayleighSpreading(pEff, td, (float) i));
			yTmp.add((double)calcRayleighSpreading(pEff, td, (float) i));
			yResult.add(yTmp);
			
			tmpString = String.valueOf(i);
			xString.add(tmpString);
		}
		//apply a new Rayleigh result object to result
		result = new RayleighResult(yResult, xResult, xString);
		
		return result;
	}
}