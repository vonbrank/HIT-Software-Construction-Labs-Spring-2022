package app;

import auxiliary.Dish;
import auxiliary.Proposal;
import auxiliary.Voter;
import pattern.VoteValidityRateVisitor;
import poll.BusinessVoting;
import vote.RealNameVote;
import vote.Vote;
import vote.VoteItem;
import vote.VoteType;

import java.util.*;

public class BusinessVotingApp {

	public static void main(String[] args) throws Exception {

		// 创建投票人
		Voter voter1 = new Voter("董事1");
		Voter voter2 = new Voter("董事2");
		Voter voter3 = new Voter("董事3");
		Voter voter4 = new Voter("董事4");
		Voter voter5 = new Voter("董事5");

		// 设定投票人的权重
		Map<Voter, Double> weightedVoters = new HashMap<>();
		weightedVoters.put(voter1, 1.0);
		weightedVoters.put(voter2, 1.0);
		weightedVoters.put(voter3, 1.0);
		weightedVoters.put(voter4, 1.0);
		weightedVoters.put(voter5, 1.0);

		// 设定投票类型
		VoteType voteType = new VoteType("\"支持\"|\"不支持\"|\"弃权\"");

		// 创建候选对象：候选人
		Proposal proposal = new Proposal("提案1", Calendar.getInstance());

		// 创建选票
		Vote<Proposal> vote1 = new RealNameVote<>(
			Set.of(new VoteItem<>(proposal, "反对")), voter1
		);
		Vote<Proposal> vote2 = new RealNameVote<>(
			Set.of(new VoteItem<>(proposal, "支持")), voter2
		);
		Vote<Proposal> vote3 = new RealNameVote<>(
			Set.of(new VoteItem<>(proposal, "支持")), voter3
		);
		Vote<Proposal> vote4 = new RealNameVote<>(
			Set.of(new VoteItem<>(proposal, "反对")), voter4
		);
		Vote<Proposal> vote5 = new RealNameVote<>(
			Set.of(new VoteItem<>(proposal, "弃权")), voter5
		);

		// 创建投票活动
		BusinessVoting poll = new BusinessVoting();
		// 设定投票基本信息：名称、日期、投票类型、选出的数量
		poll.setInfo("商业表决", Calendar.getInstance(), voteType, 1);
		// 增加候选人
		poll.addCandidates(List.of(proposal));
		// 增加投票人及其权重
		poll.addVoters(weightedVoters);
		// 添加选票
		poll.addVote(vote1);
		poll.addVote(vote2);
		poll.addVote(vote3);
		poll.addVote(vote4);
		poll.addVote(vote5);
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
