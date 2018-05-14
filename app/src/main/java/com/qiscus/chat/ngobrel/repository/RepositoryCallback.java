package com.qiscus.chat.ngobrel.repository;

/**
 * Created by omayib on 22/09/17.
 */
public interface RepositoryCallback<T> {
    void onSucceed(T value);

    void onFailed();
}
