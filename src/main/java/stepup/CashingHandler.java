package stepup;

import stepup.annotations.Cache;
import stepup.annotations.Mutator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CashingHandler<T> implements InvocationHandler {
    private T obj;
    private Map<Method, Object> cashed = new HashMap<>();
    public CashingHandler(T obj){this.obj = obj;}
    // Вызываемый метод принадлежит интерфейсам класса
    private boolean checkClassMethodInInterface(Class<?> cls, Method method){
        for (Class<?> anInterface : cls.getInterfaces()) {
            for (Method declaredMethod : anInterface.getDeclaredMethods()) {
                if (method.getName().equals(declaredMethod.getName()) &&
                        Arrays.equals(method.getParameterTypes(), declaredMethod.getParameterTypes())) {
                   return true;
                }
            }
        }
        return false;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //System.out.println("It works");

        Method objMethod = obj.getClass().getMethod(method.getName(), method.getParameterTypes());
        // Проверяем принадлежность метода какому-либо интерфейсу
        if (checkClassMethodInInterface(obj.getClass(), objMethod)) {
            // Обработка аннотации Mutator
            if (objMethod.isAnnotationPresent(Mutator.class)) {
                cashed.clear();
            }
            // Обработка аннотации Cache
            if (objMethod.isAnnotationPresent(Cache.class)) {
                if (cashed.containsKey(objMethod)) {
                    return cashed.get(objMethod);
                }
                cashed.put(objMethod, method.invoke(obj, args));
                return cashed.get(objMethod);
            }
        }
        return method.invoke(obj, args);
    }
}
