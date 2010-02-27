package org.vaadin.appfoundation.test.authorization;

import java.util.UUID;

import org.vaadin.appfoundation.authorization.Resource;

public class ResourceMock implements Resource {

    private String id;

    public ResourceMock() {
        id = UUID.randomUUID().toString();
    }

    @Override
    public Object getIdentifier() {
        return id;
    }

}
