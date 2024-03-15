package stepup.testing;

import org.junit.jupiter.api.*;
import stepup.Fractionable;
import stepup.Utils;

// Тестируем путем создания копии тестируемого класса
public class AppCopyTest {
	FractionTest fraction;
	Fractionable num;
	@BeforeEach
	void beforeEach() {
		fraction = new FractionTest( (int)(Math.random() * 100), (int)(Math.random() * 100));
		System.out.println("Создана новая дробь: "+ fraction);
		num = Utils.cache(fraction);
		System.out.println("Создан proxy интерфейс: "+ num);
	}

	@AfterEach
	void afterEach(){
		num = null;
	}

	@Test
	@DisplayName("Проверяем однократный вызов метода с аннотацией Cache")
	public void testAnnotationMethodCached () throws NoSuchFieldException {
		num.doubleValue();
		System.out.println("Новый объект закэширован: "+ fraction.cashed);
		Assertions.assertTrue(fraction.cashed);
		System.out.println("Количество кеширований объекта: "+ fraction.cashCount);
		Assertions.assertEquals(fraction.cashCount, 1);
	}

	@Test
	@DisplayName("Проверяем что вызов метода с аннотацией Cache повторно не производиться")
	public void testAnnotationMethodOneCached () {
		num.doubleValue();
		num.doubleValue();
		System.out.println("Новый объект закэширован: "+ fraction.cashed);
		Assertions.assertTrue(fraction.cashed);
		System.out.println("Количество кеширований объекта: "+ fraction.cashCount);
		Assertions.assertEquals(fraction.cashCount, 1);
	}

	@Test
	@DisplayName("Проверяем что объект очищается после выполнения методов Mutator")
	public void testAnnotationMethodCachedClear ()   {
		num.doubleValue();
		num.doubleValue();
		num.setNum((int)(Math.random() * 100));
		System.out.println("Новый объект закэширован: "+ fraction.cashed);
		Assertions.assertFalse(fraction.cashed);
		System.out.println("Количество кеширований объекта: "+ fraction.cashCount);
		Assertions.assertEquals(fraction.cashCount, 0);
	}

	@Test
	@DisplayName("Проверяем что объект повторно кэшируется после Mutator")
	public void testAnnotationMethodCachedNoClear ()   {
		num.doubleValue();
		num.doubleValue();
		num.setNum((int)(Math.random() * 100));
		num.setDenum((int)(Math.random() * 100));
		System.out.println("Дробь после setNum/setDenum:" + num.toString());
		num.doubleValue();
		System.out.println("Новый объект закэширован: "+ fraction.cashed);
		Assertions.assertTrue(fraction.cashed);
		System.out.println("Количество кеширований объекта: "+ fraction.cashCount);
		Assertions.assertEquals(fraction.cashCount, 1);
	}
	
}
