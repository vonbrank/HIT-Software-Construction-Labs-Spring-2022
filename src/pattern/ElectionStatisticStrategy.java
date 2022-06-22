package pattern;

import vote.Vote;
import vote.VoteType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ElectionStatisticStrategy implements StatisticsStrategy {

    /**
     * 根据候选人列表、投票信息，统计投票结果
     *
     * @param candidates 输入的候选人列表
     * @param votes      输入有效投票信息
     * @param voteType   投票选项类型
     * @return 计票结果，任意 (k, v) 属于返回的 Map ，表示候选人 k 的得分为 v
     * 其中 v 表示所有对 k 支持投票人的选择得分之和
     */
    @Override
    public <Person> Map<Person, Integer> getVoteStatistics(List<Person> candidates, Set<Vote<Person>> votes, VoteType voteType) {
        Map<Person, Integer> res = new HashMap<>();
        candidates.forEach(candidate -> res.put(candidate, 0));
        votes.forEach(vote -> vote.getVoteItems().forEach(voteItem-> {
            Person candidate = voteItem.getCandidate();
            int score = res.get(candidate) + voteType.getScoreByOption(voteItem.getVoteValue());
            res.put(candidate, score);
        }));
        return res;
    }
}
