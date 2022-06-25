package vote;

import exception.StringFormatException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//immutable
public class VoteType {

    // key为选项名、value为选项名对应的分数
    private Map<String, Integer> options = new HashMap<>();

    // Rep Invariants
    //   每个选项名字都不相同
    //   至少有一个选项
    // Abstract Function
    //   AF(options) = 一类投票选项，对于任意 (k, v) 属于 options ，表示存在名为 k 选项，其分数为 v
    // Safety from Rep Exposure
    //   VoteType 本身是 immutable 的，没有 mutator ，保证初始化后不会被修改
    //   构造时传入的 Map 在赋值给 options 前会拷贝一份

    private boolean checkRep() {
        return options.size() >= 1;
    }

    /**
     * 创建一个投票类型对象
     * <p>
     * 可以自行设计该方法所采用的参数
     *
     * @param options 遵循 options 的数据类型，传入的作为投票选项信息的 Map
     */
    public VoteType(Map<String, Integer> options) {
        this.options = new HashMap<>(options);
        assert checkRep();
    }

    /**
     * 根据满足特定语法规则的字符串，创建一个投票类型对象
     *
     * @param option 遵循特定语法的、包含投票类型信息的字符串（待任务12再考虑）
     */
    public VoteType(String option) throws StringFormatException {
        String errorMsg = "选项格式错误，参考格式：\"喜欢\"(2)|\"不喜欢\"(0)|\"无所谓\"(1)。";
        String regexWithScore = "\"[^\"]+\"\\(-?\\d\\)(\\|\"[^\"]+\"\\(-?\\d\\))*";
        String regexWithoutScore = "\"[^\"]+\"(\\|\"[^\"]+\")*";
        boolean withScore;
        if (option.matches(regexWithScore))
            withScore = true;
        else if (option.matches(regexWithoutScore)) {
            withScore = false;
        } else throw new StringFormatException(errorMsg);
        List<String> optionsList = List.of(option.split("\\|"));
        Pattern optionNamePattern = Pattern.compile("\"[^\"]{1,5}\"");
        Pattern optionScorePattern = Pattern.compile("\\(-?\\d\\)");
        for (var optionStr : optionsList) {
            Matcher optionNameMatcher = optionNamePattern.matcher(optionStr);
            Matcher optionScoreMatcher = optionScorePattern.matcher(optionStr);
            if (!(optionNameMatcher.find()))
                throw new StringFormatException(errorMsg);
            String optionNameStr = optionStr.substring(optionNameMatcher.start(), optionNameMatcher.end());
            optionNameStr = optionNameStr.substring(1, optionNameStr.length() - 1);
            if (withScore) {
                if (!optionScoreMatcher.find()) throw new StringFormatException(errorMsg);
                String optionScoreStr = optionStr.substring(optionScoreMatcher.start(), optionScoreMatcher.end());
                optionScoreStr = optionScoreStr.substring(1, optionScoreStr.length() - 1);
                try {
                    options.put(optionNameStr, Integer.parseInt(optionScoreStr));
                } catch (NumberFormatException e) {
                    throw new StringFormatException(errorMsg);
                }
            } else {
                options.put(optionNameStr, 1);
            }
        }
    }

    /**
     * 判断一个投票选项是否合法（用于Poll中对选票的合法性检查）
     * <p>
     * 例如，投票人给出的投票选项是“Strongly reject”，但options中不包含这个选项，因此它是非法的
     * <p>
     * 不能改动该方法的参数
     *
     * @param option 一个投票选项
     * @return 合法则true，否则false
     */
    public boolean checkLegality(String option) {
        return options.get(option) != null;
    }

    /**
     * 根据一个投票选项，得到其对应的分数
     * <p>
     * 例如，投票人给出的投票选项是“支持”，查询得到其对应的分数是1
     * <p>
     * 不能改动该方法的参数
     *
     * @param option 一个投票项取值
     * @return 该选项所对应的分数
     */
    public int getScoreByOption(String option) {
        return options.get(option);
    }

    @Override
    public int hashCode() {
        return Objects.hash(options);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof VoteType)) return false;
        VoteType otherVoteType = (VoteType) obj;
        return this.options.equals(otherVoteType.options);
    }

    @Override
    public String toString() {
        return options.toString();
    }
}

