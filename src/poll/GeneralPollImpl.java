package poll;

import java.util.*;
import java.util.stream.Collectors;

import auxiliary.Voter;
import exception.VoteInvalidException;
import pattern.CheckVoteValidityStrategy;
import pattern.SelectionStrategy;
import pattern.StatisticsStrategy;
import vote.VoteType;
import vote.Vote;

import javax.swing.text.html.parser.Entity;

public class GeneralPollImpl<C> implements Poll<C> {

    // 投票活动的名称
    private String name;
    // 投票活动发起的时间
    private Calendar date;
    // 候选对象集合
    protected List<C> candidates;
    // 投票人集合，key为投票人，value为其在本次投票中所占权重
    protected Map<Voter, Double> voters;
    // 拟选出的候选对象最大数量
    protected int quantity;
    // 本次投票拟采用的投票类型（合法选项及各自对应的分数）
    protected VoteType voteType;
    // 所有选票及其有效性
    protected Map<Vote<C>, Boolean> votes;
    // 计票结果，key为候选对象，value为其得分
    protected Map<C, Double> statistics;
    // 遴选结果，key为候选对象，value为其排序位次
    private Map<C, Double> results;
    // 检查选票合法性策略
    CheckVoteValidityStrategy checkVoteValidityStrategy;

    // Rep Invariants
    //   quantity >= 1
    //   投票人 voters 数量 >= 1
    //   候选对象 candidates 数量 >= 1
    //	 选票 votes 数量 >= 1
    // Abstract Function
    //   AF(rep) = 一次投票，名称为 name , 日期为 date , 候选人集合为 candidates ,
    //   投票人及其权重集合为 votes ,
    //   拟选出选票最大数量为 quantity ,
    //   合法选项及其分数、所有选票集合、机票结果和遴选结果分别为
    //   voteType, votes, statistics, results
    // Safety from Rep Exposure
    //   由方法参数传入的 mutable 对象将通过防御式拷贝赋值给 rep
    //   ADT 本身是 mutable 的，但是各 mutable 的 rep 不会作为某个方法的返回值。

    boolean checkRep() {
        return (quantity >= 1 && voters.size() >= 1 && candidates.size() >= 1 && votes.size() >= 1);
    }

    /**
     * 构造函数
     */
    public GeneralPollImpl() {
        this.voters = new HashMap<>();
        this.candidates = new ArrayList<>();
        this.votes = new HashMap<>();
    }

    @Override
    public void setInfo(String name, Calendar date, VoteType type, int quantity) {
        this.name = name;
        this.date = (Calendar) date.clone();
        this.voteType = type;
        this.quantity = quantity;

    }

    @Override
    public void addVoters(Map<Voter, Double> voters) {
        this.voters.putAll(voters);
    }

    @Override
    public void addCandidates(List<C> candidates) {
        this.candidates.addAll(candidates);
    }

    @Override
    public void addVote(Vote<C> vote) {
        // 此处应首先检查该选票的合法性并标记，为此需扩展或修改rep
        this.votes.put(vote, checkVoteValidityStrategy.checkAddVoteValidity(
                new ArrayList<>(candidates), vote, voteType));

        assert checkRep();
    }

    @Override
    public void setCheckVoteValidityStrategy(CheckVoteValidityStrategy checkVoteValidityStrategy) {
        this.checkVoteValidityStrategy = checkVoteValidityStrategy;
    }

    @Override
    public void statistics(StatisticsStrategy ss) {
        // 此处应首先检查当前所拥有的选票的合法性
        Map<Vote<C>, Boolean> tmp;

        try {
            tmp = checkVoteValidityStrategy.checkVoteValidity(voters, votes);
        } catch (VoteInvalidException e) {
            System.out.println(e.toString());
            return;
        }

        votes.clear();
        tmp.forEach((k, v) -> {
            if (v) votes.put(k, true);
        });

        this.statistics = ss.getVoteStatistics(voteType, voters, candidates, votes.keySet());

    }

    @Override
    public void selection(SelectionStrategy ss) {
        this.results = ss.getSelectionResult(statistics);
    }

    @Override
    public String result() {
        List<Map.Entry<C, Double>> list = new ArrayList<>(results.entrySet());
        list.sort((o1, o2) -> (int) Math.round(o1.getValue() - o2.getValue()));
        return list.stream()
                .map(entry -> entry.getKey() + ", " + (entry.getValue()).intValue())
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String toString() {
        return String.format("name: %s, candidates: (%s)",
                this.name, candidates.stream().map(Object::toString).collect(Collectors.joining(", ")));
    }
}
