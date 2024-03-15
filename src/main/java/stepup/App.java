package stepup;

public class App {

	public static void main(String[] args) {
		Fraction fr= new Fraction(2,3);
		Fractionable num = Utils.cache(fr);
		num.doubleValue();// sout сработал
		num.doubleValue();// sout молчит-
		num.doubleValue();// sout молчит
		num.setNum(5);
		num.doubleValue();// sout сработал
		num.doubleValue();// sout молчит);
	}
}
