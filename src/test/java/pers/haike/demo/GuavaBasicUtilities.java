package pers.haike.demo;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;

import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;


public class GuavaBasicUtilities {

    static class E {
        int i;
        String s;
        public E(int i, String s) {
            this.i = i;
            this.s = s;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("i", i)
                    .add("s", s)
                    .toString();
        }

        int compareTo(E other) {
           return ComparisonChain.start()
                    .compare(this.i, other.i)
                    .compare(this.s,other.s)
                    .result();
        }
    }

    @Test
    public void BasicUtilities() {
        // 防止空指针
        Optional<Integer> possible = Optional.of(5);
        possible.isPresent(); // returns true
        possible.get(); // returns 5

        // 参数检查
        checkArgument(1 >= 0, "Argument was %s but expected nonnegative", 1);

        // equal比较，处理null引用，底层使用原生equal
        System.out.println(Objects.equals(new E(2, "a"), null));

        // hash，底层使用原生hashCode
        System.out.println(Objects.hash(new E(1, "a"), 1));
        System.out.println(Objects.hash(new E(1, "a"), 1));

        // toString
        System.out.println(new E(1, "a").toString());


        // compareTo
        System.out.println(new E(2, "b").compareTo(new E(1, "c")));
        System.out.println(new E(2, "b").compareTo(new E(2, "b")));
        System.out.println(new E(1, "b").compareTo(new E(1, "c")));
    }
}
