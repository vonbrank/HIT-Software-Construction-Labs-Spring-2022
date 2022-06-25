package app;

import java.util.*;

import auxiliary.Person;
import auxiliary.Voter;
import pattern.ElectionCheckVoteValidityStrategy;
import pattern.ElectionSelectionStrategy;
import pattern.ElectionStatisticStrategy;
import pattern.VoteValidityRateVisitor;
import poll.Election;
import poll.Poll;
import vote.RealNameVote;
import vote.Vote;
import vote.VoteItem;
import vote.VoteType;

public class ElectionApp {

	public static void main(String[] args) {

		// 创建2个投票人
		Voter vr1 = new Voter("v1");
		Voter vr2 = new Voter("v2");

		// 设定2个投票人的权重
		Map<Voter, Double> weightedVoters = new HashMap<>();
		weightedVoters.put(vr1, 1.0);
		weightedVoters.put(vr2, 1.0);

		// 设定投票类型
		Map<String, Integer> types = new HashMap<>();
		types.put("Support", 1);
		types.put("Oppose", -1);
		types.put("Waive", 0);
		VoteType vt = new VoteType(types);

		// 创建候选对象：候选人
		Person p1 = new Person("ABC", 19);
		Person p2 = new Person("DEF", 20);
		Person p3 = new Person("GHI", 21);

		// 创建投票项，前三个是投票人vr1对三个候选对象的投票项，后三个是vr2的投票项
		VoteItem<Person> vi11 = new VoteItem<>(p1, "Support");
		VoteItem<Person> vi12 = new VoteItem<>(p2, "Oppose");
		VoteItem<Person> vi13 = new VoteItem<>(p3, "Support");
		VoteItem<Person> vi21 = new VoteItem<>(p1, "Oppose");
		VoteItem<Person> vi22 = new VoteItem<>(p2, "Waive");
		VoteItem<Person> vi23 = new VoteItem<>(p3, "Waive");

		// 创建2个投票人vr1、vr2的选票
		Vote<Person> rv1 = new Vote<Person>(Set.of(vi11, vi12, vi13));
		Vote<Person> rv2 = new Vote<Person>(Set.of(vi21, vi22, vi23));

		// 创建投票活动
		Poll<Person> poll = new Election();
		// 设定投票基本信息：名称、日期、投票类型、选出的数量
		poll.setInfo("选举", Calendar.getInstance(), vt, 2);
		// 增加候选人
		poll.addCandidates(List.of(p1, p2, p3));
		// 增加投票人及其权重
		poll.addVoters(weightedVoters);

		poll.setCheckVoteValidityStrategy(new ElectionCheckVoteValidityStrategy(2));

		// 增加三个投票人的选票
		poll.addVote(rv1);
		poll.addVote(rv2);

		// 按规则计票
		poll.statistics(new ElectionStatisticStrategy());
		// 按规则遴选
		poll.selection(new ElectionSelectionStrategy(2));
		// 输出遴选结果
		VoteValidityRateVisitor<Person> voteValidityRateVisitor = new VoteValidityRateVisitor<>();
		poll.accept(voteValidityRateVisitor);
		System.out.printf("选票有效率为：%s\n", voteValidityRateVisitor.getVoteValidityRate());
		System.out.println(poll.result());
	}

}
