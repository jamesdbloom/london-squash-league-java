package org.squashleague.web.tasks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.Match;
import org.squashleague.domain.league.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author jamesdbloom
 */
public class CommandHolderTest {

    @Mock
    private Player mockObject;
    @Mock
    private Future<Player> mockFutureObject;
    @Mock
    private ArrayList<Match> mockList;
    @Mock
    private Future<List<Match>> mockFutureList;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldDelegateToFutureResult() throws Exception {
        when(mockFutureObject.get()).thenReturn(mockObject);
        when(mockObject.getUser()).thenReturn(new User());

        Player proxy = CommandHolder.newInstance(mockFutureObject, Player.class);

        User user = proxy.getUser();
        verify(mockFutureObject).get();
        verify(mockObject).getUser();
        assertEquals(new User(), user);
    }

    @Test
    public void shouldDelegateToFutureResultList() throws Exception {
        when(mockFutureList.get()).thenReturn(mockList);
        when(mockList.size()).thenReturn(12);

        List<Match> proxy = CommandHolder.newListInstance(mockFutureList, Match.class);

        int size = proxy.size();
        verify(mockFutureList).get();
        verify(mockList).size();
        assertEquals(12, size);
    }
}
