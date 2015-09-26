package com.ibm.webest.test.processing;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.webest.persistence.model.ApplicationTypeGroup;
import com.ibm.webest.persistence.model.Country;
import com.ibm.webest.persistence.model.EffortUnit;
import com.ibm.webest.persistence.model.GranularityLevel;
import com.ibm.webest.persistence.model.GranularityQuestion;
import com.ibm.webest.persistence.model.IndustrySectorGroup;
import com.ibm.webest.persistence.model.MTTDTimeUnit;
import com.ibm.webest.persistence.model.MonetaryUnit;
import com.ibm.webest.persistence.model.Organization;
import com.ibm.webest.persistence.model.StaffingShape;
import com.ibm.webest.persistence.model.Template;
import com.ibm.webest.persistence.model.User;
import com.ibm.webest.processing.administration.GuiServiceException;
import com.ibm.webest.processing.administration.GuiServiceRemote;
import com.ibm.webest.test.processing.tools.JdbcUtils;

/**
 * Testcases for GUIService
 * @author Andre Munzinger
 */
public class TestGUIService {

	private static GuiServiceRemote gui;

	@BeforeClass
	public static void beforeClass() throws FileNotFoundException,
			SQLException, ClassNotFoundException, NamingException {

		JdbcUtils.createUser();
		gui = EjbUtils.getEjb(GuiServiceRemote.class,
				TestProperties.getEjbUsername(),
				TestProperties.getEjbPassword());
	}

	@AfterClass
	public static void cleanUpClass() throws FileNotFoundException,
			SQLException, ClassNotFoundException {
		CleanUp();
		JdbcUtils.executeScript("test_data_Szenario.sql");
	}

	@Before
	public void init() throws FileNotFoundException, SQLException,
			ClassNotFoundException, NamingException {
		CleanUp();
		JdbcUtils.executeScript("test_data_Szenario.sql");
	}

	private static void CleanUp() throws SQLException, ClassNotFoundException,
			FileNotFoundException {

		JdbcUtils.executeScript("del_test_data_Szenario.sql");
	}

	@Test
	public void getAllTemplatesTest() throws GuiServiceException {
		List<Template> templates = gui.getAllTemplates();
		assertEquals(3, templates.size());
	}

	@Test
	public void getAllOrganizationsTest() throws GuiServiceException {
		List<Organization> org = gui.getAllOrganizations();
		assertEquals(2, org.size());
	}

	@Test
	public void getAllEstimatorsTest() throws GuiServiceException {
		List<User> est = gui.getAllEstimators();
		assertEquals(6, est.size());
	}

	@Test
	public void getAllCountriesTest() throws GuiServiceException {
		List<Country> c = gui.getAllCountries();
		assertEquals(239, c.size());
	}

	@Test
	public void getAllIndustrysectorGroupsTest() throws GuiServiceException {
		List<IndustrySectorGroup> isg = gui.getAllIndustrySectorGroups();
		assertEquals(3, isg.size());
	}

	@Test
	public void getAllApplicationTypeGroupsTest() throws GuiServiceException {
		List<ApplicationTypeGroup> atg = gui.getAllApplicationTypeGroups();
		assertEquals(3, atg.size());
	}

	@Test
	public void getMonetaryUnitsTest() throws GuiServiceException {
		List<MonetaryUnit> mu = gui.getAllMonetaryUnits();
		assertEquals(178, mu.size());
	}

	@Test
	public void getMttdTimeUnitsTest() throws GuiServiceException {
		List<MTTDTimeUnit> mtu = gui.getAllMttdTimeUnits();
		assertEquals(5, mtu.size());
	}

	@Test
	public void getEffortUnitsTest() throws GuiServiceException {
		List<EffortUnit> eu = gui.getAllEffortUnits();
		assertEquals(12, eu.size());
	}

	@Test
	public void getStaffingShapesTest() throws GuiServiceException {
		Collection<StaffingShape> ss = gui.getAllStaffingShapes();
		assertEquals(10, ss.size());
	}

	@Test
	public void getGranularityQuestionsTest() throws GuiServiceException {
		Map<String, Map<GranularityLevel, GranularityQuestion>> q = gui
				.getAllGranularityQuestions();
		assertTrue(q != null);
	}

	@Test
	public void getGranularityLevelsTest() throws GuiServiceException {
		List<GranularityLevel> level = gui.getAllGranularityLevels();
		assertEquals(3, level.size());
	}
}
