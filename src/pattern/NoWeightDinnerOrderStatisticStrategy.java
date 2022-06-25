package pattern;

import vote.VoteType;

public class NoWeightDinnerOrderStatisticStrategy extends DinnerOrderStatisticStrategy {

    int maxOptionScore = 0;

    public NoWeightDinnerOrderStatisticStrategy(VoteType voteType) {
        for(var item : voteType.getOptions().entrySet()) {
            maxOptionScore = Math.max(maxOptionScore, item.getValue());
        }
    }

    protected double calculateScore(double previousScore, double weight, double score) {
        if((int) score == maxOptionScore) previousScore += 1;
        return previousScore;
    }
}
