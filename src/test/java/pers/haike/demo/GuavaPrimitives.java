package pers.haike.demo;

import com.google.common.base.Charsets;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Iterables;
import com.google.common.collect.Range;
import com.google.common.hash.*;
import com.google.common.primitives.Ints;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class GuavaPrimitives {

    @Test
    public void testPrimitives() {
        List<Integer> integerList = Ints.asList(1, 2, 3);
        int min = Ints.min(Ints.toArray(integerList));

        List<Integer> integerList2 = Arrays.asList(1, 2, 3);

        List<Double> scores = Arrays.asList(3.0, 4.0, 1.0, 2.0);
        Iterable<Double> belowMedianScores = Iterables.filter(scores, Range.lessThan(1.5));

        for (Double e : belowMedianScores) {
            System.out.println(e);
        }
    }

    static class Person implements Funnel<Person> {
        int id;
        int birthYear;

        @Override
        public void funnel(Person person, PrimitiveSink into) {
            into.putInt(person.id)
                    .putInt(birthYear);
        }
    }

    @Test
    public void testHash() {
        HashFunction hf = Hashing.sha256();
        Person person = new Person();
        HashCode hc = hf.newHasher()
                .putLong(1)
                .putString("name", Charsets.UTF_8)
                .putObject(person, person)
                .hash();
        System.out.println(hc.toString());


        List<Person> friendsList = Arrays.asList(new Person(), new Person(), new Person());

        BloomFilter<Person> friends = BloomFilter.create(person, 500, 0.01);
        for (Person friend : friendsList) {
            friends.put(friend);
        }
        // much later
        if (friends.mightContain(new Person())) {
            // the probability that dude reached this place if he isn't a friend is 1%
            // we might, for example, start asynchronously loading things for dude while we do a more expensive exact check
        }

        // 一定不存在
        if (!friends.mightContain(new Person())) {

        }
    }
}
