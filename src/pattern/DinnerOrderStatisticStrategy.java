package pattern;

import auxiliary.Voter;
import vote.RealNameVote;
import vote.Vote;
import vote.VoteType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DinnerOrderStatisticStrategy implements StatisticsStrategy {

    /**
     * 根据候选人列表、投票信息，统计投票结果
     *
     * @param voteType   投票选项类型
     * @param voters 投票人及其权重
     * @param candidates 输入的候选菜品列表
     * @param votes      输入有效投票信息，需要是 RealNameVote，且其 voter 是 WeightVoter 类型的
     * @return 计票结果，任意 (k, v) 属于返回的 Map ，表示候选菜品 k 的得分为 v ，
     * 其中 v 表示所有对 k 支持投票人的权重及其偏好得分乘积之和
     */
    @Override
    public <Dish> Map<Dish, Double> getVoteStatistics(
            VoteType voteType, Map<Voter, Double> voters, List<Dish> candidates, Set<Vote<Dish>> votes) {
        Map<Dish, Double> res = new HashMap<>();
        candidates.forEach(candidate -> res.put(candidate, 0.0));
        votes.forEach(vote -> {
            if(!(vote instanceof RealNameVote)) throw new RuntimeException("聚餐点菜为实名计票");
            RealNameVote<Dish> realNameVote = (RealNameVote<Dish>) vote;

            double weight = voters.get(realNameVote.getVoter());

            vote.getVoteItems().forEach(voteItem-> {
                Dish candidate = voteItem.getCandidate();
                double score = res.get(candidate) + weight * voteType.getScoreByOption(voteItem.getVoteValue());
                res.put(candidate, score);
            });
        });
        return res;
    }
}
