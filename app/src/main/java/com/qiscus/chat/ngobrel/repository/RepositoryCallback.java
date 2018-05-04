package com.qiscus.chat.ngobrel.repository;

/**
 * Created by omayib on 22/09/17.
 */

public interface RepositoryCallback<T> {
    public void onSucceed(T value);
    public void onFailed();
}
