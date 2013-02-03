package org.vaadin.appfoundation.test;

import org.easymock.EasyMock;
import org.mockito.Mockito;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

/**
 * Mock implementation of a Vaadin application
 * 
 * @author Kim
 * 
 */
public class MockUI extends UI {

	private static final long serialVersionUID = -7592972194969208126L;

	public MockUI() {
		init(null);
	}

	@Override
	protected void init(VaadinRequest request) {
		UI.setCurrent(this);
		setSession(new VaadinSession(null));
		setId("unittest");
		request = Mockito.mock(VaadinRequest.class);
		Mockito.when(request.getParameter("v-loc")).thenReturn(
				"http://localhost/unittest/");
		Mockito.when(request.getParameter("v-cw")).thenReturn("1024");
		Mockito.when(request.getParameter("v-ch")).thenReturn("1024");

		getPage().init(request);
	}

}
