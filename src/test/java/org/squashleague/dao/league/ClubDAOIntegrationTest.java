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
        assertEquals(expectedClub, clubDAO.findById(expectedClub.getId()));
        clubDAO.delete(expectedClub);
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
        assertEquals(expectedClub.incrementVersion(), clubDAO.findById(expectedClub.getId()));
        clubDAO.delete(expectedClub);
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
        assertEquals(expectedClub, clubDAO.findById(expectedClub.getId()));
        for(League league : expectedClub.getLeagues()) {
            leagueDAO.delete(league);
        }
        clubDAO.delete(expectedClub);
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
        assertArrayEquals(new Club[]{clubOne, clubTwo}, clubDAO.findAll().toArray());
        clubDAO.delete(clubOne);
        clubDAO.delete(clubTwo);
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
    public void shouldNotThrowExceptionWhenFindingNullId() {
        assertNull(clubDAO.findById(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotThrowExceptionWhenSavingNull() {
        clubDAO.save(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotThrowExceptionWhenUpdatingNull() {
        clubDAO.update(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotThrowExceptionWhenDeletingNull() {
        clubDAO.delete((Club) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotThrowExceptionWhenDeletingInvalidId() {
        clubDAO.delete(1l);
    }

}
