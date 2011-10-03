package net.chrislehmann.sipservice.service;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import net.chrislehmann.common.util.PreferencesUtilities;
import net.chrislehmann.linphone.R;
import net.chrislehmann.sipservice.SipServiceContants;
import org.linphone.LinphoneManager;
import org.linphone.LinphoneService;
import org.linphone.core.Hacks;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreException;
import roboguice.service.RoboService;
import roboguice.util.Ln;

public class SipService extends RoboService implements LinphoneManager.LinphoneServiceListener {


    private final static String LOGTAG = LinphoneService.class.getSimpleName();
    private LinphoneManager linphoneManager;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Ln.d("Got start command");


        String action = intent.getAction();
        if (SipServiceContants.Actions.STOP_SERVICE.equals(action)) {
            stopLinphoneManager();
            stopSelf();
        } else if (SipServiceContants.Actions.HANG_UP.equals(action)) {
            getLinphoneManager().terminateCall();
        } else if (SipServiceContants.Actions.ENABLE_SPEAKERPHONE.equals(action)) {
            getLinphoneManager().routeAudioToSpeaker();
        } else if (SipServiceContants.Actions.DISABLE_SPEAKERPHONE.equals(action)) {
            getLinphoneManager().routeAudioToReceiver();
        } else if (SipServiceContants.Actions.SET_SERVER_INFO.equals(action)) {
            Ln.d("Setting server info");
            PreferencesUtilities.savePreference(getBaseContext(), R.string.pref_username_key, intent.getStringExtra(SipServiceContants.Extras.USERNAME_KEY));
            PreferencesUtilities.savePreference(getBaseContext(), R.string.pref_passwd_key, intent.getStringExtra(SipServiceContants.Extras.PASSWORD));
            PreferencesUtilities.savePreference(getBaseContext(), R.string.pref_domain_key, intent.getStringExtra(SipServiceContants.Extras.DOMAIN));

            stopLinphoneManager();
            startLinphoneManager();
        }


        return START_STICKY;
    }

    private LinphoneManager getLinphoneManager() {
        startLinphoneManager();
        return linphoneManager;
    }

    private void startLinphoneManager() {
        if (linphoneManager == null) {
            Ln.d("Startiing linphone service...");
            linphoneManager = LinphoneManager.createAndStart(getBaseContext(), this);
            LinphoneManager.getLc().setNetworkReachable(true);
        }
    }

    private void dumpInstalledLinphoneInformation() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException nnfe) {
        }

        if (info != null) {
            org.linphone.core.Log.i("Linphone version is ", info.versionCode);
        } else {
            org.linphone.core.Log.i("Linphone version is unknown");
        }
    }

    private void stopLinphoneManager() {
//        stopSelf();
        if (linphoneManager != null) {
            Ln.d("Stopping linphone service...");

            linphoneManager = null;
            LinphoneManager.destroy(this);
        }
    }

    @Override
    public void onCreate() {
        // Set default preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);

        // Dump some debugging information to the logs
        Hacks.dumpDeviceInformation();
        dumpInstalledLinphoneInformation();

        Toast.makeText(this, "Started Service....", Toast.LENGTH_SHORT).show();
        super.onCreate();
    }

    //Intent-only api - No Binding for now.
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onGlobalStateChanged(LinphoneCore.GlobalState state, String message) {

        Log.d(LOGTAG, "Global state changed: " + state + " : " + message);
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
        if (state.equals(LinphoneCore.RegistrationState.RegistrationFailed)) {

        } else if (state.equals(LinphoneCore.RegistrationState.RegistrationOk)) {

        }
        Log.d(LOGTAG, "Registration state changed: " + state + " : " + message);
    }

    public void onCallStateChanged(LinphoneCall call, LinphoneCall.State state, String message) {

        try {
            if (state.equals(LinphoneCall.State.IncomingReceived)) {
                LinphoneManager.getInstance().acceptCallIfIncomingPending();
                LinphoneManager.getInstance().routeAudioToSpeaker();
            } else if (state.equals(LinphoneCall.State.CallEnd)) {
                LinphoneManager.getInstance().routeAudioToReceiver();
                //notify
            }
        } catch (LinphoneCoreException e) {
            throw new RuntimeException(e);
        }

        Log.d(LOGTAG, "Call state changed: " + state + " : " + message);

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
