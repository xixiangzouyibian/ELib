package com.martin.eDocManager.service.util;

import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class TestObject {
    int num;
    TestObject() {
        num = new Random().nextInt();
    }

    public int getNum() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return num;
    }
}

class TestTask extends RecursiveTask<Map<TestObject, Integer>> {
    private List<TestObject> request;
    private static final int CAP = 10;

    TestTask(List<TestObject> request) {
        this.request = request;
    }

    @Override
    protected Map<TestObject, Integer> compute() {
        if (request.size() <= CAP) {
            return processing(request);
        } else {
            Collection<TestTask> testTasks = ForkJoinTask.invokeAll(createSubtasks());
            Map<TestObject, Integer> result = new HashMap<>();
            for (TestTask task : testTasks) {
                Map<TestObject, Integer> taskResult = task.join();
                result.putAll(taskResult);
            }
            return result;
        }
    }

    private Map<TestObject, Integer> processing(List<TestObject> request) {
        return request.stream().collect(Collectors.toMap(o -> o, TestObject::getNum));
    }

    private Collection<TestTask> createSubtasks() {
//        System.out.println("create sub tasks");
        List<TestTask> dividedTasks = new ArrayList<>();
        dividedTasks.add(new TestTask(request.subList(0, request.size() / 2)));
        dividedTasks.add(new TestTask(request.subList(request.size() / 2, request.size())));
        return dividedTasks;
    }
}

public class ForkJoinTest {

    List<TestObject> request;

    @Before
    public void init() {
        request = new LinkedList<>();
        IntStream.range(0, 1000).forEach(i -> request.add(new TestObject()));
        System.out.println("request size: " + request.size());
    }

    @Test
    public void test_forkJoinPool() {
        ForkJoinPool pool = null;
        try {
            pool = (ForkJoinPool) Executors.newWorkStealingPool(10);
            TestTask task = new TestTask(request);
            long start = System.currentTimeMillis();
            Map<TestObject, Integer> result = pool.invoke(task);
            System.out.println(result.size());
            System.out.println("cost: " + (System.currentTimeMillis() - start) + "ms");
        } finally {
            if (pool != null) {
                pool.shutdown();
            }
        }
    }

    @Test
    public void test_plain_foreach() {
        long start = System.currentTimeMillis();
        Map<TestObject, Integer> result = request.stream().collect(Collectors.toMap(o -> o, TestObject::getNum));
        System.out.println(result.size());
        System.out.println("cost: " + (System.currentTimeMillis() - start) + "ms");
    }

    @Test
    public void test_threadExecutorPool_fix() {
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        List<CompletableFuture<Map<TestObject, Integer>>> collect = request.stream().map(r -> CompletableFuture.supplyAsync(() -> {
            Map<TestObject, Integer> result = new HashMap<>();
            result.put(r, r.getNum());
            return result;
        }, pool)).collect(Collectors.toList());
        Map<TestObject, Integer> result = new HashMap<>();
        long start = System.currentTimeMillis();
        for (CompletableFuture<Map<TestObject, Integer>> future : collect) {
            Map<TestObject, Integer> res = future.join();
            result.putAll(res);
        }
        System.out.println(result.size());
        System.out.println("cost: " + (System.currentTimeMillis() - start) + "ms");
    }

}
