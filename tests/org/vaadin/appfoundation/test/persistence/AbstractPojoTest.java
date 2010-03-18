package org.vaadin.appfoundation.test.persistence;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AbstractPojoTest {

    @Test
    public void id() {
        MockPojo pojo = new MockPojo();
        pojo.setId(1L);

        assertEquals(new Long(1), pojo.getId());
        pojo.setId(2L);
        assertEquals(new Long(2), pojo.getId());
    }

    @Test
    public void consistencyVersion() {
        MockPojo pojo = new MockPojo();
        pojo.setConsistencyVersion(1L);

        assertEquals(new Long(1), pojo.getConsistencyVersion());
        pojo.setConsistencyVersion(2L);
        assertEquals(new Long(2), pojo.getConsistencyVersion());
    }
}
