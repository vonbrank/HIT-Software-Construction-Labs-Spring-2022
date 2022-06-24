package vote;

import java.util.Set;

public abstract class VoteDecorator<C> implements Voteable<C> {

    Voteable<C> target;

    public VoteDecorator(Voteable<C> target) {
        this.target = target;
    }

    /**
     * 查询该选票中包含的所有投票项
     *
     * @return 所有投票项
     */
    @Override
    public Set<VoteItem<C>> getVoteItems() {
        return null;
    }

    /**
     * 一个特定候选人是否包含本选票中
     *
     * @param candidate 待查询的候选人
     * @return 若包含该候选人的投票项，则返回true，否则false
     */
    @Override
    public boolean candidateIncluded(C candidate) {
        return false;
    }
}
