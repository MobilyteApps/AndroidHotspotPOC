package com.p.hotspotpoc;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Vishnu on 29-08-2017.
 */

public class DeviceAdminSample extends DeviceAdminReceiver {

    void showToast(Context context, String msg) {
        //  String status = context.getString(R.string.admin_receiver_status, msg);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        showToast(context, "admin_receiver_status_enabled");
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "admin_receiver_status_disable_warning";
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context, "admin_receiver_status_disabled");
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        showToast(context, "admin_receiver_status_pw_changed");
    }
}
