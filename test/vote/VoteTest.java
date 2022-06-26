package vote;

import static org.junit.jupiter.api.Assertions.*;

import auxiliary.Dish;
import exception.StringFormatException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class VoteTest {

    /*
     * 提供一组对于 Dish 的表决：[("A", "喜欢"), ("B", "不喜欢")]
     * 覆盖 vote 中所有方法
     */

    @Test
    void test() throws NoSuchFieldException, IllegalAccessException {

        Set<VoteItem<Dish>> dishSet = new HashSet<>(Set.of(
                new VoteItem<>(new Dish("A", 10), "喜欢"),
                new VoteItem<>(new Dish("B", 15), "不喜欢")
        ));

        Vote<Dish> vote = new Vote<Dish>(dishSet);

        assertEquals(dishSet, vote.getVoteItems());

        assertTrue(vote.candidateIncluded(new Dish("A", 10)));

        Vote<Dish> otherVote = new Vote<Dish>(dishSet);

        Field f = vote.getClass().getDeclaredField("id");
        Object voteID = f.get(vote);
        f.set(otherVote, voteID);

        assertEquals(vote, otherVote);
        assertEquals(otherVote.hashCode(), vote.hashCode());

        dishSet.remove(new VoteItem<Dish>(new Dish("A", 10), "喜欢"));
        dishSet.add(new VoteItem<Dish>(new Dish("A", 11), "喜欢"));
        otherVote = new Vote<Dish>(dishSet);

        assertNotEquals(otherVote, vote);
        assertNotEquals(otherVote.hashCode(), vote.hashCode());

    }

}
