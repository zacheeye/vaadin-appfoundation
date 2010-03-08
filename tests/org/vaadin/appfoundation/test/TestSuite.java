package org.vaadin.appfoundation.test;

import org.junit.runners.Suite.SuiteClasses;
import org.vaadin.appfoundation.test.authorization.AuthorizationUtilTest;
import org.vaadin.appfoundation.test.i18n.InternationalizationServletTest;

@SuiteClasses( { AuthorizationUtilTest.class, InternationalizationServletTest.class })
public class TestSuite {

}
