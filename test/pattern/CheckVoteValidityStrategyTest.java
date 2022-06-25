package pattern;

import static org.junit.jupiter.api.Assertions.*;

import auxiliary.Dish;
import auxiliary.Person;
import auxiliary.Voter;
import exception.VoteInvalidException;
import org.junit.jupiter.api.Test;

import vote.RealNameVote;
import vote.Vote;
import vote.VoteItem;
import vote.VoteType;

import java.util.*;

/*
 * Testing strategy
 * 等价类划分：
 * 添加投票阶段有效性：
 * 1. newVote 没有包含 targetCandidates 中所有候选对象
 * 2. newVote 包含了不在 targetCandidates 中的候选对象
 * 3. newVote 出现了 voteType 中没有的选项
 * 4. newVote 中出现了对 同一个对象的多次投票
 * 5. newVote 的支持数超出限制（仅针对 ElectionCheckVoteValidityStrategy ）
 *
 * 计票前阶段有效性：
 * Default:
 * 1. 还有人没有投票
 * 2. 都投票了，但有人没有实名
 * 2. 都投票了，都实名了，但有人重复投票
 * 3. 都投票了，都实名了，且没人重复投票
 */
class CheckVoteValidityStrategyTest {

    /*
     * 覆盖情况 1, 2, 3, 4
     */
    @Test
    public void testDefaultCheckVoteValidityStrategy() {
        CheckVoteValidityStrategy cvvs = new DefaultCheckVoteValidityStrategy();
        Map<String, Integer> options = new HashMap<>();
        options.put("喜欢", 2);
        options.put("不喜欢", 0);
        options.put("无所谓", 1);
        VoteType voteType = new VoteType(options);

        List<Dish> dishes = new ArrayList<>();
        dishes.add(new Dish("A", 10));
        dishes.add(new Dish("B", 40));
        dishes.add(new Dish("C", 20));

        Set<VoteItem<Dish>> voteItems = new HashSet<>();
        voteItems.add(new VoteItem<>(dishes.get(0), "喜欢"));
        voteItems.add(new VoteItem<>(dishes.get(1), "无所谓"));
        voteItems.add(new VoteItem<>(dishes.get(2), "不喜欢"));
        Vote<Dish> vote = new Vote<>(voteItems);

        assertTrue(cvvs.checkAddVoteValidity(dishes, vote, voteType));

        voteItems = new HashSet<>();
        voteItems.add(new VoteItem<>(dishes.get(0), "喜欢"));
        voteItems.add(new VoteItem<>(dishes.get(2), "不喜欢"));
        vote = new Vote<>(voteItems);

        assertFalse(cvvs.checkAddVoteValidity(dishes, vote, voteType));


        voteItems.add(new VoteItem<>(dishes.get(1), "无所谓"));
        voteItems.add(new VoteItem<>(new Dish("D", 80), "不喜欢"));
        vote = new Vote<>(voteItems);

        assertFalse(cvvs.checkAddVoteValidity(dishes, vote, voteType));

        voteItems = new HashSet<>();
        voteItems.add(new VoteItem<>(dishes.get(0), "喜欢"));
        voteItems.add(new VoteItem<>(dishes.get(1), "非常喜欢"));
        vote = new Vote<>(voteItems);

        assertFalse(cvvs.checkAddVoteValidity(dishes, vote, voteType));

        voteItems = new HashSet<>();
        voteItems.add(new VoteItem<>(dishes.get(1), "喜欢"));
        voteItems.add(new VoteItem<>(dishes.get(1), "不喜欢"));
        vote = new Vote<>(voteItems);

        assertFalse(cvvs.checkAddVoteValidity(dishes, vote, voteType));

    }

