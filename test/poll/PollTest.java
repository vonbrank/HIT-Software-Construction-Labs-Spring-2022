package poll;

import auxiliary.Dish;
import auxiliary.Voter;
import org.junit.jupiter.api.Test;
import vote.Vote;
import vote.VoteItem;
import vote.VoteType;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class PollTest {

    /*
     * Testing strategy
     *
     * 给出一组点菜表决的数据，及其表决结果的字符串，测试 Poll 的正确性
     * Poll 的实例由 Poll.create() 返回
     *
     * 覆盖 Poll 中完成一次表决的所有方法
     */

    @Test
    void test() {
        Poll<Dish> poll = Poll.create();

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
        Vote<Dish> voteGrandpa = new Vote<>(voteItemsGrandpa);

        Set<VoteItem<Dish>> voteItemsDad = new HashSet<>();
        voteItemsDad.add(new VoteItem<>(dishes.get(0), "无所谓"));
        voteItemsDad.add(new VoteItem<>(dishes.get(1), "喜欢"));
        voteItemsDad.add(new VoteItem<>(dishes.get(2), "喜欢"));
        voteItemsDad.add(new VoteItem<>(dishes.get(3), "喜欢"));
        voteItemsDad.add(new VoteItem<>(dishes.get(4), "不喜欢"));
        voteItemsDad.add(new VoteItem<>(dishes.get(5), "喜欢"));
        Vote<Dish> voteDad = new Vote<>(voteItemsDad);

        Set<VoteItem<Dish>> voteItemsMum = new HashSet<>();
        voteItemsMum.add(new VoteItem<>(dishes.get(0), "喜欢"));
        voteItemsMum.add(new VoteItem<>(dishes.get(1), "不喜欢"));
        voteItemsMum.add(new VoteItem<>(dishes.get(2), "不喜欢"));
        voteItemsMum.add(new VoteItem<>(dishes.get(3), "不喜欢"));
        voteItemsMum.add(new VoteItem<>(dishes.get(4), "喜欢"));
        voteItemsMum.add(new VoteItem<>(dishes.get(5), "不喜欢"));
        Vote<Dish> voteMum = new Vote<>(voteItemsMum);

        Set<VoteItem<Dish>> voteItemsSon = new HashSet<>();
        voteItemsSon.add(new VoteItem<>(dishes.get(0), "喜欢"));
        voteItemsSon.add(new VoteItem<>(dishes.get(1), "无所谓"));
        voteItemsSon.add(new VoteItem<>(dishes.get(2), "喜欢"));
        voteItemsSon.add(new VoteItem<>(dishes.get(3), "喜欢"));
        voteItemsSon.add(new VoteItem<>(dishes.get(4), "喜欢"));
        voteItemsSon.add(new VoteItem<>(dishes.get(5), "不喜欢"));
        Vote<Dish> voteSon = new Vote<>(voteItemsSon);

        List<String> selectedDishes = List.of("A", "B", "C", "D");

        poll.setInfo("Dishes Vote", Calendar.getInstance(), voteType, 4);

        poll.addCandidates(dishes);

        poll.addVoters(voters);

        poll.addVote(voteGrandpa);
        poll.addVote(voteDad);
        poll.addVote(voteMum);
        poll.addVote(voteSon);

        assertEquals(String.join("\n", selectedDishes), poll.result());
        assertEquals("name: Dishes Vote, candidates: (A, B, C, D, E, F)", poll.toString());

    }

}
