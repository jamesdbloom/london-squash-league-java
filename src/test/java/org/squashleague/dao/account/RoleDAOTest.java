package org.squashleague.dao.account;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;
import org.squashleague.domain.account.Role;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author jamesdbloom
 */
@RunWith(MockitoJUnitRunner.class)
public class RoleDAOTest {

    @Mock
    protected EntityManager entityManager;
    @InjectMocks
    private RoleDAO roleDAO = new RoleDAO();

    @Test
    public void shouldReturnNullIfNotObjectFound() throws Exception {
        // given
        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createQuery(any(String.class), eq(Role.class))).thenReturn(query);
        when(query.getResultList()).thenThrow(new EmptyResultDataAccessException(0));

        // then
        assertNull(roleDAO.findByName("name"));
    }

    @Test
    public void shouldReturnNullWhenEmptyListReturned() throws Exception {
        // given
        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createQuery(any(String.class), eq(Role.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<Role>());

        // then
        assertNull(roleDAO.findByName("name"));
    }

    @Test
    public void shouldReturnObjectWhenFound() throws Exception {
        // given
        Role role = new Role().withName("role name");
        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createQuery(any(String.class), eq(Role.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(role));

        // then
        assertEquals(role, roleDAO.findByName("name"));
    }

}
