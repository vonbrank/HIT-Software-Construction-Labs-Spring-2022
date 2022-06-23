package pattern;

import auxiliary.Voter;
import exception.VoteInvalidException;
import vote.Vote;
import vote.VoteType;

import java.util.List;
import java.util.Map;

public interface CheckVoteValidityStrategy {
    /**
     * 根据固有候选对象、投票选项，检查新增投票是否合法
     *
     * @param targetCandidates 候选对象
     * @param newVote          新增投票
     * @param voteType         投票选项
     * @param <C>              候选对象类型
     * @return 合法返回 true ，否则返回 false
     */
    <C> boolean checkAddVoteValidity(List<C> targetCandidates, Vote<C> newVote, VoteType voteType);

    /**
     * 根据投票人列表和选票列表，在开始计票前检验其合法性
     * @param voters 投票人列表及其权重
     * @param votes 选票列表及其有效性，若一个投票人提交了多张选票，则它们均为非法，计票时不计算在内，即被标记为 false
     *              此方法将直接修改这个对象
     * @param <C> 候选人类型
     * @throws VoteInvalidException 有人没有投票时抛出异常
     * @return 返回修改后的有效票集合
     */
    <C> Map<Vote<C>, Boolean> checkVoteValidity(Map<Voter, Double> voters, Map<Vote<C>, Boolean> votes) throws VoteInvalidException;
}
