package pattern;

import static org.junit.jupiter.api.Assertions.*;

import auxiliary.*;
import org.junit.jupiter.api.Test;
import vote.RealNameVote;
import vote.Vote;
import vote.VoteItem;
import vote.VoteType;

import java.util.*;

/*
 * Testing strategy
 * 提案计票的等价类划分：
 * 1. 提案数不是 1
 * 2. 提案数为 1 ，vote 类型为 Vote
 * 3. 提案数为 1 ，vote 类型为 RealNameVote，其 voter 类型为 Voter
 *
 * 点菜计票的等价类划分：
 * 1. vote 类型为 Vote
 * 2. vote 类型为 RealNameVote，其 voter 类型为 Voter
 * 3. vote 类型为 RealNameVote，其 voter 类型为 WeightVoter
 *
 * 选举计票的等价类划分：
 * 任意一种合法的输入
 *
 */

public class StatisticsStrategyTest {
    /*
     * 覆盖：
     * 提案计票情况 1
     */
    @Test
    public void testBusinessVotingStatisticStrategy() {
        StatisticsStrategy ss = new BusinessVotingStatisticStrategy();

        VoteType voteType = new VoteType(Map.of(
                "支持", 0, "反对", 0
        ));

        List<Proposal> proposals = new ArrayList<>();
        proposals.add(new Proposal("提案1", Calendar.getInstance()));
        proposals.add(new Proposal("提案2", Calendar.getInstance()));

        Map<Voter, Double> voters = Map.of(
                new Voter("A"), 5.0,
                new Voter("B"), 51.0,
                new Voter("C"), 10.0,
                new Voter("D"), 24.0,
                new Voter("E"), 20.0
        );

        Set<Vote<Proposal>> votes = new HashSet<>();
        votes.add(new RealNameVote<>(Set.of(
                new VoteItem<>(proposals.get(0), "反对"),
                new VoteItem<>(proposals.get(1), "反对")),
                new Voter("A")));
        votes.add(new RealNameVote<>(Set.of(
                new VoteItem<>(proposals.get(0), "支持"),
                new VoteItem<>(proposals.get(1), "支持")),
                new Voter("B")));
        votes.add(new RealNameVote<>(Set.of(
                new VoteItem<>(proposals.get(0), "支持"),
                new VoteItem<>(proposals.get(1), "支持")),
                new Voter("C")));
        votes.add(new RealNameVote<>(Set.of(
                new VoteItem<>(proposals.get(0), "反对"),
                new VoteItem<>(proposals.get(1), "反对")),
                new Voter("D")));
        votes.add(new RealNameVote<>(Set.of(
                new VoteItem<>(proposals.get(0), "反对"),
                new VoteItem<>(proposals.get(1), "反对")),
                new Voter("E")));
        List<Proposal> finalProposals = proposals;
        Set<Vote<Proposal>> finalVotes = votes;
        assertThrows(RuntimeException.class, () -> ss.getVoteStatistics(voteType, voters, finalProposals, finalVotes));


        proposals = new ArrayList<>();
        proposals.add(new Proposal("提案1", Calendar.getInstance()));

        votes = new HashSet<>();
        votes.add(new Vote<>(Set.of(
                new VoteItem<>(proposals.get(0), "反对"))));
        votes.add(new Vote<>(Set.of(
                new VoteItem<>(proposals.get(0), "支持"))));
        votes.add(new Vote<>(Set.of(
                new VoteItem<>(proposals.get(0), "支持"))));
        votes.add(new Vote<>(Set.of(
                new VoteItem<>(proposals.get(0), "反对"))));
        votes.add(new Vote<>(Set.of(
                new VoteItem<>(proposals.get(0), "反对"))));

        List<Proposal> finalProposals2 = proposals;
        Set<Vote<Proposal>> finalVotes2 = votes;
        assertThrows(RuntimeException.class, () -> ss.getVoteStatistics(voteType, voters, finalProposals2, finalVotes2));


        proposals = new ArrayList<>();
        proposals.add(new Proposal("提案1", Calendar.getInstance()));

        votes = new HashSet<>();
        votes.add(new RealNameVote<>(Set.of(
                new VoteItem<>(proposals.get(0), "反对")),
                new Voter("A")));
        votes.add(new RealNameVote<>(Set.of(
                new VoteItem<>(proposals.get(0), "支持")),
                new Voter("B")));
        votes.add(new RealNameVote<>(Set.of(
                new VoteItem<>(proposals.get(0), "支持")),
                new Voter("C")));
        votes.add(new RealNameVote<>(Set.of(
                new VoteItem<>(proposals.get(0), "反对")),
                new Voter("D")));
        votes.add(new RealNameVote<>(Set.of(
                new VoteItem<>(proposals.get(0), "反对")),
                new Voter("E")));
        List<Proposal> finalProposals3 = proposals;
        Set<Vote<Proposal>> finalVotes3 = votes;
        assertEquals(Map.of(proposals.get(0), 61.0), ss.getVoteStatistics(voteType, voters, finalProposals3, finalVotes3));

    }

