package org.vaadin.appfoundation.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.vaadin.appfoundation.test.authorization.AuthorizationUtilTest;
import org.vaadin.appfoundation.test.i18n.FillXmlTest;
import org.vaadin.appfoundation.test.i18n.InternationalizationServletTest;
import org.vaadin.appfoundation.test.i18n.LangTest;
import org.vaadin.appfoundation.test.i18n.TranslationUtilTest;
import org.vaadin.appfoundation.test.persistence.FacadeFactoryTest;
import org.vaadin.appfoundation.test.view.AbstractViewTest;
import org.vaadin.appfoundation.test.view.DefaultViewFactoryTest;
import org.vaadin.appfoundation.test.view.ViewHandlerTest;
import org.vaadin.appfoundation.test.view.ViewItemTest;

@RunWith(Suite.class)
@SuiteClasses( { AuthorizationUtilTest.class, FillXmlTest.class,
        InternationalizationServletTest.class, LangTest.class,
        TranslationUtilTest.class, ViewItemTest.class,
        DefaultViewFactoryTest.class, ViewHandlerTest.class,
        AbstractViewTest.class, FacadeFactoryTest.class })
public class TestSuite {

}
