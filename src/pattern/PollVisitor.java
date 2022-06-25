package pattern;

import vote.Vote;

import java.util.Map;

public interface PollVisitor<C> {
    void visitVote(Map<Vote<C>, Boolean> votes);
}
