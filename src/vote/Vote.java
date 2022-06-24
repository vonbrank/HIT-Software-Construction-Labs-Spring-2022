package vote;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

//immutable
public class Vote<C> {

    // 缺省为“匿名”投票，即不需要管理投票人的信息

    // 一个投票人对所有候选对象的投票项集合
    private Set<VoteItem<C>> voteItems = new HashSet<>();
    // 投票时间
    private Calendar date = Calendar.getInstance();
    // 选票 id
    long id;

    // Rep Invariants
    //   voteItems 和 date 都不为 null 且 Vote<C> 本身是 immutable 的
    // Abstract Function
    //   AF(voteItems, date) = 一次投票，voteItems 包含了改投票的所有内容，date 是这次投票的创建时间
    // Safety from Rep Exposure
    //   Vote<C> 是 immutable 的，voteItems 和 date 只在构造函数内被赋值，没有其他 mutator
    //   返回 voteItems 的方法将执行防御式拷贝

    private boolean checkRep() {
        return voteItems != null && date != null;
    }

    /**
     * 创建一个选票对象
     * <p>
     * 可以自行设计该方法所采用的参数
     */
    public Vote(Set<VoteItem<C>> voteItems) {
        this.voteItems.addAll(voteItems);
        Random r = new Random();
        this.id = r.nextLong();
    }

    /**
     * 查询该选票中包含的所有投票项
     *
     * @return 所有投票项
     */
    public Set<VoteItem<C>> getVoteItems() {
        return new HashSet<>(voteItems);
    }

    /**
     * 一个特定候选人是否包含本选票中
     *
     * @param candidate 待查询的候选人
     * @return 若包含该候选人的投票项，则返回true，否则false
     */
    public boolean candidateIncluded(C candidate) {
        AtomicBoolean res = new AtomicBoolean(false);
        voteItems.forEach(voteItem -> {
            if (voteItem.getCandidate().equals(candidate)) res.set(true);
        });
        return res.get();
    }

    @Override
    public int hashCode() {
        return Objects.hash(voteItems, id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote<?> vote = (Vote<?>) o;
        return voteItems.equals(vote.voteItems) && id == vote.id;
    }
}
