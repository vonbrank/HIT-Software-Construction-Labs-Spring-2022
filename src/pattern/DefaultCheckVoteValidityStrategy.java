package pattern;

import auxiliary.Voter;
import exception.VoteInvalidException;
import vote.RealNameVote;
import vote.Vote;
import vote.VoteType;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultCheckVoteValidityStrategy implements CheckVoteValidityStrategy {
    @Override
    public <C> boolean checkAddVoteValidity(List<C> targetCandidates, Vote<C> newVote, VoteType voteType) {
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

    @Override
    public <C> Map<Vote<C>, Boolean> checkVoteValidity(Map<Voter, Double> voters, Map<Vote<C>, Boolean> votes)
            throws VoteInvalidException {
        Map<Voter, Integer> polledVoters = new HashMap<>();
        Map<Vote<C>, Boolean> res = new HashMap<>();

        for (Vote<C> vote : votes.keySet()) {
            if (!(vote instanceof RealNameVote)) throw new VoteInvalidException("默认使用实名计票。");
            RealNameVote<C> realNameVote = (RealNameVote<C>) vote;
            int current = polledVoters.get(realNameVote.getVoter()) == null ? 0 : polledVoters.get(realNameVote.getVoter());
            polledVoters.put(realNameVote.getVoter(), current + 1);
        }


        for (Voter voter : voters.keySet()) {
            if (!polledVoters.containsKey(voter)) throw new VoteInvalidException("有人未投票。");
        }

        votes.forEach((vote, validity) -> {
            RealNameVote<C> realNameVote = (RealNameVote<C>) vote;
            if (polledVoters.get(realNameVote.getVoter()) > 1) res.put(vote, false);
            else res.put(vote, validity);
        });
        return res;
    }
}

