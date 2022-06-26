package vote;

import static org.junit.jupiter.api.Assertions.*;

import exception.StringFormatException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class VoteTypeTest {

	/* Testing strategy
	 *
	 * 提供一组 VoteType 选项及其分数：(["喜欢", 2], ["不喜欢", 0], ["无所谓", 1])
	 * 测试 VoteType 的所有方法
	 *
	 */

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

	/*
	 *  Testing strategy
	 *
	 *  1. 英语，5 个字符以内   "Yes"(1)|"No"(-1)|"SoSo"(0)
	 *  2. 英语，超过 5 个字符  "Support"(1)|"Oppose"(-1)|"Waive"(0)
	 *  3. 汉语，5 个字符以内   "支持"(1)|"不支持"(-1)|"无所谓"(0)
	 *  4. 汉语，超过 5 个字符  "超级无敌支持"(1)|"不支持"(-1)|"无所谓"(0)
	 *  5. 英语，无分数        "Yes"|"No"|"SoSo"
	 *  6. 汉语，无分数        "支持"|"不支持"|"无所谓"
	 */

	@Test
	void testRegexConstructor() {
		try {
			VoteType voteType1 = new VoteType("\"Yes\"(1)|\"No\"(-1)|\"SoSo\"(0)");
			System.out.println(voteType1);

			assertThrows(StringFormatException.class, () -> {
				VoteType voteType2 = new VoteType("\"Support\"(1)|\"Oppose\"(-1)|\"Waive\"(0)");
			});

			VoteType voteType3 = new VoteType("\"支持\"(1)|\"不支持\"(-1)|\"无所谓\"(0)");
			System.out.println(voteType3);

			assertThrows(StringFormatException.class, () -> {
				VoteType voteType4 = new VoteType("\"超级无敌支持\"(1)|\"不支持\"(-1)|\"无所谓\"(0)");
			});

			VoteType voteType5 = new VoteType("\"Yes\"|\"No\"|\"SoSo\"");
			System.out.println(voteType5);

			VoteType voteType6 = new VoteType("\"支持\"|\"不支持\"|\"无所谓\"");
			System.out.println(voteType6);

		} catch (StringFormatException e) {
			e.printStackTrace();
		}

	}

}
