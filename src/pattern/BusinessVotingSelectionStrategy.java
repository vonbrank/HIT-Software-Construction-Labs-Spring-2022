package pattern;

import java.util.Map;

public class BusinessVotingSelectionStrategy implements SelectionStrategy {

    /**
     * 根据候选人得分计算每个候选人的名次
     *
     * @param statistics 输入的statistics.size() 必须 = 1 ，唯一对键值 (k, v) ，表示 k
     * @return 提案通过情况，返回的唯一键值对 k, v 表示提案的通过情况，v = 0 表示未通过， v = 1 表示通过，
     * statistics 中的 v 超过 2/3 则通过，即 v >= 200 / 3
     */
    @Override
    public <C> Map<C, Double> getSelectionResult(Map<C, Double> statistics) {
        if (statistics.size() != 1) throw new RuntimeException("应该只有一个提案");
        for (C candidate : statistics.keySet()) {
            if (!(statistics.get(candidate) >= 0 && statistics.get(candidate) <= 100))
                throw new RuntimeException("提案支持率百分比应该在 0 - 100 之间");
            return Map.of(candidate, statistics.get(candidate) >= 200 / 3.0 ? 1.0 : 0.0);
        }
        return null;
    }
}

