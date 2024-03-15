package stepup.testing;

import org.junit.jupiter.api.*;
import stepup.Fraction;
import stepup.Fractionable;
import stepup.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

// Тестируем через перехват вывода
public class AppTest {
    Fraction fraction;
    Fractionable num;
    ByteArrayOutputStream arrayOutStream;
    PrintStream defaultPrintStream = System.out;
    PrintStream printStream;

    @BeforeEach
    void beforeEach() {
        fraction = new Fraction((int)(Math.random() * 100), (int)(Math.random() * 100));
        System.out.println("Создана новая дробь: "+ fraction);
        num = Utils.cache(fraction);
        System.out.println("Создан proxy интерфейс: "+ num);
        // Переопределяем вывод
        arrayOutStream = new ByteArrayOutputStream();
        printStream = new PrintStream(arrayOutStream);
    }

    @AfterEach
    void afterEach(){
        num = null;
    }

    // Количество вхождений подстроки в строку
    public static int count(String str, String target) {
        return (str.length() - str.replace(target, "").length()) / target.length();
    }

    @Test
    @DisplayName("Проверяем однократный вызов метода с аннотацией Cache")
    public void testAnnotationMethodCached () throws NoSuchFieldException {
        System.setOut(printStream);
        num.doubleValue();
        System.setOut(defaultPrintStream);
        String result = arrayOutStream.toString();
        System.out.println("Результат вызова proxy: '"+ result + "'");
        Assertions.assertEquals(count(result, "invoke double value"), 1);
    }

    @Test
    @DisplayName("Проверяем что вызов метода с аннотацией Cache повторно не производиться")
    public void testAnnotationMethodOneCached () {
        System.setOut(printStream);
        num.doubleValue();
        num.doubleValue();
        System.setOut(defaultPrintStream);
        String result = arrayOutStream.toString();
        System.out.println("Результат вызова proxy: '"+ result + "'");
        Assertions.assertEquals(count(result, "invoke double value"), 1);
    }

    @Test
    @DisplayName("Проверяем что объект очищается после выполнения методов Mutator")
    public void testAnnotationMethodCachedClear ()   {
        System.setOut(printStream);
        num.doubleValue();
        num.doubleValue();
        num.setNum((int)(Math.random() * 100));
        System.setOut(defaultPrintStream);
        String result = arrayOutStream.toString();
        System.out.println("Результат вызова proxy: '"+ result + "'");
        Assertions.assertEquals(count(result, "invoke double value"), 1);
        Assertions.assertEquals(count(result, "invoke setNum"), 1);
    }

    @Test
    @DisplayName("Проверяем что объект повторно кэшируется после Mutator")
    public void testAnnotationMethodCachedNoClear ()   {
        System.setOut(printStream);
        num.doubleValue();
        num.doubleValue();
        num.setNum((int)(Math.random() * 100));
        num.setDenum((int)(Math.random() * 100));
        num.doubleValue();
        System.setOut(defaultPrintStream);
        String result = arrayOutStream.toString();
        System.out.println("Результат вызова proxy: '"+ result + "'");
        Assertions.assertEquals(count(result, "invoke setNum"), 1);
        Assertions.assertEquals(count(result, "invoke setDenum"), 1);
        Assertions.assertEquals(count(result, "invoke double value"), 2);
    }
}
