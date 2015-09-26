package com.ibm.webest.test.processing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * This class started all test classes from processing.
 * 
 * @author Andre Munzinger, Wail Shakir
 * 
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({

TestCOCOMOCalculationService.class, TestConstraintsService.class,
		TestConversion.class, TestEstimationManagerService.class,
		TestGUIService.class, TestPICalcService.class,
		TestPutnamCalculationService.class,
		TestRayleighCalculationService.class,
		TestReportCalculationService.class, TestSizingCalculatorService.class })
public class AllTests {

}