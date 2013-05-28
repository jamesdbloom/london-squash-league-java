package org.squashleague.dao.league;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.squashleague.configuration.RootConfiguration;
import org.squashleague.domain.league.Club;
import org.squashleague.domain.league.League;
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
public class ClubDAOIntegrationTest extends AdministratorLoggedInTest {

    @Resource
    private ClubDAO clubDAO;
    @Resource
    private LeagueDAO leagueDAO;

    @Test
    public void shouldSaveRequiredFieldsAndRetrieveById() throws Exception {
        // given
        Club expectedClub = new Club()
                .withName("expectedClub name")
                .withAddress("expectedClub address");

        // when
        clubDAO.save(expectedClub);

        // then
        Club actualClub = clubDAO.findById(expectedClub.getId());
        try {
            assertEquals(expectedClub, actualClub);
        } finally {
            clubDAO.delete(expectedClub);
        }
    }

    @Test
    public void shouldSaveUpdateAndRetrieveById() throws Exception {
        // given
        Club expectedClub = new Club()
                .withName("expectedClub name")
                .withAddress("expectedClub address");
        clubDAO.save(expectedClub);
        expectedClub
                .withName("new expectedClub name")
                .withAddress("new expectedClub address");

        // when
        clubDAO.update(expectedClub);

        // then
        Club actualClub = clubDAO.findById(expectedClub.getId());
        try {
            assertEquals(expectedClub.incrementVersion(), actualClub);
        } finally {
            clubDAO.delete(expectedClub);
        }
    }

    @Test
    public void shouldSaveAllFieldsWithObjectHierarchyAndRetrieveById() throws Exception {
        // given
        Club expectedClub = new Club()
                .withName("expectedClub name")
                .withAddress("expectedClub address")
                .withLeagues(
                        new League().withName("league one"),
                        new League().withName("league two")
                );

        // when
        clubDAO.save(expectedClub);

        // then
        Club actualClub = clubDAO.findById(expectedClub.getId());
        try {
            assertEquals(expectedClub, actualClub);
        } finally {
            for (League league : expectedClub.getLeagues()) {
                leagueDAO.delete(league);
            }
            clubDAO.delete(expectedClub);
        }
    }

    @Test
    public void shouldUpdateWhenContainsChildren() throws Exception {
        // given
        Club expectedClub = new Club()
                .withName("club name")
                .withAddress("club description");

        // when
        clubDAO.save(expectedClub);
        Club updatedClub =
                expectedClub.merge(new Club()
                        .withName("new club name")
                        .withAddress("new club description"));
        clubDAO.update(updatedClub);

        // then
        Club actualClub = clubDAO.findById(expectedClub.getId());
        try {
            assertEquals(updatedClub.incrementVersion(), actualClub);
            assertEquals("new club name", actualClub.getName());
        } finally {
            clubDAO.delete(expectedClub);
        }
    }

    @Test
    public void shouldSaveAndRetrieveList() throws Exception {
        // given
        Club clubOne = new Club()
                .withName("clubOne name")
                .withAddress("clubOne address");
        Club clubTwo = new Club()
                .withName("clubTwo name")
                .withAddress("clubTwo address");

        // when
        clubDAO.save(clubOne);
        clubDAO.save(clubTwo);

        // then
        Object[] actualClubs = clubDAO.findAll().toArray();
        try {
            assertArrayEquals(new Club[]{clubOne, clubTwo}, actualClubs);
        } finally {
            clubDAO.delete(clubOne);
            clubDAO.delete(clubTwo);
        }
    }

    @Test
    public void shouldSaveAndRetrieveAndDelete() throws Exception {
        // given
        Club expectedClub = new Club()
                .withName("expectedClub name")
                .withAddress("expectedClub address");
        clubDAO.save(expectedClub);
        assertEquals(expectedClub, clubDAO.findById(expectedClub.getId()));

        // when
        clubDAO.delete(expectedClub);

        // then
        assertNull(clubDAO.findById(expectedClub.getId()));
    }

    @Test
    public void shouldSaveAndRetrieveAndDeleteById() throws Exception {
        // given
        Club expectedClub = new Club()
                .withName("expectedClub name")
                .withAddress("expectedClub address");
        clubDAO.save(expectedClub);
        assertEquals(expectedClub, clubDAO.findById(expectedClub.getId()));

        // when
        clubDAO.delete(expectedClub.getId());

        // then
        assertNull(clubDAO.findById(expectedClub.getId()));
    }

    @Test
    public void shouldThrowExceptionWhenFindingNullId() {
        assertNull(clubDAO.findById(null));
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenSavingNull() {
        clubDAO.save(null);
    }

    @Test
    public void shouldNotThrowExceptionWhenUpdatingNull() {
        clubDAO.update(null);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenDeletingNull() {
        clubDAO.delete((Club) null);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenDeletingInvalidId() {
        clubDAO.delete(1l);
    }

}
