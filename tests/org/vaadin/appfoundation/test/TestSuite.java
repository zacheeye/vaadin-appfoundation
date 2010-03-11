package org.vaadin.appfoundation.test;

import org.junit.runners.Suite.SuiteClasses;
import org.vaadin.appfoundation.test.authorization.AuthorizationUtilTest;
import org.vaadin.appfoundation.test.i18n.FillXmlTest;
import org.vaadin.appfoundation.test.i18n.InternationalizationServletTest;

@SuiteClasses( { AuthorizationUtilTest.class, FillXmlTest.class,
        InternationalizationServletTest.class })
public class TestSuite {

}
