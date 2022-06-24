package app;

import auxiliary.Person;
import auxiliary.Voter;
import vote.*;

import java.util.Set;

public class VoteApp {
    public static void main(String[] args) {

        Voter voter1 = new Voter("Voter-01");
        Voter voter2 = new Voter("Voter-02");

        Person personAlice = new Person("Alice", 19);
        Person personBob = new Person("Bob", 19);
        Person personCathy = new Person("Cathy", 20);
        Voteable<Person> vote = new Vote<>(Set.of(
                new VoteItem<>(personAlice, "支持"),
                new VoteItem<>(personBob, "不支持"),
                new VoteItem<>(personCathy, "弃权")
        ));
        Vote<Person> realNameVote = new RealNameVote<>(Set.of(
                new VoteItem<>(personAlice, "支持"),
                new VoteItem<>(personBob, "不支持"),
                new VoteItem<>(personCathy, "弃权")
        ), voter1);
        System.out.println(((RealNameVote<?>)realNameVote).getVoter());

        Voteable<Person> realNameVoteDecorator = new RealNameVoteDecorator<>(vote, voter2);
        System.out.println(((RealNameVoteDecorator<?>)realNameVoteDecorator).getVoter());

    }
}
