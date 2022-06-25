package poll;

import pattern.NoWeightDinnerOrderStatisticStrategy;

public class NoWeightDinnerOrder extends DinnerOrder {
    @Override
    public void statistics() {
        statistics(new NoWeightDinnerOrderStatisticStrategy(voteType));
    }
}
