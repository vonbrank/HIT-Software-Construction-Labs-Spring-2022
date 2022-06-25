package vote;

import static org.junit.jupiter.api.Assertions.*;

import auxiliary.Dish;
import exception.StringFormatException;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class VoteTest {

    /*
     * 提供一组对于 Dish 的表决：[("A", "喜欢"), ("B", "不喜欢")]
     * 覆盖 vote 中所有方法
     */

    @Test
    void test() {

        Set<VoteItem<Dish>> dishSet = new HashSet<>(Set.of(
                new VoteItem<>(new Dish("A", 10), "喜欢"),
                new VoteItem<>(new Dish("B", 15), "不喜欢")
        ));

        Vote<Dish> vote = new Vote<Dish>(dishSet);

        assertEquals(dishSet, vote.getVoteItems());

        assertTrue(vote.candidateIncluded(new Dish("A", 10)));

        Vote<Dish> otherVote = new Vote<Dish>(dishSet);

        assertEquals(vote, otherVote);
        assertEquals(otherVote.hashCode(), vote.hashCode());

        dishSet.remove(new VoteItem<Dish>(new Dish("A", 10), "喜欢"));
        dishSet.add(new VoteItem<Dish>(new Dish("A", 11), "喜欢"));
        otherVote = new Vote<Dish>(dishSet);

        assertNotEquals(otherVote, vote);
        assertNotEquals(otherVote.hashCode(), vote.hashCode());

    }

    /*
     *  Testing strategy
     *
     *  1. 英语，5 个字符以内   "Yes"(1)|"No"(-1)|"SoSo"(0)
     *  2. 英语，超过 5 个字符  "Support"(1)|"Oppose"(-1)|"Waive"(0)
     *  3. 汉语，5 个字符以内   "支持"(1)|"不支持"(-1)|"无所谓"(0)
     *  4. 汉语，超过 5 个字符  "超级无敌支持"(1)|"不支持"(-1)|"无所谓"(0)
     *  5. 英语，无分数        "Yes"|"No"|"SoSo"
     *  6. 汉语，无分数        "支持"|"不支持"|"无所谓"
     */

    @Test
    public void testRegexConstructor() {
        try {
            VoteType voteType1 = new VoteType("\"Yes\"(1)|\"No\"(-1)|\"SoSo\"(0)");
            System.out.println(voteType1);

            assertThrows(StringFormatException.class, () -> {
                VoteType voteType2 = new VoteType("\"Support\"(1)|\"Oppose\"(-1)|\"Waive\"(0)");
            });

            VoteType voteType3 = new VoteType("\"支持\"(1)|\"不支持\"(-1)|\"无所谓\"(0)");
            System.out.println(voteType3);

            assertThrows(StringFormatException.class, () -> {
                VoteType voteType4 = new VoteType("\"超级无敌支持\"(1)|\"不支持\"(-1)|\"无所谓\"(0)");
            });

            VoteType voteType5 = new VoteType("\"Yes\"|\"No\"|\"SoSo\"");
            System.out.println(voteType5);

            VoteType voteType6 = new VoteType("\"支持\"|\"不支持\"|\"无所谓\"");
            System.out.println(voteType6);

        } catch (StringFormatException e) {
			e.printStackTrace();
        }

    }

}
