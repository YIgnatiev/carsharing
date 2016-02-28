package com.morfitrun.services.record_service;

import android.os.Binder;

/**
 * Created by vasia on 24.03.2015.
 */
public class RecordServiceBinder extends Binder {

    private RecordService mService;

    public RecordServiceBinder(RecordService _mService) {
        mService = _mService;
    }

    public void setService(RecordService _service){
        mService = _service;
    }

    public RecordService getService(){
        return mService;
    }
}
