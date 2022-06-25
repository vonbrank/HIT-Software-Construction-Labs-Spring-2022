package poll;

import auxiliary.Dish;
import pattern.*;
import vote.VoteType;

import java.util.Calendar;

public class DinnerOrder extends GeneralPollImpl<Dish> implements Poll<Dish> {
    // Rep Invariants
    //   voters 数量 <= quantity <= candidates 数量
    //   投票人 voters 数量 >= 1
    //   候选对象 candidates 数量 >= 1
    //	 选票 votes 数量 >= 1
    //   voteType 有 (支持, 1), (反对, -1), (弃权, 0)
    // Abstract Function
    //   AF(rep) = 一次投票，名称为 name , 日期为 date , 候选人集合为 candidates ,
    //   投票人及其权重集合为 votes ,
    //   拟选出选票最大数量为 quantity ,
    //   合法选项及其分数、所有选票集合、机票结果和遴选结果分别为
    //   voteType, votes, statistics, results
    // Safety from Rep Exposure
    //   由方法参数传入的 mutable 对象将通过防御式拷贝赋值给 rep
    //   ADT 本身是 mutable 的，但是各 mutable 的 rep 不会作为某个方法的返回值。
    @Override
    boolean checkRep() {
        boolean res = (voters.size() <= quantity && quantity <= candidates.size());
        boolean tmp = super.checkRep();
        if (!tmp) res = false;
        return res;
    }

    @Override
    public void setInfo(String name, Calendar date, VoteType type, int quantity) {
        super.setInfo(name, date, type, quantity);
        this.checkVoteValidityStrategy = new DefaultCheckVoteValidityStrategy();
    }

    public void statistics() {
        statistics(new DinnerOrderStatisticStrategy());
    }

    public void selection() {
        selection(new DinnerOrderSelectionStrategy(quantity));
    }
}
