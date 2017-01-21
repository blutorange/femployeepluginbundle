package de.xima.fc.fuerth.employees;

import java.util.Locale;

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
	public final void testMessage() {
		System.out.println(Messages.msg(CmnCnst.MSG_ERROR_DAO_STATE_LIST, Locale.ENGLISH));
		System.out.println(Messages.fmt(CmnCnst.MSG_ERROR_DAO_STATE_LIST, Locale.ENGLISH, "myProject"));
		System.out.println(Messages.msg(CmnCnst.MSG_ERROR_DAO_STATE_LIST, Locale.GERMAN));
		System.out.println(Messages.fmt(CmnCnst.MSG_ERROR_DAO_STATE_LIST, Locale.GERMAN, "myProject"));
	}

}
