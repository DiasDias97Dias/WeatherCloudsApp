package com.example.google.weatherclouds;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FBSMessage extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("111", "111");
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Intent i = new Intent(this, MapsActivity.class);
            i.putExtra("msg", remoteMessage.getData().get("msg"));
            i.putExtra("x",remoteMessage.getData().get("x"));
            i.putExtra("y",remoteMessage.getData().get("y"));
           Log.e("MSG", remoteMessage.getData().toString());
            startActivity(i);
        }
    }
}
