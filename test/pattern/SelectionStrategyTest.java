package pattern;

import static org.junit.jupiter.api.Assertions.*;

import auxiliary.Dish;
import auxiliary.Person;
import auxiliary.Proposal;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/*
 * Testing strategy
 * 等价类划分：
 *
 * 商业投票:
 * 1. statistics.size() != 1
 * 2. statistics.size() = 1 ，提案百分比不在 0 - 100 之间
 * 3. statistics.size() = 1 ，提案百分比在 0 - 100 之间，表决通过
 * 4. statistics.size() = 1 ，提案百分比在 0 - 100 之间，表决不通过
 *
 * 选举:
 * 1. statistics.size() <= k
 * 2. statistics.size() > k ，存在不确定
 * 3. statistics.size() > k ，不存在不确定
 *
 * 针对点菜的样例
 * 1. statistics.size() <= k
 * 2. statistics.size() > k ，存在不确定
 * 3. statistics.size() > k ，不存在不确定
 */

class SelectionStrategyTest {
    /*
     * 覆盖：
     * 商业投票
     */
    @Test
    public void testBusinessVotingSelectionStrategy() {
        SelectionStrategy ss = new BusinessVotingSelectionStrategy();
        Calendar data = Calendar.getInstance();

        List<Proposal> proposalList = List.of(
                new Proposal("提案1", data),
                new Proposal("提案2", data)
        );

        Map<Proposal, Double> statistics = Map.of(
                proposalList.get(0), 51.0,
                proposalList.get(1), 80.0
        );

        Map<Proposal, Double> finalStatistics = statistics;
        assertThrows(RuntimeException.class, () -> ss.getSelectionResult(finalStatistics));

        statistics = Map.of(
                proposalList.get(0), 151.0
        );

        Map<Proposal, Double> finalStatistics1 = statistics;
        assertThrows(RuntimeException.class, () -> ss.getSelectionResult(finalStatistics1));

        statistics = Map.of(
                proposalList.get(0), 80.0
        );

        assertEquals(Map.of(proposalList.get(0), 1.0), ss.getSelectionResult(statistics));

        statistics = Map.of(
                proposalList.get(1), 51.0
        );

        assertEquals(Map.of(proposalList.get(1), 0.0), ss.getSelectionResult(statistics));


    }

    /*
     * 覆盖：
     * 选举
     */
    @Test
    public void testElectionSelectionStrategy() {
        SelectionStrategy ss = new ElectionSelectionStrategy(2);

        List<Person> people = List.of(
                new Person("Alice", 19),
                new Person("Boc", 19),
                new Person("Cathy", 20)
        );

        Map<Person, Double> statistics = Map.of(
                people.get(0), 3.0,
                people.get(1), 5.0
        );

        assertEquals(Map.of(
                people.get(0), 2.0,
                people.get(1), 1.0
        ), ss.getSelectionResult(statistics));

        statistics = Map.of(
                people.get(0), 3.0,
                people.get(1), 5.0,
                people.get(2), 3.0
        );

        assertEquals(Map.of(
                people.get(1), 1.0
        ), ss.getSelectionResult(statistics));

        statistics = Map.of(
                people.get(2), 6.0,
                people.get(1), 5.0,
                people.get(0), 3.0
        );

        assertEquals(Map.of(
                people.get(2), 1.0,
                people.get(1), 2.0
        ), ss.getSelectionResult(statistics));


    }

    /*
     * 覆盖：
     * 点菜
     */
    @Test
    public void testDinnerOrderSelectionStrategy() {
        SelectionStrategy ss = new DinnerOrderSelectionStrategy(2);

        List<Dish> dishList = List.of(
                new Dish("A", 19),
                new Dish("B", 19),
                new Dish("C", 20)
        );

        Map<Dish, Double> statistics = Map.of(
                dishList.get(1), 5.0
        );

        assertEquals(Map.of(
                dishList.get(1), 1.0
        ), ss.getSelectionResult(statistics));

        statistics = Map.of(
                dishList.get(0), 3.0,
                dishList.get(1), 5.0,
                dishList.get(2), 3.0
        );

        assertTrue(Map.of(
                dishList.get(1), 1.0,
                dishList.get(0), 2.0
        ).equals(ss.getSelectionResult(statistics)) ||
                Map.of(
                        dishList.get(1), 1.0,
                        dishList.get(2), 2.0
                ).equals(ss.getSelectionResult(statistics)));


        statistics = Map.of(
                dishList.get(2), 6.0,
                dishList.get(1), 5.0,
                dishList.get(0), 3.0
        );

        assertEquals(Map.of(
                dishList.get(2), 1.0,
                dishList.get(1), 2.0
        ), ss.getSelectionResult(statistics));

    }


}