    /*
     * 覆盖：
     * 提案计票情况 2
     */
    @Test
    public void testDinnerOrderStatisticStrategy() {
        StatisticsStrategy ss = new DinnerOrderStatisticStrategy();

        Map<String, Integer> options = new HashMap<>();
        options.put("喜欢", 2);
        options.put("不喜欢", 0);
        options.put("无所谓", 1);
        VoteType voteType = new VoteType(options);

        Map<Voter, Double> voters = Map.of(
                new Voter("爷爷"), 4.0,
                new Voter("爸爸"), 1.0,
                new Voter("妈妈"), 2.0,
                new Voter("儿子"), 2.0
        );

        List<Dish> dishes = new ArrayList<>();
        dishes.add(new Dish("A", 10));
        dishes.add(new Dish("B", 40));
        dishes.add(new Dish("C", 20));

        Set<Vote<Dish>> votes = new HashSet<>();
        votes.add(new RealNameVote<>(Set.of(
                new VoteItem<>(dishes.get(0), "喜欢"),
                new VoteItem<>(dishes.get(1), "喜欢"),
                new VoteItem<>(dishes.get(2), "无所谓")
        ), new Voter("爷爷")));
        votes.add(new RealNameVote<>(Set.of(
                new VoteItem<>(dishes.get(0), "无所谓"),
                new VoteItem<>(dishes.get(1), "喜欢"),
                new VoteItem<>(dishes.get(2), "喜欢")
        ), new Voter("爸爸")));
        votes.add(new RealNameVote<>(Set.of(
                new VoteItem<>(dishes.get(0), "喜欢"),
                new VoteItem<>(dishes.get(1), "不喜欢"),
                new VoteItem<>(dishes.get(2), "不喜欢")
        ), new Voter("妈妈")));
        votes.add(new RealNameVote<>(Set.of(
                new VoteItem<>(dishes.get(0), "喜欢"),
                new VoteItem<>(dishes.get(1), "无所谓"),
                new VoteItem<>(dishes.get(2), "喜欢")
        ), new Voter("儿子")));

        assertEquals(Map.of(
                dishes.get(0), 17.0,
                dishes.get(1), 12.0,
                dishes.get(2), 10.0
        ), ss.getVoteStatistics(voteType, voters, dishes, votes));
//        assertThrows(RuntimeException.class, () -> ss.getVoteStatistics(dishes, votes, voteType));
    }

    /*
     * 覆盖：
     * 选举计票情况
     */
    @Test
    public void testElectionStatisticStrategy() {
        StatisticsStrategy ss = new ElectionStatisticStrategy();

        Map<String, Integer> options = new HashMap<>();
        options.put("支持", 1);
        options.put("反对", -1);
        options.put("弃权", 0);
        VoteType voteType = new VoteType(options);

        List<Person> people = new ArrayList<>();
        people.add(new Person("Alice", 18));
        people.add(new Person("Bob", 19));
        people.add(new Person("Cathy", 20));

        Set<Vote<Person>> votes = new HashSet<>();
        votes.add(new Vote<>(Set.of(
                new VoteItem<>(people.get(0), "支持"),
                new VoteItem<>(people.get(1), "反对"),
                new VoteItem<>(people.get(2), "支持")
        )));
        votes.add(new Vote<>(Set.of(
                new VoteItem<>(people.get(0), "反对"),
                new VoteItem<>(people.get(1), "反对"),
                new VoteItem<>(people.get(2), "支持")
        )));
        votes.add(new Vote<>(Set.of(
                new VoteItem<>(people.get(0), "弃权"),
                new VoteItem<>(people.get(1), "支持"),
                new VoteItem<>(people.get(2), "支持")
        )));
        votes.add(new Vote<>(Set.of(
                new VoteItem<>(people.get(0), "反对"),
                new VoteItem<>(people.get(1), "反对"),
                new VoteItem<>(people.get(2), "反对")
        )));

        assertEquals(Map.of(
                people.get(0), -1.0,
                people.get(1), -2.0,
                people.get(2), 2.0
        ), ss.getVoteStatistics(voteType, null, people, votes));
    }

}
