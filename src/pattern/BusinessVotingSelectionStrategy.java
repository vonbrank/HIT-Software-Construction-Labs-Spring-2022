package pattern;

import java.util.HashMap;
import java.util.Map;

public class BusinessVotingSelectionStrategy implements SelectionStrategy {

    // 提案数量
    int quantity;

    public BusinessVotingSelectionStrategy() {
        quantity = 1;
    }

    public BusinessVotingSelectionStrategy(int quantity) {
        this.quantity = quantity;
    }

    /**
     * 根据候选人得分计算每个候选人的名次
     *
     * @param statistics 输入的statistics.size() 必须 = quantity ，对于每一对键值 (k, v) ，表示 k
     * @return 提案通过情况，返回的每一键值对 k, v 表示提案的通过情况，v = 0 表示未通过， v = 1 表示通过，
     * statistics 中的 v 超过 2/3 则通过，即 v >= 200 / 3
     */
    @Override
    public <C> Map<C, Double> getSelectionResult(Map<C, Double> statistics) {
        if (statistics.size() != quantity) throw new RuntimeException(String.format("候选提案数只能为 %d .", quantity));
        Map<C, Double> res = new HashMap<>();
        for (C candidate : statistics.keySet()) {
            if (!(statistics.get(candidate) >= 0 && statistics.get(candidate) <= 100))
                throw new RuntimeException("提案支持率百分比应该在 0 - 100 之间");
            res.put(candidate, statistics.get(candidate) >= 200 / 3.0 ? 1.0 : 0.0);
        }
        return res;
    }
}

