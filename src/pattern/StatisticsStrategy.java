package pattern;

import auxiliary.Voter;
import vote.Vote;
import vote.VoteType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface StatisticsStrategy {
    /**
     * 根据候选人列表、投票信息，统计投票结果
     * @param voteType 投票选项类型
     * @param voters 投票人及其权重
     * @param candidates 输入的候选人列表
     * @param votes 输入有效投票信息
     * @return 计票结果，任意 (k, v) 属于返回的 Map ，表示候选人 k 的得分为 v
     */
     <C> Map<C, Double> getVoteStatistics(VoteType voteType,  Map<Voter, Double> voters, List<C> candidates, Set<Vote<C>> votes);
}
