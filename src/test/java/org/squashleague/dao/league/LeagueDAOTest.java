package org.squashleague.dao.league;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.League;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author jamesdbloom
 */
@RunWith(MockitoJUnitRunner.class)
public class LeagueDAOTest {

    @Mock
    protected EntityManager entityManager;
    @InjectMocks
    private LeagueDAO leagueDAO = new LeagueDAO();

    @Test
    public void shouldReturnEmptyListIfNoActiveLeagues() throws Exception {
        // given
        User user = (User) new User().withId(1l);
        TypedQuery<League> query = mock(TypedQuery.class);
        when(entityManager.createQuery(any(String.class), eq(League.class))).thenReturn(query);
        when(query.getResultList()).thenThrow(new EmptyResultDataAccessException(0));

        // then
        assertEquals(new ArrayList<League>(), leagueDAO.findAllUnregisteredLeagues(user));
    }
}
