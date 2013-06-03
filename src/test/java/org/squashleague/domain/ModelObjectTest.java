package org.squashleague.domain;

import com.google.common.collect.Maps;
import org.junit.Test;
import org.squashleague.domain.account.Role;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertSame;

public class ModelObjectTest {

    @Test
    public void shouldMap() {
        List<ModelObject> modelObjects = Arrays.asList(new Role().withId(1l), new Role().withId(3l), new Role().withId(2l));

        Map actual = Maps.uniqueIndex(modelObjects, ModelObject.TO_MAP);

        assertSame(actual.get(1l), modelObjects.get(0));
        assertSame(actual.get(3l), modelObjects.get(1));
        assertSame(actual.get(2l), modelObjects.get(2));
    }
}
