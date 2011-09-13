package net.chrislehmann.sipservice.service;

import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import roboguice.service.RoboService;
import roboguice.util.Ln;

public class SipService extends RoboService {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Ln.d("Got start command");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Started Service....", Toast.LENGTH_SHORT).show();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
