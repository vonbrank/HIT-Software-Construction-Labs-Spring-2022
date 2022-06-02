package poll;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import auxiliary.Voter;
import pattern.SelectionStrategy;
import pattern.StatisticsStrategy;
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
	protected Set<Vote> votes;
	// 计票结果，key为候选对象，value为其得分
	protected Map<C, Double> statistics;
	// 遴选结果，key为候选对象，value为其排序位次
	private Map<C, Double> results;

	// Rep Invariants
	// TODO
	// Abstract Function
	// TODO
	// Safety from Rep Exposure
	// TODO

	private boolean checkRep() {
		// TODO
		return false;
	}

	/**
	 * 构造函数
	 */
	public GeneralPollImpl() {
		// TODO
	}

	@Override
	public void setInfo(String name, Calendar date, VoteType type, int quantity) {
		// TODO
	}

	@Override
	public void addVoters(Map<Voter, Double> voters) {
		// TODO
	}

	@Override
	public void addCandidates(List<C> candidates) {
		// TODO
	}

	@Override
	public void addVote(Vote<C> vote) {
		// 此处应首先检查该选票的合法性并标记，为此需扩展或修改rep
		// TODO
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
