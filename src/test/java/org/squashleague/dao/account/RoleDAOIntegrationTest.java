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
public class RoleDAOIntegrationTest {

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
    public void shouldSaveRequiredFieldsAndRetrieveById() throws Exception {
        // given
        Role expectedRole = new Role()
                .withName("role name")
                .withDescription("role description");

        // when
        roleDAO.save(expectedRole);

        // then
        assertEquals(expectedRole, roleDAO.findOne(expectedRole.getId()));
        roleDAO.delete(expectedRole);
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
        assertEquals(expectedRole, roleDAO.findOne(expectedRole.getId()));
        roleDAO.delete(expectedRole);
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
        assertArrayEquals(new Role[]{roleOne, roleTwo}, roleDAO.findAll().toArray());
        roleDAO.delete(roleOne);
        roleDAO.delete(roleTwo);
    }

    @Test
    public void shouldSaveAndRetrieveAndDelete() throws Exception {
        // given
        Role expectedRole = new Role()
                .withName("role name")
                .withDescription("role description");
        roleDAO.save(expectedRole);
        assertEquals(expectedRole, roleDAO.findOne(expectedRole.getId()));

        // when
        roleDAO.delete(expectedRole);

        // then
        assertNull(roleDAO.findOne(expectedRole.getId()));
    }

}
