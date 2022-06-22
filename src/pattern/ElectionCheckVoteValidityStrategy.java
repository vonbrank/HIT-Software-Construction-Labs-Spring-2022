package pattern;

import vote.Vote;
import vote.VoteType;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ElectionCheckVoteValidityStrategy extends DefaultCheckVoteValidityStrategy {
    // 支持票数量上限
    private final int quantity;

    /**
     * 构造选举投票专用的投票有效性检查策略
     *
     * @param quantity 支持票数量上限
     */
    ElectionCheckVoteValidityStrategy(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public <C> boolean checkVoteValidity(List<C> targetCandidates, Vote<C> newVote, VoteType voteType) {
        boolean res = super.checkVoteValidity(targetCandidates, newVote, voteType);
        AtomicInteger voteQuantity = new AtomicInteger();
        newVote.getVoteItems().forEach(voteItem -> {
            if (voteItem.getVoteValue().equals("支持")) voteQuantity.getAndIncrement();
        });
        if (voteQuantity.get() > quantity) res = false;
        return res;
    }
}
