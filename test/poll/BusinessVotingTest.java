package poll;

import static org.junit.jupiter.api.Assertions.*;

import auxiliary.Proposal;
import auxiliary.Voter;
import org.junit.jupiter.api.Test;
import pattern.BusinessVotingSelectionStrategy;
import pattern.BusinessVotingStatisticStrategy;
import pattern.DefaultCheckVoteValidityStrategy;
import vote.RealNameVote;
import vote.Vote;
import vote.VoteItem;
import vote.VoteType;

import java.util.*;

class BusinessVotingTest {

    /*
     * Testing strategy
     *
     * 给出一组商业表决的数据，及其表决结果的字符串，测试 BusinessVoting 的正确性
     * BusinessVoting 的实例直接调用构造函数产生
     *
     * 覆盖 BusinessVoting 中完成一次表决的所有方法，包括通过与不通过
     */

    @Test
    void test() {
        Poll<Proposal> poll = new BusinessVoting();

        Map<String, Integer> options = new HashMap<>();
        options.put("支持", 1);
        options.put("反对", 1);
        VoteType voteType = new VoteType(options);

        Proposal proposal = new Proposal("提案1", Calendar.getInstance());

        Map<Voter, Double> voters = Map.of(
                new Voter("A"), 5.0,
                new Voter("B"), 51.0,
                new Voter("C"), 10.0,
                new Voter("D"), 24.0,
                new Voter("E"), 20.0
        );

        Set<Vote<Proposal>> votes = Set.of(
                new RealNameVote<>(Set.of(new VoteItem<>(proposal, "反对")), new Voter("A")),
                new RealNameVote<>(Set.of(new VoteItem<>(proposal, "支持")), new Voter("B")),
                new RealNameVote<>(Set.of(new VoteItem<>(proposal, "支持")), new Voter("C")),
                new RealNameVote<>(Set.of(new VoteItem<>(proposal, "反对")), new Voter("D")),
                new RealNameVote<>(Set.of(new VoteItem<>(proposal, "弃权")), new Voter("E"))
        );

        poll.setInfo("商业表决", Calendar.getInstance(), voteType, 1);

        poll.addCandidates(List.of(proposal));

        poll.addVoters(voters);

        poll.setCheckVoteValidityStrategy(new DefaultCheckVoteValidityStrategy());

        votes.forEach(poll::addVote);

        poll.statistics(new BusinessVotingStatisticStrategy());
        poll.selection(new BusinessVotingSelectionStrategy());

        assertEquals("提案1, 0", poll.result());
        assertEquals("name: 商业表决, candidates: (提案1)", poll.toString());

        poll = new BusinessVoting();

        votes = Set.of(
                new RealNameVote<>(Set.of(new VoteItem<>(proposal, "反对")), new Voter("A")),
                new RealNameVote<>(Set.of(new VoteItem<>(proposal, "支持")), new Voter("B")),
                new RealNameVote<>(Set.of(new VoteItem<>(proposal, "支持")), new Voter("C")),
                new RealNameVote<>(Set.of(new VoteItem<>(proposal, "支持")), new Voter("D")),
                new RealNameVote<>(Set.of(new VoteItem<>(proposal, "弃权")), new Voter("E"))
        );

        poll.setInfo("商业表决", Calendar.getInstance(), voteType, 1);

        poll.addCandidates(List.of(proposal));

        poll.addVoters(voters);

        poll.setCheckVoteValidityStrategy(new DefaultCheckVoteValidityStrategy());

        votes.forEach(poll::addVote);

        poll.statistics(new BusinessVotingStatisticStrategy());
        poll.selection(new BusinessVotingSelectionStrategy());

        assertEquals("提案1, 1", poll.result());
        assertEquals("name: 商业表决, candidates: (提案1)", poll.toString());

    }

}
