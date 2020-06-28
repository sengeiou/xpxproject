package com.iccard.entity;

import android.util.Log;

import java.io.File;
import java.util.Vector;

public class Driver {
    public Driver(String name, String root) {
        mDriverName = name;
        mDeviceRoot = root;
    }
    private String mDriverName;
    private String mDeviceRoot;
    Vector<File> mDevices = null;
    public Vector<File> getDevices() {
        if (mDevices == null) {
            mDevices = new Vector<File>();
            File dev = new File("/dev");
            File[] files = dev.listFiles();
            int i;
            for (i=0; i<files.length; i++) {
                if (files[i].getAbsolutePath().startsWith(mDeviceRoot)) {
                    mDevices.add(files[i]);
                }
            }
        }
        return mDevices;
    }
    public String getName() {
        return mDriverName;
    }
}
