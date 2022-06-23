package pattern;

import java.util.Map;

public interface SelectionStrategy {

    /**
     * 根据候选人得分计算每个候选人的名次
     *
     * @param statistics 输入的候选人得分，任意 (k, v) 属于 statistics 表示候选人 k 得分为 v
     * @param <C>        候选对象类型
     * @return 候选对象名次，任意 (k, v) 属于返回值，表示候选对象 k 的名次为 v
     */
    <C> Map<C, Double> getSelectionResult(Map<C, Double> statistics);

}

