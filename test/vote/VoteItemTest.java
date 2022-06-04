package vote;

import static org.junit.jupiter.api.Assertions.*;

import auxiliary.Dish;
import org.junit.jupiter.api.Test;

class VoteItemTest {

    /* Testing strategy
     * 给出一个对于 Dish 的具体表决：(("A", 10), "喜欢")
     * 测试 voteItem 中的所有方法
     */

    @Test
    void test() {

        VoteItem<Dish> voteItem = new VoteItem<>(new Dish("A", 10), "喜欢");

        assertEquals(new Dish("A", 10), voteItem.getCandidate());
        assertNotEquals(new Dish("A", 11), voteItem.getCandidate());

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
