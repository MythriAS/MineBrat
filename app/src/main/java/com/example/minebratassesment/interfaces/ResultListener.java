package com.example.minebratassesment.interfaces;

public interface ResultListener<T> {
    void onSuccess(T data);
    void onError(String message);
}
