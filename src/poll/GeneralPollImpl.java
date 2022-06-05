package poll;

import java.util.*;

import auxiliary.Voter;
import pattern.SelectionStrategy;
import pattern.StatisticsStrategy;
import vote.VoteItem;
import vote.VoteType;
import vote.Vote;

public class GeneralPollImpl<C> implements Poll<C> {

	// 投票活动的名称
	private String name;
	// 投票活动发起的时间
	private Calendar date;
	// 候选对象集合
	private List<C> candidates;
	// 投票人集合，key为投票人，value为其在本次投票中所占权重
	private Map<Voter, Double> voters;
	// 拟选出的候选对象最大数量
	private int quantity;
	// 本次投票拟采用的投票类型（合法选项及各自对应的分数）
	private VoteType voteType;
	// 所有选票集合
	protected Set<Vote<C>> votes;
	// 计票结果，key为候选对象，value为其得分
	protected Map<C, Double> statistics;
	// 遴选结果，key为候选对象，value为其排序位次
	private Map<C, Double> results;

	// Rep Invariants
	//   候选对象 candidates 数量 >= 1
	//   所有字段不为 null
	// Abstract Function
	//   AF(rep) = 一次投票，名称为 name , 日期为 date , 候选人集合为 candidates ,
	//   投票人及其权重集合为 votes ,
	//   拟选出选票最大数量为 quantity ,
	//   合法选项及其分数、所有选票集合、机票结果和遴选结果分别为
	//   voteType, votes, statistics, results
	// Safety from Rep Exposure
	//   由方法参数传入的 mutable 对象将通过防御式拷贝赋值给 rep
	//   ADT 本身是 mutable 的，但是各 mutable 的 rep 不会作为某个方法的返回值。

	private boolean checkRep() {
		boolean res = true;
		res = name != null && date != null && candidates != null && voters != null
				&& statistics != null && results != null;
		if(!res) return false;
		res = candidates.size() >= 1;
		return res;
	}

	/**
	 * 构造函数
	 */
	public GeneralPollImpl() {

	}

	@Override
	public void setInfo(String name, Calendar date, VoteType type, int quantity) {
		this.name = name;
		this.date = (Calendar) date.clone();
		this.voteType = type;
		this.quantity = quantity;
		checkRep();
	}

	@Override
	public void addVoters(Map<Voter, Double> voters) {
		this.voters = new HashMap<>(voters);
	}

	@Override
	public void addCandidates(List<C> candidates) {
		this.candidates = new ArrayList<>(candidates);
	}

	@Override
	public void addVote(Vote<C> vote) {
		// 此处应首先检查该选票的合法性并标记，为此需扩展或修改rep
		this.votes.add(vote);
	}

	@Override
	public void statistics(StatisticsStrategy ss) {
		// 此处应首先检查当前所拥有的选票的合法性
		// TODO
	}

	@Override
	public void selection(SelectionStrategy ss) {
		// TODO
	}

	@Override
	public String result() {
		// TODO
		return null;
	}
}
