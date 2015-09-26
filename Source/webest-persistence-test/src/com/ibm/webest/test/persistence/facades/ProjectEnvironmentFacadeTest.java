package com.ibm.webest.test.persistence.facades;

import static org.junit.Assert.*;


import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.webest.persistence.model.ApplicationType;
import com.ibm.webest.persistence.model.ApplicationTypeGroup;
import com.ibm.webest.persistence.model.Country;
import com.ibm.webest.persistence.model.DefectCategory;
import com.ibm.webest.persistence.model.Division;
import com.ibm.webest.persistence.model.EffortUnit;
import com.ibm.webest.persistence.model.IndustrySector;
import com.ibm.webest.persistence.model.IndustrySectorGroup;
import com.ibm.webest.persistence.model.MTTDTimeUnit;
import com.ibm.webest.persistence.model.MeasuringUnit;
import com.ibm.webest.persistence.model.Milestone;
import com.ibm.webest.persistence.model.MonetaryUnit;
import com.ibm.webest.persistence.model.Organization;
import com.ibm.webest.persistence.model.Phase;
import com.ibm.webest.persistence.model.Template;
import com.ibm.webest.persistence.service.DatabaseConnectionException;
import com.ibm.webest.persistence.service.EntityNotFoundException;
import com.ibm.webest.persistence.service.ProjectEnvironmentFacadeRemote;
import com.ibm.webest.test.persistence.EjbUtils;
import com.ibm.webest.test.persistence.JdbcUtils;
import com.ibm.webest.test.persistence.TestProperties;

/**
 * JUnit tests for the ProjectEnvironmentFacade session bean. 
 * @author Gregor Schumm
 *
 */
