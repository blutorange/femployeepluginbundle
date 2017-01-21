package de.xima.fc.fuerth.employees;

import java.text.SimpleDateFormat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CheckDatePluginTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetConfigPage() {
		new CheckDatePlugin().getConfigPage();
	}

	@Test
	public final void testDate() {
		new SimpleDateFormat("k'k");
	}

}
