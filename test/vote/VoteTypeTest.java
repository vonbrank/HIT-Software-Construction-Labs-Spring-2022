package vote;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class VoteTypeTest {

	// test strategy
	// TODO

	@Test
	void test() {
		Map<String, Integer> options = new HashMap<>();
		options.put("喜欢", 2);
		options.put("不喜欢", 0);
		options.put("无所谓", 1);
		VoteType voteType = new VoteType(options);

		assertTrue(voteType.checkLegality("喜欢"));
		assertTrue(voteType.checkLegality("无所谓"));
		assertFalse(voteType.checkLegality("非常喜欢！(❤ ω ❤)"));

		assertEquals(2, voteType.getScoreByOption("喜欢"));
		assertEquals(0, voteType.getScoreByOption("不喜欢"));
		assertEquals(1, voteType.getScoreByOption("无所谓"));

		Map<String, Integer> otherOptions = new HashMap<>();
		otherOptions.put("喜欢", 2);
		otherOptions.put("无所谓", 1);
		VoteType otherVoteType = new VoteType(otherOptions);
		assertNotEquals(voteType, otherVoteType);
		assertNotEquals(voteType.hashCode(), otherVoteType.hashCode());
		otherOptions.put("不喜欢", 0);
		otherVoteType = new VoteType(otherOptions);
		assertEquals(voteType, otherVoteType);
		assertEquals(voteType.hashCode(), otherVoteType.hashCode());

	}

}
