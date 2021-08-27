package my_spring.robot_impl;

import my_spring.annotations.inject.InjectByType;

/**
 * @author Evgeny Borisov
 */
public class BenchmarkCleanerImplWrapper implements Cleaner { //or extends CleanerImpl

    @InjectByType
    private CleanerImpl cleaner;

    @Override
    public void clean() {
        long start = System.nanoTime();
        cleaner.clean();
        long end = System.nanoTime();
        System.out.println(end-start);
    }
}
