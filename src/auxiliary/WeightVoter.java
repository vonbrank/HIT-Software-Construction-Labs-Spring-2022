package auxiliary;

import java.util.Objects;

public class WeightVoter extends Voter {

    private final Integer weight;

    public WeightVoter(String ID, Integer weight) {
        super(ID);
        this.weight = weight;
    }

    public Integer getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WeightVoter that = (WeightVoter) o;
        return Objects.equals(weight, that.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), weight);
    }
}
