package org.vaadin.appfoundation.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.vaadin.appfoundation.test.authentication.AuthenticationUtilTest;
import org.vaadin.appfoundation.test.authentication.PasswordUtilTest;
import org.vaadin.appfoundation.test.authentication.SessionHandlerTest;
import org.vaadin.appfoundation.test.authentication.UserTest;
import org.vaadin.appfoundation.test.authentication.UserUtilTest;
import org.vaadin.appfoundation.test.authorization.AuthorizationUtilTest;
import org.vaadin.appfoundation.test.i18n.FillXmlTest;
import org.vaadin.appfoundation.test.i18n.InternationalizationServletTest;
import org.vaadin.appfoundation.test.i18n.LangTest;
import org.vaadin.appfoundation.test.i18n.TranslationUtilTest;
import org.vaadin.appfoundation.test.persistence.AbstractPojoTest;
import org.vaadin.appfoundation.test.persistence.FacadeFactoryTest;
import org.vaadin.appfoundation.test.persistence.JPAFacadeTest;
import org.vaadin.appfoundation.test.view.AbstractViewTest;
import org.vaadin.appfoundation.test.view.DefaultViewFactoryTest;
import org.vaadin.appfoundation.test.view.ViewHandlerTest;
import org.vaadin.appfoundation.test.view.ViewItemTest;

@RunWith(Suite.class)
@SuiteClasses( { PasswordUtilTest.class, AuthorizationUtilTest.class,
        FillXmlTest.class, InternationalizationServletTest.class,
        LangTest.class, TranslationUtilTest.class, ViewItemTest.class,
        DefaultViewFactoryTest.class, ViewHandlerTest.class,
        AbstractViewTest.class, FacadeFactoryTest.class, JPAFacadeTest.class,
        AbstractPojoTest.class, UserTest.class, AuthenticationUtilTest.class,
        SessionHandlerTest.class, UserUtilTest.class })
public class TestSuite {

}
