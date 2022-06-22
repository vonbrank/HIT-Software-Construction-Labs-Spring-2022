package poll;

import auxiliary.Person;
import auxiliary.Voter;
import org.junit.jupiter.api.Test;
import vote.Vote;
import vote.VoteItem;
import vote.VoteType;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ElectionTest {

    /*
     * Testing strategy
     *
     * 给出一组商业表决的数据，及其表决结果的字符串，测试 Election 的正确性
     * Election 的实例直接调用构造函数产生
     *
     * 覆盖 Election 中完成一次表决的所有方法
     */

    @Test
    void test() {
        Poll<Person> poll = new Election();

        Map<String, Integer> options = new HashMap<>();
        options.put("支持", 1);
        options.put("反对", -1);
        options.put("弃权", 0);
        VoteType voteType = new VoteType(options);

        List<Person> people = new ArrayList<>();
        people.add(new Person("A", 11));
        people.add(new Person("B", 14));
        people.add(new Person("C", 12));
        people.add(new Person("D", 18));
        people.add(new Person("E", 15));

        Map<Voter, Double> voters = new HashMap<>();
        voters.put(new Voter("投票人1"), 1.0);
        voters.put(new Voter("投票人2"), 1.0);
        voters.put(new Voter("投票人3"), 1.0);
        voters.put(new Voter("投票人4"), 1.0);
        voters.put(new Voter("投票人5"), 1.0);
        voters.put(new Voter("投票人6"), 1.0);

        List<Vote<Person>> voteItems = new ArrayList<>();

        Map<String, List<String>> voterChoices = Map.of(
                "投票人1", List.of("支持", "反对", "支持", "反对", "弃权"),
                "投票人2", List.of("反对", "反对", "支持", "支持", "支持"),
                "投票人3", List.of("弃权", "支持", "支持", "支持", "支持"),
                "投票人4", List.of("反对", "反对", "反对", "反对", "反对"),
                "投票人5", List.of("反对", "反对", "支持", "支持", "支持"),
                "投票人6", List.of("弃权", "支持", "支持", "反对", "弃权")
        );

        voterChoices.forEach((voterID, choices) -> {
            Set<VoteItem<Person>> voteItem = new HashSet<>();
            for (int i = 0; i < choices.size(); i++) {
                voteItem.add(new VoteItem<>(people.get(i), choices.get(i)));
            }
            voteItems.add(new Vote<>(voteItem));
        });

        List<String> selectedCandidates = List.of("C");

        poll.setInfo("Election", Calendar.getInstance(), voteType, 3);

        poll.addCandidates(people);

        poll.addVoters(voters);

        voteItems.forEach(poll::addVote);

        assertEquals(String.join("\n", selectedCandidates), poll.result());

    }

}
