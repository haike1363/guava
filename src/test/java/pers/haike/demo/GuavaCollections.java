package pers.haike.demo;

import com.google.common.collect.*;
import org.checkerframework.dataflow.qual.TerminatesExecution;
import org.junit.Test;
import sun.security.provider.certpath.Vertex;

import java.util.*;

public class GuavaCollections {
    public static final ImmutableSet<String> COLOR_NAMES = ImmutableSet.of(
            "red",
            "orange",
            "yellow",
            "green",
            "blue",
            "purple");

    public static final ImmutableSet<String> GOOGLE_COLORS =
            ImmutableSet.<String>builder()
                    .addAll(COLOR_NAMES)
                    .add(new String("123"))
                    .build();

    class Bar {
        int i;
    }

    class Foo {
        final ImmutableSet<Bar> bars;

        Foo(Set<Bar> bars) {
            this.bars = ImmutableSet.copyOf(bars); // defensive copy!
        }
    }

    @Test
    public void ImmutableCollections() {
        BiMap<Integer, String> biMap = HashBiMap.create();
        biMap.forcePut(1, "a");
        biMap.inverse().forcePut("a", 2);
        System.out.println(biMap.get(2));
        System.out.println(biMap.inverse().get("a"));
    }

    @Test
    public void testClassToInstanceMap() {
        class B {

        }
        class B1 extends B {
            int i;

            public B1(int i) {
                this.i = i;
            }
        }
        class B2 extends B {
            int i;

            public B2(int i) {
                this.i = i;
            }
        }
        ClassToInstanceMap<B> classToInstanceMap = MutableClassToInstanceMap.create();
        classToInstanceMap.put(B1.class, new B1(1));
        classToInstanceMap.put(B1.class, new B1(11));
        classToInstanceMap.put(B2.class, new B2(2));
        classToInstanceMap.putInstance(B2.class, new B2(22));
        B1 b1 = classToInstanceMap.getInstance(B1.class);
        System.out.println(b1.i);
        B2 b2 = classToInstanceMap.getInstance(B2.class);
        System.out.println(b2.i);
    }

    @Test
    public void testTable() {
        // 二维查找表
        Table<String, String, Double> tagkvPostList = HashBasedTable.create();
        tagkvPostList.put("1", "2", 4.0);
        tagkvPostList.put("1", "3", 20.0);
        tagkvPostList.put("1", "3", 5.0);

        tagkvPostList.row("1"); // returns a Map mapping v2 to 4, v3 to 20
        tagkvPostList.column("3"); // returns a Map mapping v1 to 20, v2 to 5
        tagkvPostList.get("1", "2");
    }

    enum MyEnum {
        INIT, RUN
    }

    @Test
    public void testMutiset() {
        Multiset<String> multiset = HashMultiset.create();
        multiset.add("1");
        multiset.add("1");

        System.out.println(multiset.count("1"));
        System.out.println(multiset.entrySet().size());

        ListMultimap<String, Integer> listMultimap = MultimapBuilder.treeKeys().arrayListValues().build();
        listMultimap.put("1", 1);
        listMultimap.put("2", 2);


        // creates a SetMultimap with hash keys and enum set values
        SetMultimap<Integer, MyEnum> hashEnumMultimap =
                MultimapBuilder.hashKeys().enumSetValues(MyEnum.class).build();

    }

    @Test
    public void testRangeSet() {
        {
            RangeSet<Integer> rangeSet = TreeRangeSet.create();
            rangeSet.add(Range.closed(1, 10)); // {[1, 10]}
            rangeSet.add(Range.closed(1, 10)); // {[1, 10]}
            rangeSet.add(Range.closedOpen(11, 15)); // disconnected range: {[1, 10], [11, 15)}
            rangeSet.add(Range.closedOpen(15, 20)); // connected range; {[1, 10], [11, 20)}
            rangeSet.add(Range.openClosed(0, 0)); // empty range; {[1, 10], [11, 20)}
            rangeSet.remove(Range.open(5, 10)); // splits [1, 10]; {[1, 5], [10, 10], [11, 20)}
            System.out.println(rangeSet.toString());
        }
        {
            RangeSet<String> rangeSet = TreeRangeSet.create();
            rangeSet.add(Range.closed("01", "11")); // {[1, 10]}
            rangeSet.remove(Range.closed("05", "07"));
            System.out.println(rangeSet.toString());
        }
    }

    @Test
    public void testNavigableSet() {
        NavigableSet<String> navigableSet = new TreeSet<String>(); // SortedSet接收TreeSet的实例
        // 增加元素
        navigableSet.add("aa");
        navigableSet.add("bb");
        navigableSet.add("cc");
        System.out.println(navigableSet.ceiling("bc"));
        System.out.println(navigableSet.floor("bc"));
        SortedSet<String> tailSet = navigableSet.tailSet("bb", true);
        for(String e : tailSet) {
            System.out.println(e);
        }
    }
}