    /*
     * 覆盖情况 5
     */
    @Test
    public void checkElectionCheckVoteValidityStrategy() {
        CheckVoteValidityStrategy cvvs = new ElectionCheckVoteValidityStrategy(2);
        Map<String, Integer> options = new HashMap<>();
        options.put("支持", 1);
        options.put("反对", -1);
        options.put("放弃", 0);
        VoteType voteType = new VoteType(options);

        List<Person> people = new ArrayList<>();
        people.add(new Person("Alice", 19));
        people.add(new Person("Bob", 20));
        people.add(new Person("Cathy", 19));

        Set<VoteItem<Person>> voteItems = new HashSet<>();
        voteItems.add(new VoteItem<>(people.get(0), "支持"));
        voteItems.add(new VoteItem<>(people.get(1), "支持"));
        voteItems.add(new VoteItem<>(people.get(2), "反对"));
        Vote<Person> vote = new Vote<>(voteItems);

        assertTrue(cvvs.checkAddVoteValidity(people, vote, voteType));

        voteItems = new HashSet<>();
        voteItems.add(new VoteItem<>(people.get(0), "支持"));
        voteItems.add(new VoteItem<>(people.get(1), "支持"));
        voteItems.add(new VoteItem<>(people.get(2), "支持"));
        vote = new Vote<>(voteItems);

        assertFalse(cvvs.checkAddVoteValidity(people, vote, voteType));
    }

    /*
     * 覆盖：
     * 计票前阶段有效性（Default）
     */
    @Test
    public void testDefaultCheckVoteValidityStrategyBeforeStatistics() throws VoteInvalidException {
        CheckVoteValidityStrategy cvvs = new DefaultCheckVoteValidityStrategy();
        Map<Voter, Double> voters = Map.of(
                new Voter("干饭人1"), 1.0,
                new Voter("干饭人2"), 1.0
        );

        List<Dish> dishes = List.of(
                new Dish("A", 20),
                new Dish("B", 30));
        Map<Vote<Dish>, Boolean> votes = Map.of(
                new RealNameVote<>(Set.of(
                        new VoteItem<>(dishes.get(0), "喜欢"),
                        new VoteItem<>(dishes.get(1), "喜欢")),
                        new Voter("干饭人1")), true
        );

        Map<Vote<Dish>, Boolean> finalVotes1 = votes;
        assertThrows(VoteInvalidException.class, () -> cvvs.checkVoteValidity(voters, finalVotes1));

        Vote<Dish> vote1 = new RealNameVote<>(Set.of(
                new VoteItem<>(dishes.get(0), "喜欢"),
                new VoteItem<>(dishes.get(1), "喜欢")),
                new Voter("干饭人1"));
        Vote<Dish> vote2 = new Vote<>(Set.of(
                new VoteItem<>(dishes.get(0), "喜欢"),
                new VoteItem<>(dishes.get(1), "喜欢")));
        Vote<Dish> vote3;


        votes = Map.of(
                vote1, true,
                vote2, true
        );

        Map<Vote<Dish>, Boolean> finalVotes = votes;
        assertThrows(VoteInvalidException.class, () -> cvvs.checkVoteValidity(voters, finalVotes));


        vote1 = new RealNameVote<>(Set.of(
                new VoteItem<>(dishes.get(0), "喜欢"),
                new VoteItem<>(dishes.get(1), "喜欢")),
                new Voter("干饭人1"));
        vote2 = new RealNameVote<>(Set.of(
                new VoteItem<>(dishes.get(0), "喜欢"),
                new VoteItem<>(dishes.get(1), "不喜欢")),
                new Voter("干饭人1"));
        vote3 = new RealNameVote<>(Set.of(
                new VoteItem<>(dishes.get(0), "喜欢"),
                new VoteItem<>(dishes.get(1), "喜欢")),
                new Voter("干饭人2"));

        votes = Map.of(
                vote1, true,
                vote2, true,
                vote3, true
        );

        assertEquals(Map.of(
                vote1, false,
                vote2, false,
                vote3, true
        ), cvvs.checkVoteValidity(voters, votes));


        vote1 = new RealNameVote<>(Set.of(
                new VoteItem<>(dishes.get(0), "喜欢"),
                new VoteItem<>(dishes.get(1), "喜欢")),
                new Voter("干饭人1"));
        vote2 = new RealNameVote<>(Set.of(
                new VoteItem<>(dishes.get(0), "喜欢"),
                new VoteItem<>(dishes.get(1), "喜欢")),
                new Voter("干饭人2"));

        votes = Map.of(
                vote1, true,
                vote2, true
        );

        assertEquals(Map.of(
                vote1, true,
                vote2, true
        ), cvvs.checkVoteValidity(voters, votes));

    }

}
