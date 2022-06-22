package pattern;

import vote.Vote;
import vote.VoteType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultCheckVoteValidityStrategy implements CheckVoteValidityStrategy {
    @Override
    public <C> boolean checkVoteValidity(List<C> targetCandidates, Vote<C> newVote, VoteType voteType) {
        AtomicBoolean voteValid = new AtomicBoolean(true);
        targetCandidates.forEach(candidate -> {
            if (!newVote.candidateIncluded(candidate)) voteValid.set(false);
        });
        Set<C> polledCandidates = new HashSet<>();
        newVote.getVoteItems().forEach(voteItem -> {
            if (!targetCandidates.contains(voteItem.getCandidate())) voteValid.set(false);
            if (!voteType.checkLegality(voteItem.getVoteValue())) voteValid.set(false);
            if (polledCandidates.contains(voteItem.getCandidate())) voteValid.set(false);
            polledCandidates.add(voteItem.getCandidate());
        });
        return voteValid.get();
    }
}

