package poll;

import pattern.AccurateElectionSelectionStrategy;
import pattern.AccurateElectionStatisticStrategy;
import pattern.ElectionSelectionStrategy;
import pattern.ElectionStatisticStrategy;
import poll.Election;

public class AccurateElection extends Election {
    @Override
    public void statistics() {
        statistics(new AccurateElectionStatisticStrategy());
    }

    @Override
    public void selection() {
        selection(new AccurateElectionSelectionStrategy(quantity));
    }
}
