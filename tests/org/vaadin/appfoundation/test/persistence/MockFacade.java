package org.vaadin.appfoundation.test.persistence;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.vaadin.appfoundation.persistence.data.AbstractPojo;
import org.vaadin.appfoundation.persistence.facade.IFacade;

public class MockFacade implements IFacade {

    public void close() {

    }

    public void delete(AbstractPojo pojo) {

    }

    public <A extends AbstractPojo> void deleteAll(Collection<A> pojos) {
    }

    public <A extends AbstractPojo> A find(Class<A> clazz, Long id) {
        return null;
    }

    public <A extends AbstractPojo> A find(String queryStr,
            Map<String, Object> parameters) {
        return null;
    }

    public void init(String name) {

    }

    public void kill() {

    }

    public <A extends AbstractPojo> List<A> list(Class<A> clazz) {
        return null;
    }

    public <A extends AbstractPojo> List<A> list(String queryStr,
            Map<String, Object> parameters) {
        return null;
    }

    public <A extends AbstractPojo> void refresh(A pojo) {

    }

    public void store(AbstractPojo pojo) {

    }

    public <A extends AbstractPojo> void storeAll(Collection<A> pojos) {

    }

    public Long count(Class<? extends AbstractPojo> c) {
        return 0L;
    }

}
