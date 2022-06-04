package vote;

import static org.junit.jupiter.api.Assertions.*;

import auxiliary.Dish;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class VoteTest {

	// test strategy
	// TODO

	@Test
	void test() {

		Set<VoteItem<Dish>> dishSet = new HashSet<>(Set.of(
				new VoteItem<>(new Dish("A", 10), "喜欢"),
				new VoteItem<>(new Dish("B", 15), "不喜欢")
		));

		Vote<Dish> vote = new Vote<Dish>(dishSet);

		assertEquals(dishSet, vote.getVoteItems());

		assertTrue(vote.candidateIncluded(new Dish("A", 10)));

		Vote<Dish> otherVote = new Vote<Dish>(dishSet);

		assertEquals(vote, otherVote);
		assertEquals(otherVote.hashCode(), vote.hashCode());

		dishSet.remove(new VoteItem<Dish>(new Dish("A", 10), "喜欢"));
		dishSet.add(new VoteItem<Dish>(new Dish("A", 11), "喜欢"));
		otherVote = new Vote<Dish>(dishSet);

		assertNotEquals(otherVote, vote);
		assertNotEquals(otherVote.hashCode(), vote.hashCode());

	}

}