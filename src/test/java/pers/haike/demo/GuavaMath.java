package pers.haike.demo;

import com.google.common.math.DoubleMath;
import com.google.common.math.IntMath;
import org.junit.Test;

import java.math.RoundingMode;

public class GuavaMath {

    @Test
    public void test() {
        System.out.println(IntMath.gcd(8, 16));
        System.out.println(DoubleMath.fuzzyEquals(1.0, 1.001, 0.01));
        System.out.println(DoubleMath.roundToInt(8.9, RoundingMode.CEILING));
    }
}
