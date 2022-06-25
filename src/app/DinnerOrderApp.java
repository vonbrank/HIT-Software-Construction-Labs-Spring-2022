package app;

import auxiliary.Dish;
import auxiliary.Person;
import auxiliary.Voter;
import pattern.VoteValidityRateVisitor;
import poll.DinnerOrder;
import vote.*;

import java.util.*;

public class DinnerOrderApp {

    public static void main(String[] args) throws Exception {

        // 创建2个投票人
        Voter voterGrandpa = new Voter("爷爷");
        Voter voterDad = new Voter("爸爸");
        Voter voterMum = new Voter("妈妈");
        Voter voterSon = new Voter("儿子");

        // 设定2个投票人的权重
        Map<Voter, Double> weightedVoters = new HashMap<>();
        weightedVoters.put(voterGrandpa, 4.0);
        weightedVoters.put(voterDad, 1.0);
        weightedVoters.put(voterMum, 2.0);
        weightedVoters.put(voterSon, 2.0);

        // 设定投票类型
        VoteType voteType = new VoteType("\"喜欢\"(1)|\"不喜欢\"(0)|\"无所谓\"(1)");

        // 创建候选对象：候选人
        Dish dishA = new Dish("A", 10);
        Dish dishB = new Dish("B", 40);
        Dish dishC = new Dish("C", 20);
        Dish dishD = new Dish("D", 80);
        Dish dishE = new Dish("E", 50);
        Dish dishF = new Dish("F", 70);

        // 创建选票
        Vote<Dish> voteGrandpa = new RealNameVote<>(
                Set.of(
                        new VoteItem<>(dishA, "喜欢"),
                        new VoteItem<>(dishB, "喜欢"),
                        new VoteItem<>(dishC, "无所谓"),
                        new VoteItem<>(dishD, "无所谓"),
                        new VoteItem<>(dishE, "不喜欢"),
                        new VoteItem<>(dishF, "不喜欢")
                ), voterGrandpa
        );
        Vote<Dish> voteDad = new RealNameVote<>(
                Set.of(
                        new VoteItem<>(dishA, "无所谓"),
                        new VoteItem<>(dishB, "喜欢"),
                        new VoteItem<>(dishC, "喜欢"),
                        new VoteItem<>(dishD, "喜欢"),
                        new VoteItem<>(dishE, "不喜欢"),
                        new VoteItem<>(dishF, "喜欢")
                ), voterDad
        );
        Vote<Dish> voteMum = new RealNameVote<>(
                Set.of(
                        new VoteItem<>(dishA, "喜欢"),
                        new VoteItem<>(dishB, "不喜欢"),
                        new VoteItem<>(dishC, "不喜欢"),
                        new VoteItem<>(dishD, "不喜欢"),
                        new VoteItem<>(dishE, "喜欢"),
                        new VoteItem<>(dishF, "不喜欢")
                ), voterMum
        );
        Vote<Dish> voteSon = new RealNameVote<>(
                Set.of(
                        new VoteItem<>(dishA, "喜欢"),
                        new VoteItem<>(dishB, "无所谓"),
                        new VoteItem<>(dishC, "喜欢"),
                        new VoteItem<>(dishD, "喜欢"),
                        new VoteItem<>(dishE, "喜欢"),
                        new VoteItem<>(dishF, "不喜欢")
                ), voterSon
        );

        // 创建投票活动
        DinnerOrder poll = new DinnerOrder();

        // 设定投票基本信息：名称、日期、投票类型、选出的数量
        poll.setInfo("点菜", Calendar.getInstance(), voteType, 4);
        // 增加候选人
        poll.addCandidates(List.of(dishA, dishB, dishC, dishD, dishE, dishF));
        // 增加投票人及其权重
        poll.addVoters(weightedVoters);
        // 添加选票
        poll.addVote(voteGrandpa);
        poll.addVote(voteDad);
        poll.addVote(voteMum);
        poll.addVote(voteSon);
        // 按规则计票
        poll.statistics();
        // 输出遴选结果
        poll.selection();

        VoteValidityRateVisitor<Dish> voteValidityRateVisitor = new VoteValidityRateVisitor<>();
        poll.accept(voteValidityRateVisitor);

        System.out.println(poll);
        System.out.printf("选票有效率为：%s\n", voteValidityRateVisitor.getVoteValidityRate());
        System.out.println("结果：");
        System.out.println(poll.result());
    }
}
