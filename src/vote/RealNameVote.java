package vote;

import auxiliary.Voter;

//immutable
public class RealNameVote<C> extends Vote<C>{
	
	//投票人
	private Voter voter;
	
	public RealNameVote(/*TODO*/) {
		// TODO
	}
	
	public Voter getVoter() {
		return this.voter;
	}
}
