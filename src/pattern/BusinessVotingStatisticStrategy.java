package pattern;

import auxiliary.WeightVoter;
import vote.RealNameVote;
import vote.Vote;
import vote.VoteType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BusinessVotingStatisticStrategy implements StatisticsStrategy {

    /**
     * 根据投票信息，统计投票结果
     *
     * @param candidates 提案列表，长度为 1
     * @param votes      输入的投票信息，需要是 RealNameVote，且其 voter 是 WeightVoter 类型的
     * @param voteType   投票选项类型
     * @return 计票结果，任意 (k, v) 属于返回的 Map ，表示提案 k 支持的百分比 v，其中 v 表示所有对 k 支持投票人的权重和
     */
    @Override
    public <Proposal> Map<Proposal, Integer> getVoteStatistics(List<Proposal> candidates, Set<Vote<Proposal>> votes, VoteType voteType) {
        if (candidates.size() != 1) throw new RuntimeException("候选提案数只能为 1 .");
        Map<Proposal, Integer> res = new HashMap<>();
        candidates.forEach(candidate -> res.put(candidate, 0));
        votes.forEach(vote -> {
            if (!(vote instanceof RealNameVote)) throw new RuntimeException("商业提案为实名计票");
            RealNameVote<Proposal> realNameVote = (RealNameVote<Proposal>) vote;

            if (!(realNameVote.getVoter() instanceof WeightVoter)) throw new RuntimeException("商业提案的投票人需要有权重");
            WeightVoter weightVoter = (WeightVoter) realNameVote.getVoter();
            int weight = weightVoter.getWeight();
//            System.out.println(vote.getVoteItems().size());

            vote.getVoteItems().forEach(voteItem -> {
                Proposal candidate = voteItem.getCandidate();
                if (voteItem.getVoteValue().equals("支持")) {
                    int newWeight = res.get(candidate) + weight;
//                System.out.println(newWeight);
                    res.put(candidate, newWeight);
                }
            });
        });
        return res;
    }
}

