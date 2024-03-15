package stepup.testing;

import stepup.Fraction;
import stepup.Fractionable;
import stepup.annotations.Cache;
import stepup.annotations.Mutator;

public class FractionTest implements Fractionable {
    public boolean cashed;
    public int cashCount;

    private int num;
    private int denum;

    public FractionTest(int num, int denum) {
        this.num = num;
        this.denum = denum;
        cashed = false;
        cashCount = 0;
    }

    @Mutator
    public void setNum(int num) {
        cashed = false;
        cashCount = 0;
        this.num = num;
    }

    @Mutator
    public void setDenum(int denum) {
        cashed = false;
        cashCount = 0;
        this.denum = denum;
    }

    @Override
    @Cache
    public double doubleValue() {
        cashed = true;
        cashCount = cashCount + 1;
        return (double) num/denum;
    }

    @Override
    public String toString() {
        return num + "/" + denum;
    }
}
