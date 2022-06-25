package app;

import auxiliary.Proposal;
import auxiliary.Voter;
import exception.StringFormatException;
import pattern.VoteValidityRateVisitor;
import poll.MultipleBusinessVoting;
import vote.RealNameVote;
import vote.Vote;
import vote.VoteItem;
import vote.VoteType;

import java.util.*;

public class MultipleBusinessVotingApp {
    public static void main(String[] args) throws StringFormatException {

        // 创建投票人
        Voter voter1 = new Voter("董事1");
        Voter voter2 = new Voter("董事2");
        Voter voter3 = new Voter("董事3");
        Voter voter4 = new Voter("董事4");
        Voter voter5 = new Voter("董事5");

        // 设定投票人的权重
        Map<Voter, Double> weightedVoters = new HashMap<>();
        weightedVoters.put(voter1, 5.0);
        weightedVoters.put(voter2, 51.0);
        weightedVoters.put(voter3, 10.0);
        weightedVoters.put(voter4, 24.0);
        weightedVoters.put(voter5, 20.0);

        // 设定投票类型
        VoteType voteType = new VoteType("\"支持\"|\"不支持\"|\"弃权\"");

        // 创建候选对象：候选人
        Proposal proposalA = new Proposal("提案A", Calendar.getInstance());
        Proposal proposalB = new Proposal("提案B", Calendar.getInstance());

        // 创建选票
        Vote<Proposal> vote1A = new RealNameVote<>(
                Set.of(
                        new VoteItem<>(proposalA, "反对"),
                        new VoteItem<>(proposalB, "反对")
                        ),
                voter1
        );
        Vote<Proposal> vote2A = new RealNameVote<>(
                Set.of(
                        new VoteItem<>(proposalA, "支持"),
                        new VoteItem<>(proposalB, "支持")
                        ),
                voter2
        );
        Vote<Proposal> vote3A = new RealNameVote<>(
                Set.of(
                        new VoteItem<>(proposalA, "支持"),
                        new VoteItem<>(proposalB, "支持")
                        ),
                voter3
        );
        Vote<Proposal> vote4A = new RealNameVote<>(
                Set.of(
                        new VoteItem<>(proposalA, "反对"),
                        new VoteItem<>(proposalB, "反对")
                        ),
                voter4
        );
        Vote<Proposal> vote5A = new RealNameVote<>(
                Set.of(
                        new VoteItem<>(proposalA, "弃权"),
                        new VoteItem<>(proposalB, "支持")
                        ),
                voter5
        );

        // 创建投票活动
        MultipleBusinessVoting poll = new MultipleBusinessVoting();
        // 设定投票基本信息：名称、日期、投票类型、选出的数量
        poll.setInfo("商业表决", Calendar.getInstance(), voteType, 2);
        // 增加候选人
        poll.addCandidates(List.of(proposalA, proposalB));
        // 增加投票人及其权重
        poll.addVoters(weightedVoters);
        // 添加选票
        poll.addVote(vote1A);
        poll.addVote(vote2A);
        poll.addVote(vote3A);
        poll.addVote(vote4A);
        poll.addVote(vote5A);

        // 按规则计票
        poll.statistics();
        // 输出遴选结果
        poll.selection();

        VoteValidityRateVisitor<Proposal> voteValidityRateVisitor = new VoteValidityRateVisitor<>();
        poll.accept(voteValidityRateVisitor);

        System.out.println(poll);
        System.out.printf("选票有效率为：%s\n", voteValidityRateVisitor.getVoteValidityRate());
        System.out.println("结果：");
        System.out.println(poll.result());
    }
}
