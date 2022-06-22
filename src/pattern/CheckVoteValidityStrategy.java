package pattern;

import vote.Vote;
import vote.VoteType;

import java.util.List;
import java.util.Set;

public interface CheckVoteValidityStrategy {
    /**
     * 根据固有候选对象、已有投票信息、投票选项，检查新增投票是否合法
     * @param targetCandidates 候选对象
     * @param newVote 新增投票
     * @param polledVotes 已添加投票
     * @param voteType 投票选项
     * @param <C>  候选对象类型
     * @return 合法返回 true ，否则返回 false
     */
    <C> boolean checkVoteValidity(List<C> targetCandidates, Vote<C> newVote,
                                  Set<Vote<C>> polledVotes, VoteType voteType);
}
