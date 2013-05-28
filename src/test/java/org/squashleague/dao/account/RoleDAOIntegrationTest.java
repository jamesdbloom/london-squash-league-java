package org.squashleague.dao.account;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.squashleague.configuration.RootConfiguration;
import org.squashleague.dao.league.ClubDAO;
import org.squashleague.dao.league.HSQLApplicationContextInitializer;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.league.Club;
import org.squashleague.service.security.AdministratorLoggedInTest;

import javax.annotation.Resource;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author jamesdbloom
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = RootConfiguration.class, initializers = HSQLApplicationContextInitializer.class)
public class RoleDAOIntegrationTest extends AdministratorLoggedInTest {

    @Resource
    private RoleDAO roleDAO;
    @Resource
    private ClubDAO clubDAO;
    private Club club;

    @Before
    public void setupDatabase() {
        club = new Club()
                .withName("club name")
                .withAddress("address");
        clubDAO.save(club);
    }

    @After
    public void teardownDatabase() {
        clubDAO.delete(club);
    }

    @Test
    public void shouldSaveRequiredFieldsAndRetrieveByName() throws Exception {
        // given
        Role expectedRole = new Role()
                .withName("role name")
                .withDescription("role description");

        // when
        roleDAO.save(expectedRole);

        // then
        Role actualRole = roleDAO.findById(expectedRole.getId());
        try {
            assertEquals(expectedRole, actualRole);
        } finally {
            roleDAO.delete(expectedRole);
        }
    }

    @Test
    public void shouldSaveUpdateAndRetrieveById() throws Exception {
        // given
        Role expectedRole = new Role()
                .withName("role name")
                .withDescription("role description");
        roleDAO.save(expectedRole);
        expectedRole
                .withName("new role name")
                .withDescription("new role description");

        // when
        roleDAO.update(expectedRole);

        // then
        Role actualRole = roleDAO.findById(expectedRole.getId());
        try {
            assertEquals(expectedRole.incrementVersion(), actualRole);
        } finally {
            roleDAO.delete(expectedRole);
        }
    }

    @Test(expected = Exception.class)
    public void shouldNotAllowDuplicateNames() throws Exception {
        // given
        Role expectedRoleOne = new Role()
                .withName("role name")
                .withDescription("role description one");
        Role expectedRoleTwo = new Role()
                .withName(expectedRoleOne.getName())
                .withDescription("role description two");
        try {
            // when
            roleDAO.save(expectedRoleOne);
            roleDAO.save(expectedRoleTwo);
        } finally {
            roleDAO.delete(expectedRoleOne);
        }
    }

    @Test
    public void shouldSaveRequiredFieldsAndRetrieveById() throws Exception {
        // given
        Role expectedRole = new Role()
                .withName("role name")
                .withDescription("role description");

        // when
        roleDAO.save(expectedRole);

        // then
        Role actualRole = roleDAO.findById(expectedRole.getId());
        try {
            assertEquals(expectedRole, actualRole);
        } finally {
            roleDAO.delete(expectedRole);
        }
    }

    @Test
    public void shouldSaveAllFieldsWithObjectHierarchyAndRetrieveById() throws Exception {
        // given
        Role expectedRole = new Role()
                .withName("role name")
                .withDescription("role description")
                .withClub(club);

        // when
        roleDAO.save(expectedRole);

        // then
        Role actualRole = roleDAO.findById(expectedRole.getId());
        try {
            assertEquals(expectedRole, actualRole);
        } finally {
            roleDAO.delete(expectedRole);
        }
    }

    @Test
    public void shouldUpdateWhenContainsChildren() throws Exception {
        // given
        Role expectedRole = new Role()
                .withName("role name")
                .withDescription("role description")
                .withClub(club);

        // when
        roleDAO.save(expectedRole);
        Role updatedRole =
                expectedRole.merge(new Role()
                        .withName("new role name")
                        .withDescription("new role description"));
        roleDAO.update(updatedRole);

        // then
        Role actualRole = roleDAO.findById(expectedRole.getId());
        try {
            assertEquals(updatedRole.incrementVersion(), actualRole);
            assertEquals("new role name", actualRole.getName());
        } finally {
            roleDAO.delete(expectedRole);
        }
    }

    @Test
    public void shouldSaveAndRetrieveList() throws Exception {
        // given
        Role roleOne = new Role()
                .withName("role one name")
                .withDescription("role one description");
        Role roleTwo = new Role()
                .withName("role two name")
                .withDescription("role two description");

        // when
        roleDAO.save(roleOne);
        roleDAO.save(roleTwo);

        // then
        Object[] actualRoles = roleDAO.findAll().toArray();
        try {
            assertArrayEquals(new Role[]{roleOne, roleTwo}, actualRoles);
        } finally {
            roleDAO.delete(roleOne);
            roleDAO.delete(roleTwo);
        }
    }

    @Test
    public void shouldSaveAndRetrieveAndDelete() throws Exception {
        // given
        Role expectedRole = new Role()
                .withName("role name")
                .withDescription("role description");
        roleDAO.save(expectedRole);
        assertEquals(expectedRole, roleDAO.findById(expectedRole.getId()));

        // when
        roleDAO.delete(expectedRole);

        // then
        assertNull(roleDAO.findById(expectedRole.getId()));
    }

    @Test
    public void shouldSaveAndRetrieveAndDeleteById() throws Exception {
        // given
        Role expectedRole = new Role()
                .withName("role name")
                .withDescription("role description");
        roleDAO.save(expectedRole);
        assertEquals(expectedRole, roleDAO.findById(expectedRole.getId()));

        // when
        roleDAO.delete(expectedRole.getId());

        // then
        assertNull(roleDAO.findById(expectedRole.getId()));
    }

    @Test
    public void shouldThrowExceptionWhenFindingNullId() {
        assertNull(roleDAO.findById(null));
    }

    @Test
    public void shouldThrowExceptionWhenFindingNullEmail() {
        assertNull(roleDAO.findByName(null));
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenSavingNull() {
        roleDAO.save(null);
    }

    @Test
    public void shouldNotThrowExceptionWhenUpdatingNull() {
        roleDAO.update(null);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenDeletingNull() {
        roleDAO.delete((Role) null);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenDeletingInvalidId() {
        roleDAO.delete(1l);
    }

}
