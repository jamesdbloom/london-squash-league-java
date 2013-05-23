package org.squashleague.dao.account;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.*;

/**
 * @author jamesdbloom
 */
@RunWith(MockitoJUnitRunner.class)
public class UserDAOTest {

    @Mock
    protected EntityManager entityManager;
    @InjectMocks
    private UserDAO userDAO = new UserDAO();

    @Test
    public void shouldReturnNullIfNotObjectFound() throws Exception {
        // given
        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createQuery(any(String.class), eq(User.class))).thenReturn(query);
        when(query.getResultList()).thenThrow(new NoResultException());

        // then
        assertNull(userDAO.findByEmail("email"));
    }

    @Test
    public void shouldReturnNullWhenEmptyListReturned() throws Exception {
        // given
        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createQuery(any(String.class), eq(User.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<User>());

        // then
        assertNull(userDAO.findByEmail("email"));
    }

    @Test
    public void shouldReturnObjectWhenFound() throws Exception {
        // given
        User user = new User().withName("user name");
        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createQuery(any(String.class), eq(User.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(user));

        // then
        assertEquals(user, userDAO.findByEmail("email"));
    }

    @Test
    public void shouldReturnPersistRoleWhenItDoesNotExist() throws Exception {
        // given
        Role newRole = new Role()
                .withName("ROLE_NAME")
                .withDescription("role description");
        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createQuery(any(String.class), eq(Role.class))).thenReturn(query);
        when(query.getResultList()).thenThrow(new NoResultException());

        // then
        assertEquals(newRole, userDAO.findOrCreateRole(newRole));
        verify(entityManager).persist(same(newRole));
    }

    @Test
    public void shouldReturnPersistRoleWhenEmptyListReturned() throws Exception {
        // given
        Role newRole = new Role()
                .withName("ROLE_NAME")
                .withDescription("role description");
        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createQuery(any(String.class), eq(Role.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<Role>());

        // then
        assertEquals(newRole, userDAO.findOrCreateRole(newRole));
        verify(entityManager).persist(same(newRole));
    }

    @Test
    public void shouldReturnRoleWhenItDoesExist() throws Exception {
        // given
        Role newRole = new Role()
                .withName("ROLE_NAME")
                .withDescription("role description");
        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createQuery(any(String.class), eq(Role.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(newRole));

        // then
        assertEquals(newRole, userDAO.findOrCreateRole(newRole));
        verify(entityManager, times(0)).persist(newRole);
    }

    @Test
    public void shouldReturnFirstRoleWhenMultipleExist() throws Exception {
        // given
        Role newRole = new Role()
                .withName("ROLE_NAME")
                .withDescription("role description");
        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createQuery(any(String.class), eq(Role.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(newRole,
                new Role()
                        .withName("OTHER_ROLE")
                        .withDescription("another role")));

        // then
        assertEquals(newRole, userDAO.findOrCreateRole(newRole));
        verify(entityManager, times(0)).persist(newRole);
    }

}
