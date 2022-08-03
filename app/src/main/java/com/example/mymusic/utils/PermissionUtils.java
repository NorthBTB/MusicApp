package com.example.mymusic.utils;

import android.Manifest;
import android.app.Activity;

import com.example.mymusic.utils.BasePermission.CallbackPermissionListener;
import com.example.mymusic.utils.BasePermission.PermissionBuilder;

public class PermissionUtils {

    public static void checkExternalStoragePermission(Activity activity, CallbackPermissionListener listener) {
        BasePermission.checkPermission(
                new PermissionBuilder(
                        activity,
                        listener,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .setReason("We need external storage permission to read/write your file.")
                        .setRejectedMessage("We can\'t read/write external storage without permission."));
    }
}









