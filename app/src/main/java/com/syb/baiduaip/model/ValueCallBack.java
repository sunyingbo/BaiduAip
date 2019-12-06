package com.syb.baiduaip.model;

public interface ValueCallBack<T> {

    void onSuccess(T t);

    void onFail(String message);

}
