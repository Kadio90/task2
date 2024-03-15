package stepup;
import java.lang.reflect.Proxy;

public class Utils {

    // метод принимает объект обобщенного типа T и возвращает объект того же типа
    public static <T>T cache(T obj) {
        return (T) Proxy.newProxyInstance(
                obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                new CashingHandler(obj));
    }


}
