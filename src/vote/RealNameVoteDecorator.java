package vote;

import auxiliary.Voter;

public class RealNameVoteDecorator<C> extends VoteDecorator<C> {

    //投票人
    private final Voter voter;

    public RealNameVoteDecorator(Voteable<C> target, Voter voter) {
        super(target);
        this.voter = voter;
    }

    public Voter getVoter() {
        return this.voter;
    }
}
