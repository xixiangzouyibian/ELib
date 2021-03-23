package com.martin.ELib.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class Stack<T> {
    Object[] stack;
    int tail;
    int cap;

    Stack(int cap) {
        // init
        this.stack = new Object[cap];
        this.cap = cap;
        this.tail = -1;
    }

    public void push(T input) {
        synchronized (stack) {
            if (tail + 1 == cap) {
                grow();
            }
            stack[++tail] = input;
        }
    }

    public T peek() throws Exception {
        if (tail == -1) return null;
        return (T) stack[tail];
    }

    public T pop() throws Exception {
        T res = null;
        synchronized (stack) {
            if (tail == -1) return null;
            res = (T) stack[tail];
            tail--;
        }
        return res;
    }

    private void grow() {
        cap = 2 * cap;
        Object[] growStack = new Object[cap];
        System.arraycopy(stack, 0, growStack, 0, stack.length);
        stack = growStack;
    }

    public static void main(String[] args) throws Exception {
        Stack<Integer> myStack = new Stack<>(5);

        ExecutorService executor = Executors.newFixedThreadPool(10);
        IntStream.range(0, 100).boxed().forEach(i -> {
            Future<?> future = executor.submit(() -> {
                myStack.push(i);
            });
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        for (int i = 0; i < 100; i++) {
            System.out.println(myStack.pop());
        }
    }
}
