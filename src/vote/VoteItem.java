package vote;

import java.util.Objects;

//immutable
public class VoteItem<C> {

    // 本投票项所针对的候选对象
    private C candidate;
    // 对候选对象的具体投票选项，例如“支持”、“反对”等
    // 无需保存具体数值，需要时可以从投票活动的VoteType对象中查询得到
    private String value;

    // Rep Invariants
    //   本投票项针对的候选对象和投票选项都不为空
    // Abstract Function
    //   AF(candidate, value) = 一个针对 candidate 的投票项，投票选项为 value
    // Safety from Rep Exposure
    //   VoteItem<C> 是 immutable 的，candidate 和 value 只在构造函数中被赋值，没有其他 mutator

    private boolean checkRep() {
        return candidate != null && value != null;
    }

    /**
     * 创建一个投票项对象 例如：针对候选对象“张三”，投票选项是“支持”
     *
     * @param candidate 所针对的候选对象
     * @param value     所给出的投票选项
     */
    public VoteItem(C candidate, String value) {
        this.candidate = candidate;
        this.value = value;
        assert checkRep();
    }

    /**
     * 得到该投票选项的具体投票项
     *
     * @return 返回投票的具体投票项
     */
    public String getVoteValue() {
        return this.value;
    }

    /**
     * 得到该投票选项所对应的候选人
     *
     * @return 返回改投票对应的候选人
     */
    public C getCandidate() {
        return this.candidate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(candidate, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof VoteItem<?>)) return false;
        VoteItem<?> otherVoteItem = (VoteItem<?>) obj;
        return otherVoteItem.candidate.equals(this.candidate) && otherVoteItem.value.equals(this.value);
    }

}
