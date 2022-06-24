package vote;

import java.util.Set;

public interface Voteable<C> {
    /**
     * 查询该选票中包含的所有投票项
     *
     * @return 所有投票项
     */
    public Set<VoteItem<C>> getVoteItems();
    /**
     * 一个特定候选人是否包含本选票中
     *
     * @param candidate 待查询的候选人
     * @return 若包含该候选人的投票项，则返回true，否则false
     */
    public boolean candidateIncluded(C candidate);
}
