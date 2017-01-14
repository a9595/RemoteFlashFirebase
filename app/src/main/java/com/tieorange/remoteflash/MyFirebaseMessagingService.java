package com.tieorange.remoteflash;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by root on 1/14/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();
        data.get("body");
        Log.d(TAG, "onMessageReceived() called with: remoteMessage = [" + data.get("body") + "]");

//        enableTorch();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("torch", true);
        startActivity(intent);
    }

    private void enableTorch() {
        CameraTorch2 torch = new CameraTorch2(this);
        torch.init();
        if (torch.isOn())
            torch.toggle(false);
        else torch.toggle(true);
    }
}