public class ProjectEnvironmentFacadeTest {
	private static ProjectEnvironmentFacadeRemote facade;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		JdbcUtils.createUser();
		facade = EjbUtils.getEjb(ProjectEnvironmentFacadeRemote.class, TestProperties.getEjbUsername(), TestProperties.getEjbPassword());
	}

	@Test
	public void testGetCountriesEmpty() throws DatabaseConnectionException,
			SQLException, ClassNotFoundException {
		clearTestData(Country.class.getSimpleName());
		List<Country> lst = facade.getCountries();
		assertEquals(0, lst.size());
	}

	@Test
	public void testGetCountries() throws DatabaseConnectionException,
			SQLException, ClassNotFoundException, FileNotFoundException {
		clearTestData(Country.class.getSimpleName());
		insertTestData(Country.class.getSimpleName());
		List<Country> lst = facade.getCountries();
		List<String[]> testCountries = getTestCountries();
		assertEquals(testCountries.size(), lst.size());
		for (int i = 0; i < testCountries.size(); i++) {
			String[] candidate = new String[] { lst.get(i).getCode(),
					lst.get(i).getName() };
			assertArrayEquals(testCountries.get(i), candidate);
		}
	}

	private List<String[]> getTestCountries() throws SQLException,
			ClassNotFoundException {
		Connection jdbcConnection = JdbcUtils.getJdbcConnection();
		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement
				.executeQuery("select * from country order by name");
		List<String[]> countries = new ArrayList<String[]>();
		while (rs.next()) {
			String code = rs.getString("code");
			String name = rs.getString("name");
			countries.add(new String[] { code, name });
		}
		return countries;
	}

	private static void insertTestData(String entity)
			throws FileNotFoundException, SQLException, ClassNotFoundException {
		JdbcUtils.executeScript("test_data_" + entity + ".sql");
	}

	private static void clearTestData(String entity) throws SQLException,
			ClassNotFoundException {
		Connection jdbcConnection = JdbcUtils.getJdbcConnection();
		Statement statement = jdbcConnection.createStatement();
		statement.execute("delete from " + entity);
		statement.close();
		jdbcConnection.commit();
	}

	@Test
	public void testGetMonetaryUnitsEmpty() throws SQLException,
			ClassNotFoundException, DatabaseConnectionException {
		clearTestData(MonetaryUnit.class.getSimpleName());
		assertEquals(0, facade.getMonetaryUnits().size());
	}

	@Test
	public void testGetMonetaryUnits() throws SQLException,
			ClassNotFoundException, FileNotFoundException,
			DatabaseConnectionException {
		String entity = MonetaryUnit.class.getSimpleName();
		clearTestData(entity);
		insertTestData(entity);
		List<? extends MeasuringUnit> lst = facade.getMonetaryUnits();
		List<Object[]> testUnits = getTestUnits(entity);
		assertEquals(testUnits.size(), lst.size());
		for (int i = 0; i < testUnits.size(); i++) {
			MeasuringUnit unit = lst.get(i);
			Object[] candidate = new Object[] { unit.getId(), unit.getName(),
					unit.isReference(), unit.getFactor() };
			assertArrayEquals(testUnits.get(i), candidate);
		}
	}

	@Test
	public void testGetEffortUnitsEmpty() throws SQLException,
			ClassNotFoundException, DatabaseConnectionException {
		clearTestData(EffortUnit.class.getSimpleName());
		assertEquals(0, facade.getEffortUnits().size());
	}

	@Test
	public void testGetEffortUnits() throws SQLException,
			ClassNotFoundException, FileNotFoundException,
			DatabaseConnectionException {
		String entity = EffortUnit.class.getSimpleName();
		clearTestData(entity);
		insertTestData(entity);
		List<? extends MeasuringUnit> lst = facade.getEffortUnits();
		List<Object[]> testUnits = getTestUnits(entity);
		assertEquals(testUnits.size(), lst.size());
		for (int i = 0; i < testUnits.size(); i++) {
			MeasuringUnit unit = lst.get(i);
			Object[] candidate = new Object[] { unit.getId(), unit.getName(),
					unit.isReference(), unit.getFactor() };
			assertArrayEquals(testUnits.get(i), candidate);
		}
	}

	@Test
	public void testGetMTTDTimeUnitsEmpty() throws SQLException,
			ClassNotFoundException, DatabaseConnectionException {
		clearTestData(MTTDTimeUnit.class.getSimpleName());
		assertEquals(0, facade.getMTTDTimeUnits().size());
	}

	@Test
	public void testGetMTTDTimeUnits() throws SQLException,
			ClassNotFoundException, FileNotFoundException,
			DatabaseConnectionException {
		String entity = MTTDTimeUnit.class.getSimpleName();
		clearTestData(entity);
		insertTestData(entity);
		List<? extends MeasuringUnit> lst = facade.getMTTDTimeUnits();
		List<Object[]> testUnits = getTestUnits(entity);
		assertEquals(testUnits.size(), lst.size());
		for (int i = 0; i < testUnits.size(); i++) {
			MeasuringUnit unit = lst.get(i);
			Object[] candidate = new Object[] { unit.getId(), unit.getName(),
					unit.isReference(), unit.getFactor() };
			assertArrayEquals(testUnits.get(i), candidate);
		}
	}

	private List<Object[]> getTestUnits(String entity) throws SQLException,
			ClassNotFoundException {
		Connection jdbcConnection = JdbcUtils.getJdbcConnection();
		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("select * from " + entity
				+ " order by name");
		List<Object[]> units = new ArrayList<Object[]>();
		while (rs.next()) {
			String id = rs.getString("id");
			String name = rs.getString("name");
			boolean reference = rs.getBoolean("reference");
			double factor = rs.getDouble("factor");
			units.add(new Object[] { id, name, reference, factor });
		}
		rs.close();
		statement.close();
		return units;
	}

	@Test
	public void testGetApplicationTypeGroupsEmpty() throws SQLException,
			ClassNotFoundException, DatabaseConnectionException {
		clearTestData(ApplicationTypeGroup.class.getSimpleName());
		assertEquals(0, facade.getApplicationTypeGroups().size());
	}

	@Test
	public void testGetApplicationTypeGroups() throws SQLException,
			ClassNotFoundException, DatabaseConnectionException {
		clearTestData(ApplicationTypeGroup.class.getSimpleName());
		Object[] validGroups = createRandomGroups(ApplicationTypeGroup.class
				.getSimpleName());
		List<ApplicationTypeGroup> groups = facade.getApplicationTypeGroups();
		assertEquals(validGroups.length, groups.size());
		for (int i = 0; i < validGroups.length; i++) {
			ApplicationTypeGroup group = groups.get(i);
			Object[] children = (Object[]) ((Object[]) validGroups[i])[2];
			for (int j = 0; j < children.length; j++) {
				ApplicationType type = group.getApplicationTypes().get(j);
				Object[] actual = new Object[] { type.getName(),
						type.getGroup().getId() };
				assertArrayEquals((Object[]) children[j], actual);
			}
			assertEquals(((Object[]) validGroups[i])[0], group.getId());
			assertEquals(((Object[]) validGroups[i])[1], group.getName());
		}
		clearTestData(ApplicationTypeGroup.class.getSimpleName());
	}

	private static Object[] createRandomGroups(String entityName)
			throws SQLException, ClassNotFoundException {
		Random rand = new Random();
		String childEntityName = entityName.replace("Group", "");
		int max = 20;
		Object[] arr = new Object[rand.nextInt(max)];
		Connection con = JdbcUtils.getJdbcConnection();
		Statement stmt = con.createStatement();
		for (int i = 0; i < arr.length; i++) {
			Object[] children = new Object[rand.nextInt(max)];
			int id = i + 1;
			String groupName = entityName + " " + id;
			stmt.execute("INSERT INTO " + entityName + " (id, name) VALUES ("
					+ id + ", '" + groupName + "')");
			for (int j = 0; j < children.length; j++) {
				int childId = j + 1;
				String childName = childEntityName + " [" + id + ", " + childId
						+ "]";
				stmt.execute("INSERT INTO " + childEntityName
						+ " (\"GROUP\", name) VALUES (" + id + ", '"
						+ childName + "')");
				children[j] = new Object[] { childName, id };
			}
			Arrays.sort(children, new Comparator<Object>() {
				@Override
				public int compare(Object object1, Object object2) {
					return ((String) ((Object[]) object1)[0])
							.compareTo((String) ((Object[]) object2)[0]);
				}
			});
			arr[i] = new Object[] { id, groupName, children };
		}
		stmt.close();
		con.commit();
		Arrays.sort(arr, new Comparator<Object>() {
			@Override
			public int compare(Object object1, Object object2) {
				return ((String) ((Object[]) object1)[1])
						.compareTo((String) ((Object[]) object2)[1]);
			}
		});
		return arr;
	}

	@Test
	public void testGetIndustrySectorGroupsEmpty() throws SQLException,
			ClassNotFoundException, DatabaseConnectionException {
		clearTestData(IndustrySectorGroup.class.getSimpleName());
		assertEquals(0, facade.getIndustrySectorGroups().size());
	}

	@Test
	public void testGetIndustrySectorGroups() throws SQLException,
			ClassNotFoundException, DatabaseConnectionException {
		clearTestData(IndustrySectorGroup.class.getSimpleName());
		Object[] validGroups = createRandomGroups(IndustrySectorGroup.class
				.getSimpleName());
		List<IndustrySectorGroup> groups = facade.getIndustrySectorGroups();
		assertEquals(validGroups.length, groups.size());
		for (int i = 0; i < validGroups.length; i++) {
			IndustrySectorGroup group = groups.get(i);
			Object[] children = (Object[]) ((Object[]) validGroups[i])[2];
			for (int j = 0; j < children.length; j++) {
				IndustrySector type = group.getIndustrySectors().get(j);
				Object[] actual = new Object[] { type.getName(),
						type.getGroup().getId() };
				assertArrayEquals((Object[]) children[j], actual);
			}
			assertEquals(((Object[]) validGroups[i])[0], group.getId());
			assertEquals(((Object[]) validGroups[i])[1], group.getName());
		}
		clearTestData(IndustrySectorGroup.class.getSimpleName());
	}

	@Test
	public void testGetOrganizationsEmpty() throws Exception {
		clearTestData(Organization.class.getSimpleName());
		assertEquals(0, facade.getOrganizations().size());
	}

	@Test
	public void testGetOrganizations() throws Exception {
		clearTestData(Organization.class.getSimpleName());
		Object[] validOrgs = createRandomOrganizations();
		List<Organization> orgs = facade.getOrganizations();
		assertEquals(validOrgs.length, orgs.size());
		for (int i = 0; i < validOrgs.length; i++) {
			Organization org = orgs.get(i);
			Object[] children = (Object[]) ((Object[]) validOrgs[i])[2];
			for (int j = 0; j < children.length; j++) {
				Division type = org.getDivisions().get(j);
				Object[] actual = new Object[] { type.getName(),
						type.getOrganization().getId() };
				assertArrayEquals((Object[]) children[j], actual);
			}
			assertEquals(((Object[]) validOrgs[i])[0], org.getId());
			assertEquals(((Object[]) validOrgs[i])[1], org.getName());
		}
		clearTestData(Organization.class.getSimpleName());
	}

	private static Object[] createRandomOrganizations() throws SQLException,
			ClassNotFoundException {
		Random rand = new Random();
		String entityName = "Organization";
		String childEntityName = "Division";
		int max = 20;
		Object[] arr = new Object[rand.nextInt(max)];
		Connection con = JdbcUtils.getJdbcConnection();
		Statement stmt = con.createStatement();
		for (int i = 0; i < arr.length; i++) {
			Object[] children = new Object[rand.nextInt(max)];
			int id = i + 1;
			String groupName = entityName + " " + id;
			stmt.execute("INSERT INTO " + entityName + " (id, name) VALUES ("
					+ id + ", '" + groupName + "')");
			for (int j = 0; j < children.length; j++) {
				int childId = (j + 1)+id*max;
				String childName = childEntityName + " [" + id + ", " + childId
						+ "]";
				stmt.execute("INSERT INTO " + childEntityName
						+ " (id, organization, name) VALUES (" + childId + ", "
						+ id + ", '" + childName + "')");
				children[j] = new Object[] { childName, id };
			}
			Arrays.sort(children, new Comparator<Object>() {
				@Override
				public int compare(Object object1, Object object2) {
					return ((String) ((Object[]) object1)[0])
							.compareTo((String) ((Object[]) object2)[0]);
				}
			});
			arr[i] = new Object[] { id, groupName, children };
		}
		stmt.close();
		con.commit();
		Arrays.sort(arr, new Comparator<Object>() {
			@Override
			public int compare(Object object1, Object object2) {
				return ((String) ((Object[]) object1)[1])
						.compareTo((String) ((Object[]) object2)[1]);
			}
		});
		return arr;
	}

	@Test
	public void testGetTemplatesEmpty() throws SQLException, ClassNotFoundException, DatabaseConnectionException {
		clearTestData(Organization.class.getSimpleName());
		assertEquals(0, facade.getOrganizations().size());
	}
	
	@Test
	public void testGetTemplates() throws SQLException, ClassNotFoundException, DatabaseConnectionException {
		clearTestData(Template.class.getSimpleName());
		clearTestData(Milestone.class.getSimpleName());
		clearTestData(Phase.class.getSimpleName());
		clearTestData(DefectCategory.class.getSimpleName());
		Object[] validTemplates = createRandomTemplates();
		List<Template> templates = facade.getTemplates();
		assertEquals(validTemplates.length, templates.size());
		for (int i = 0; i < validTemplates.length; i++) {
			Template tpl = templates.get(i);
			Object[] phases = (Object[]) ((Object[]) validTemplates[i])[5];
			Object[] defectCategories = (Object[]) ((Object[]) validTemplates[i])[7];
			for (int j = 0; j < phases.length; j++) {
				Phase phase = tpl.getPhases().get(j);
				Object[] actual = new Object[] {phase.getId(), phase.getOwner().getId(), phase.getName(), phase.getAcronym()};
				assertArrayEquals((Object[]) phases[j], actual);
			}

			List<DefectCategory> defCats = new ArrayList<DefectCategory>(tpl.getDefectCategories());
			Collections.sort(defCats, new Comparator<DefectCategory>() {

				@Override
				public int compare(DefectCategory object1,
						DefectCategory object2) {
					return object1.getId()-object2.getId();
				}
			});
			
			for (int j = 0; j < defectCategories.length; j++) {
				DefectCategory defCat = defCats.get(j);
				Object[] actual = new Object[] {defCat.getOwner().getId(), defCat.getName(), defCat.isIncluded() };
				assertArrayEquals((Object[]) defectCategories[j], actual);
			}
			
			assertEquals(((Object[]) validTemplates[i])[0], tpl.getId());
			assertEquals(((Object[]) validTemplates[i])[1], tpl.getName());
			assertEquals(((Object[]) validTemplates[i])[2], tpl.getDaysPerWeek());
			assertEquals(((Object[]) validTemplates[i])[3], tpl.getHoursPerDay());
		}
		clearTestData(Template.class.getSimpleName());
		clearTestData(Milestone.class.getSimpleName());
		clearTestData(Phase.class.getSimpleName());
		clearTestData(DefectCategory.class.getSimpleName());
	}

	private static Object[] createRandomTemplates() throws SQLException,
			ClassNotFoundException {
		int phase=0;
		Random rand = new Random();
		String entityName = "Template";
		String phaseName = "Phase";
		String milestoneName = "Milestone";
		String defCat = "DefectCategory";
		int max = 20;
		Object[] arr = new Object[rand.nextInt(max)];
		Connection con = JdbcUtils.getJdbcConnection();
		Statement stmt = con.createStatement();
		for (int i = 0; i < arr.length; i++) {
			Object[] phases = new Object[4];
			Object[] milestones = new Object[rand.nextInt(max)];
			Object[] defectCategories = new Object[rand.nextInt(max)];
			int id = i + 1;
			String templateName = entityName + " " + id;
			byte daysPerWeek = (byte) (rand.nextInt(7)+1);
			byte hoursPerDay = (byte) (rand.nextInt(24)+1);
			String description = RandomStringUtils.random(rand.nextInt(4000));
			description=description.replace("'", "''");
			String parentSql = "INSERT INTO " + entityName + " (id, name, daysperweek, hoursperday, description) VALUES ("
			+ id + ", '" + templateName + "', "+daysPerWeek+", "+hoursPerDay+", '"+description+"')";
			stmt.execute(parentSql);
			for (int j = 0; j < phases.length; j++) {
				int childId = (j + 1)+id*max;
				String childName = phaseName + " [" + id + ", " + childId
						+ "]";
				String childDescription = RandomStringUtils.random(rand.nextInt(4000));
				childDescription=childDescription.replace("'", "''");
				String acronym = RandomStringUtils.randomAlphabetic(rand.nextInt(10));
				String sql = "INSERT INTO " + phaseName
				+ " (id, number, owner, name, acronym, description) VALUES (" + childId + ", " + (j+1) + ", "
				+ id + ", '" + childName + "', '"+acronym+"', '"+childDescription+"')";
				stmt.execute(sql);
				phase = childId;
				phases[j] = new Object[] {childId, id, childName, acronym};
			}
			for (int j = 0; j < milestones.length; j++) {
				int childId = (j + 1)+id*max;
				String childName = milestoneName + " [" + id + ", " + childId
						+ "]";
				String childDescription = RandomStringUtils.random(rand.nextInt(4000));
				childDescription=childDescription.replace("'", "''");
				String acronym = RandomStringUtils.randomAlphabetic(rand.nextInt(10));
				int percentage = rand.nextInt(101);
				//int phase = childId;
				int included = rand.nextInt(2);
				String sql = "INSERT INTO " + milestoneName
				+ " (milestoneid, owner, name, acronym, description, percentage, phase, included) VALUES (" + childId + ", "
				+ id + ", '" + childName + "', '"+acronym+"', '"+childDescription+"', "+percentage+", "+phase+", "+included+")";
				stmt.execute(sql);
				milestones[j] = new Object[] { childId, id, childName, acronym, percentage, phase, included == 1 ? true : false };
			}
			for (int j = 0; j < defectCategories.length; j++) {
				int childId = (j + 1)+id*max;
				String childName = defCat + " [" + id + ", " + childId
						+ "]";
				int percentage = rand.nextInt(101);
				int included = rand.nextInt(2);
				String sql = "INSERT INTO " + defCat
				+ " (id, owner, name, percentage, included) VALUES (" + childId + ", "
				+ id + ", '" + childName + "', "+percentage+", "+included+")";
				stmt.execute(sql);
				defectCategories[j] = new Object[] {id, childName, included == 1 ? true : false };
			}
			arr[i] = new Object[] { id, templateName, daysPerWeek, hoursPerDay, description, phases, milestones, defectCategories };
		}
		stmt.close();
		con.commit();
		Arrays.sort(arr, new Comparator<Object>() {
			@Override
			public int compare(Object object1, Object object2) {
				return ((String) ((Object[]) object1)[1])
						.compareTo((String) ((Object[]) object2)[1]);
			}
		});
		return arr;
	}
	
	@Test
	public void testSaveTemplate() throws DatabaseConnectionException {
		List<Template> result = facade.getTemplates();
		assertEquals(0, result.size());
		
		Template t = new Template();
		
		t.setDaysPerWeek((byte) 5);
		t.setHoursPerDay((byte) 8);
		t.setName("TestTemplate");
		t.setDescription("TestDescription");
		
		facade.saveTemplate(t);
		
		result = facade.getTemplates();
		assertEquals(1, result.size());
		
		assertEquals("TestTemplate", result.get(0).getName());
		assertEquals("TestDescription", result.get(0).getDescription());
	}
	
	@Test
	public void testGetReferenceUnit() throws EntityNotFoundException, DatabaseConnectionException {
		MeasuringUnit u = facade.getReferenceUnit(MTTDTimeUnit.class);
		assertTrue(u instanceof MTTDTimeUnit);
		assertTrue(u.isReference());
		u = facade.getReferenceUnit(EffortUnit.class);
		assertTrue(u instanceof EffortUnit);
		assertTrue(u.isReference());
		u = facade.getReferenceUnit(MonetaryUnit.class);
		assertTrue(u instanceof MonetaryUnit);
		assertTrue(u.isReference());
	}
}