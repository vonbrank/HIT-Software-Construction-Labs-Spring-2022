package pattern;

import java.util.Comparator;
import java.util.Map;

public class AccurateElectionSelectionStrategy extends ElectionSelectionStrategy {

    private static int encodeScoreBase = (1 << 15);

    protected Comparator<Node> cmp = (node1, node2) -> {
        int score1 = (int) node1.score;
        int score2 = (int) node2.score;
        int first1 = score1 / encodeScoreBase;
        int second1 = score1 % encodeScoreBase;
        int first2 = score2 / encodeScoreBase;
        int second2 = score2 % encodeScoreBase;
        if(first1 != first2) return first1 - first2;
        return second1 - second2;
    };

    public AccurateElectionSelectionStrategy(int quantity) {
        super(quantity);
    }

    @Override
    public <C> Map<C, Double> getSelectionResult(Map<C, Double> statistics) {
        return super.getSelectionResult(statistics);
    }
}
