package com.carusselgroup.dwt.rest;

public interface IResponse {
    public void onSuccess(Object response);
    public void onFailure(Object response);
}
