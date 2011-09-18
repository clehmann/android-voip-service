package net.chrislehmann.sipservice.service;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;
import net.chrislehmann.sipservice.SipServiceContants;
import org.linphone.LinphoneManager;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCore;
import roboguice.service.RoboService;
import roboguice.util.Ln;

public class SipService extends RoboService implements LinphoneManager.LinphoneServiceListener {


    private LinphoneManager linphoneManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Ln.d("Got start command");

        if (linphoneManager == null) {
            linphoneManager = LinphoneManager.createAndStart(getBaseContext(), this);
        }


        String action = intent.getAction();
        if (SipServiceContants.Actions.STOP_SERVICE.equals(action)) {
            stopLinphoneManager();
        } else if (SipServiceContants.Actions.HANG_UP.equals(action)) {
            linphoneManager.terminateCall();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void stopLinphoneManager() {
        stopSelf();
        linphoneManager = null;
        LinphoneManager.destroy(this);
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Started Service....", Toast.LENGTH_SHORT).show();
        super.onCreate();
    }

    //Intent-only api - No Binding for now.
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onGlobalStateChanged(LinphoneCore.GlobalState state, String message) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void tryingNewOutgoingCallButCannotGetCallParameters() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void tryingNewOutgoingCallButWrongDestinationAddress() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void tryingNewOutgoingCallButAlreadyInCall() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onRegistrationStateChanged(LinphoneCore.RegistrationState state, String message) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onCallStateChanged(LinphoneCall call, LinphoneCall.State state, String message) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onRingerPlayerCreated(MediaPlayer mRingerPlayer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onDisplayStatus(String message) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onAlreadyInVideoCall() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onCallEncryptionChanged(LinphoneCall call, boolean encrypted, String authenticationToken) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
