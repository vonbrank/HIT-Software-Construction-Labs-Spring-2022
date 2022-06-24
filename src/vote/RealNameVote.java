package vote;

import auxiliary.Voter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

//immutable
public class RealNameVote<C> extends Vote<C> {

    //投票人
    private final Voter voter;

    public RealNameVote(Set<VoteItem<C>> voteItems, Voter voter) {
        super(voteItems);
        this.voter = voter;
    }

    public Voter getVoter() {
        return this.voter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RealNameVote<?> that = (RealNameVote<?>) o;
        return Objects.equals(voter, that.voter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), voter);
    }
}
