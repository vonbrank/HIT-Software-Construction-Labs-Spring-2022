package pattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DinnerOrderSelectionStrategy implements SelectionStrategy {

    // 拟选出的菜品数量
    int quantity;

    public DinnerOrderSelectionStrategy(int quantity) {
        this.quantity = quantity;
    }

    /**
     * 根据候选人得分计算每个候选菜的名次
     *
     * @param statistics 输入的候选人得分，任意 (k, v) 属于 statistics 表示候选菜 k 得分为 v
     * @return 候选对象名次，任意 (k, v) 属于返回值，表示候选对象 k 的名次为 v，包含明确在前 quantity 名的菜品，
     * 以及随机选出的、得分相等的不明确在 quantity 的菜品，凑出齐 quantity 个菜品
     */
    @Override
    public <C> Map<C, Double> getSelectionResult(Map<C, Double> statistics) {
        List<Node<C>> nodeList = new ArrayList<>();
        statistics.forEach((k, v) -> nodeList.add(new Node<>(k, v)));
        nodeList.sort((node1, node2) -> (int) Math.round(node2.score - node1.score));
        int lastNum = Math.min(nodeList.size(), quantity);
        Map<C, Double> res = new HashMap<>();
        for (int i = 0; i < lastNum; i++) {
            res.put(nodeList.get(i).candidate, (double) (i + 1));
        }
        return res;
    }

    private class Node<C> {
        final public C candidate;
        final public double score;

        Node(C candidate, double score) {
            this.candidate = candidate;
            this.score = score;
        }

    }
}

