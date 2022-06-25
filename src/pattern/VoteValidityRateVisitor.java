package pattern;

import vote.Vote;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class VoteValidityRateVisitor<C> implements PollVisitor<C> {

    private double voteValidityRate = 0;

    @Override
    public void visitVote(Map<Vote<C>, Boolean> votes) {
        int total = votes.size();
        AtomicInteger validityNum = new AtomicInteger();
        votes.forEach((k, v) -> {
            if (v) validityNum.getAndIncrement();
        });
        voteValidityRate = validityNum.get() / (double) total;
    }

    public double getVoteValidityRate() {
        return voteValidityRate;
    }
}
