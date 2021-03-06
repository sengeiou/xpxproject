package com.intersky.strang.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.intersky.strang.android.asks.LoginAsks;
import com.intersky.strang.android.prase.LoginPrase;
import com.intersky.strang.android.view.activity.ScanLoginActivity;

import intersky.xpxnet.net.NetObject;

public class ScanLoginHandler extends Handler {
    ScanLoginActivity theActivity;

    public ScanLoginHandler(ScanLoginActivity mScanLoginActivity) {
        theActivity = mScanLoginActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case LoginAsks.SAFE_LOGIN_SUCCESS:
                theActivity.waitDialog.hide();
                if(LoginPrase.praseDate(theActivity, (NetObject) msg.obj))
                theActivity.mScanLoginPresenter.doFinish();
                break;
        }

    }
}
