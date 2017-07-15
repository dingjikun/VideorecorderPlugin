package com.ding.plugins;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.ding.videorecorderlib.VideoRecorderMainActivity;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class echoes a string called from JavaScript.
 */
public class videorecorder extends CordovaPlugin {

    public static final int REQUEST_CODE = 0x0214c05e;

    private static final String LOG_TAG = "videorecorder";


    private String [] permissions = { Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE };

    private CallbackContext callbackContext;
    private JSONArray args;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        this.callbackContext = callbackContext;
        this.args = args;

        if (action.equals("recordVideo")) {
            if(!hasPermisssion()) {
                requestPermissions(0);
            } else {
                record(args);
            }
            return true;
        }
        return false;
    }

    private void record(JSONArray args) throws JSONException {
        Intent intent = new Intent(cordova.getActivity().getBaseContext(), VideoRecorderMainActivity.class);
        //args[0] userid      args[1] longitude    args[2] latitude
        //有可能null！！！

        if (!args.isNull(0))
        {
            intent.putExtra("userid", args.getString(0));
        }
        else
        {
            intent.putExtra("userid", "");
        }

        if (!args.isNull(1))
        {
            intent.putExtra("jd", args.getString(1));
        }
        else
        {
            intent.putExtra("jd", "");
        }

        if (!args.isNull(2))
        {
            intent.putExtra("wd", args.getString(2));
        }
        else
        {
            intent.putExtra("wd", "");
        }


        cordova.startActivityForResult(this, intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE && this.callbackContext != null) {
            if (resultCode == Activity.RESULT_OK) {
                this.callbackContext.success("sucess");
            } else if (resultCode == Activity.RESULT_CANCELED) {
                //this.success(new PluginResult(PluginResult.Status.OK, obj), this.callback);
                this.callbackContext.success("cancel");
            } else {
                //this.error(new PluginResult(PluginResult.Status.ERROR), this.callback);
                this.callbackContext.error("Unexpected error");
            }
        }
    }

    public boolean hasPermisssion() {
        for(String p : permissions)
        {
            if(!PermissionHelper.hasPermission(this, p))
            {
                return false;
            }
        }
        return true;
    }

    public void requestPermissions(int requestCode)
    {
        PermissionHelper.requestPermissions(this, requestCode, permissions);
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions,
                                          int[] grantResults) throws JSONException
    {
        PluginResult result;
        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                Log.d(LOG_TAG, "Permission Denied!");
                result = new PluginResult(PluginResult.Status.ILLEGAL_ACCESS_EXCEPTION);
                this.callbackContext.sendPluginResult(result);
                return;
            }
        }

        switch(requestCode)
        {
            case 0:
                record(args);
                break;
        }
    }

    public void onRestoreStateForActivityResult(Bundle state, CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }
}
