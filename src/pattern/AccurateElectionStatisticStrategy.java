package pattern;

public class AccurateElectionStatisticStrategy extends ElectionStatisticStrategy {
    private static int encodeScoreBase = (1 << 15);
    @Override
    protected int encodeScore(int previousScore, int delta) {
        int first = previousScore / encodeScoreBase;
        int second = previousScore % encodeScoreBase;
        if(delta > 0) first++;
        if(delta < 0) second++;
        return first * encodeScoreBase + second;
    }
}
