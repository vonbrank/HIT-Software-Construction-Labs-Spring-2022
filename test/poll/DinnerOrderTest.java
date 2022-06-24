package poll;

import static org.junit.jupiter.api.Assertions.*;

import auxiliary.Dish;
import auxiliary.Voter;
import org.junit.jupiter.api.Test;
import pattern.DefaultCheckVoteValidityStrategy;
import pattern.DinnerOrderSelectionStrategy;
import pattern.DinnerOrderStatisticStrategy;
import vote.RealNameVote;
import vote.Vote;
import vote.VoteItem;
import vote.VoteType;

import java.util.*;

class DinnerOrderTest {

    /*
     * Testing strategy
     *
     * 给出一组点菜表决的数据，及其表决结果的字符串，测试 DinnerOrder 的正确性
     * DinnerOrder 的实例直接调用构造函数产生
     *
     * 覆盖 DinnerOrder 中完成一次表决的所有方法
     */

    @Test
    void test() {
        Poll<Dish> poll = new DinnerOrder();

        Map<String, Integer> options = new HashMap<>();
        options.put("喜欢", 2);
        options.put("不喜欢", 0);
        options.put("无所谓", 1);
        VoteType voteType = new VoteType(options);

        List<Dish> dishes = new ArrayList<>();
        dishes.add(new Dish("A", 10));
        dishes.add(new Dish("B", 40));
        dishes.add(new Dish("C", 20));
        dishes.add(new Dish("D", 80));
        dishes.add(new Dish("E", 50));
        dishes.add(new Dish("F", 70));

        Map<Voter, Double> voters = new HashMap<>();
        voters.put(new Voter("爷爷"), 4.0);
        voters.put(new Voter("爸爸"), 1.0);
        voters.put(new Voter("妈妈"), 2.0);
        voters.put(new Voter("儿子"), 2.0);

        Set<VoteItem<Dish>> voteItemsGrandpa = new HashSet<>();
        voteItemsGrandpa.add(new VoteItem<>(dishes.get(0), "喜欢"));
        voteItemsGrandpa.add(new VoteItem<>(dishes.get(1), "喜欢"));
        voteItemsGrandpa.add(new VoteItem<>(dishes.get(2), "无所谓"));
        voteItemsGrandpa.add(new VoteItem<>(dishes.get(3), "无所谓"));
        voteItemsGrandpa.add(new VoteItem<>(dishes.get(4), "不喜欢"));
        voteItemsGrandpa.add(new VoteItem<>(dishes.get(5), "不喜欢"));
        Vote<Dish> voteGrandpa = new RealNameVote<>(voteItemsGrandpa, new Voter("爷爷"));

        Set<VoteItem<Dish>> voteItemsDad = new HashSet<>();
        voteItemsDad.add(new VoteItem<>(dishes.get(0), "无所谓"));
        voteItemsDad.add(new VoteItem<>(dishes.get(1), "喜欢"));
        voteItemsDad.add(new VoteItem<>(dishes.get(2), "喜欢"));
        voteItemsDad.add(new VoteItem<>(dishes.get(3), "喜欢"));
        voteItemsDad.add(new VoteItem<>(dishes.get(4), "不喜欢"));
        voteItemsDad.add(new VoteItem<>(dishes.get(5), "喜欢"));
        Vote<Dish> voteDad = new RealNameVote<>(voteItemsDad, new Voter("爸爸"));

        Set<VoteItem<Dish>> voteItemsMum = new HashSet<>();
        voteItemsMum.add(new VoteItem<>(dishes.get(0), "喜欢"));
        voteItemsMum.add(new VoteItem<>(dishes.get(1), "不喜欢"));
        voteItemsMum.add(new VoteItem<>(dishes.get(2), "不喜欢"));
        voteItemsMum.add(new VoteItem<>(dishes.get(3), "不喜欢"));
        voteItemsMum.add(new VoteItem<>(dishes.get(4), "喜欢"));
        voteItemsMum.add(new VoteItem<>(dishes.get(5), "不喜欢"));
        Vote<Dish> voteMum = new RealNameVote<>(voteItemsMum, new Voter("妈妈"));

        Set<VoteItem<Dish>> voteItemsSon = new HashSet<>();
        voteItemsSon.add(new VoteItem<>(dishes.get(0), "喜欢"));
        voteItemsSon.add(new VoteItem<>(dishes.get(1), "无所谓"));
        voteItemsSon.add(new VoteItem<>(dishes.get(2), "喜欢"));
        voteItemsSon.add(new VoteItem<>(dishes.get(3), "喜欢"));
        voteItemsSon.add(new VoteItem<>(dishes.get(4), "喜欢"));
        voteItemsSon.add(new VoteItem<>(dishes.get(5), "不喜欢"));
        Vote<Dish> voteSon = new RealNameVote<>(voteItemsSon, new Voter("儿子"));

        List<String> selectedDishes = List.of("A", "B", "C", "D");

        poll.setInfo("Dishes Vote", Calendar.getInstance(), voteType, 4);

        poll.addCandidates(dishes);

        poll.addVoters(voters);

        poll.setCheckVoteValidityStrategy(new DefaultCheckVoteValidityStrategy());

        poll.addVote(voteGrandpa);
        poll.addVote(voteDad);
        poll.addVote(voteMum);
        poll.addVote(voteSon);

        poll.statistics(new DinnerOrderStatisticStrategy());
        poll.selection(new DinnerOrderSelectionStrategy(4));

//        assertEquals(String.join("\n", selectedDishes), poll.result());

        assertTrue("A, 1\nB, 2\nC, 3\nD, 4".equals(poll.result()) ||
                "A, 1\nB, 2\nD, 3\nC, 4".equals(poll.result()));
        assertEquals("name: Dishes Vote, candidates: (A, B, C, D, E, F)", poll.toString());

    }

}
