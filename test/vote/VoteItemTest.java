package vote;

import static org.junit.jupiter.api.Assertions.*;

import auxiliary.Dish;
import org.junit.jupiter.api.Test;

class VoteItemTest {

    // test strategy

    @Test
    void test() {

        VoteItem<Dish> voteItem = new VoteItem<>(new Dish("A", 10), "喜欢");

        assertEquals("喜欢", voteItem.getVoteValue());
        assertNotEquals("讨厌", voteItem.getVoteValue());

        assertEquals(new VoteItem<>(new Dish("A", 10), "喜欢"), voteItem);
        assertNotEquals(new VoteItem<>(new Dish("A", 11), "喜欢"), voteItem);
        assertNotEquals(new VoteItem<>(new Dish("A", 10), "不喜欢"), voteItem);

        assertEquals(new VoteItem<>(new Dish("A", 10), "喜欢").hashCode(), voteItem.hashCode());
        assertNotEquals(new VoteItem<>(new Dish("A", 11), "喜欢").hashCode(), voteItem.hashCode());
        assertNotEquals(new VoteItem<>(new Dish("A", 10), "不喜欢").hashCode(), voteItem.hashCode());

    }

}
