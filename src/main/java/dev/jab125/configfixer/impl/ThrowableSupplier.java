package dev.jab125.configfixer.impl;

@FunctionalInterface
public interface ThrowableSupplier<T> {
    T get() throws Throwable;
}
