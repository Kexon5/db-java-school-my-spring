package my_spring.annotations.benchmark;

import lombok.SneakyThrows;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BenchmarkProxyAnnotationConfigurator implements ProxyAnnotationConfigurator {
    @Override
    public <T> T configure(T t) {
        Set<String> methodsWithAnnotation = getMethodNames(t);

        if (!methodsWithAnnotation.isEmpty()) {
            if (t.getClass().getInterfaces().length != 0) {
                return createProxyByInterface(t, methodsWithAnnotation);
            }

            return createProxyByClass(t, methodsWithAnnotation);
        }
        return t;
    }

    private <T> T createProxyByInterface(T t, Set<String> methodsWithAnnotation) {
        return (T) Proxy.newProxyInstance(t.getClass().getClassLoader(),
                t.getClass().getInterfaces(),
                (proxy, method, args) -> runBenchmark(t, method, args, methodsWithAnnotation));
    }

    private <T> T createProxyByClass(T t, Set<String> methodsWithAnnotation) {
        return (T) Enhancer.create(t.getClass(),
                (MethodInterceptor) (o, method, args, proxy) -> runBenchmark(t, method, args, methodsWithAnnotation));
    }

    @SneakyThrows
    private <T> T runBenchmark(T t, Method method, Object[] args, Set<String> methodsWithAnnotations) {
        if (methodsWithAnnotations.contains(method.getName())) {
            System.out.println("********BENCHMARK STARTED FOR METHOD " + method.getName() + " **********");
            long start = System.nanoTime();
            Object retVal = method.invoke(t, args);
            long end = System.nanoTime();
            System.out.println(end - start);

            System.out.println("********BENCHMARK ENDED FOR METHOD " + method.getName() + " **********");


            return (T) retVal;
        }
        return (T) method.invoke(t, args);
    }
    private <T> HashSet<String> getMethodNames(T t) {
        Set<String> setMethodsWithAnnotations = new HashSet<>();
        Class<T> type = (Class<T>) t.getClass();

        if (type.isAnnotationPresent(Benchmark.class)) {
            List<String> names = Arrays.stream(type.getDeclaredMethods()).map(Method::getName).collect(Collectors.toList());
            setMethodsWithAnnotations.addAll(names);
        } else {
            for (Method method: type.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Benchmark.class)) {
                    setMethodsWithAnnotations.add(method.getName());
                }
            }
        }

        return (HashSet<String>) setMethodsWithAnnotations;
    }
}
