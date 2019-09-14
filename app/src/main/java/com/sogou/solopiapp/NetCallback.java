package com.sogou.solopiapp;

public interface NetCallback<T> {

    void onSuccess(T t);

    void onFailed();

}
