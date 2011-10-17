package net.chrislehmann.sipservice.service;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import net.chrislehmann.common.util.PreferencesUtilities;
import net.chrislehmann.linphone.R;
import net.chrislehmann.sipservice.SipServiceContants;
import org.linphone.LinphoneManager;
import org.linphone.LinphonePreferenceManager;
import org.linphone.core.Hacks;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreException;
import roboguice.service.RoboService;
import roboguice.util.Ln;

public class SipService extends RoboService implements LinphoneManager.LinphoneServiceListener {


    private final static String LOGTAG = SipService.class.getSimpleName();
    private LinphoneManager linphoneManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Ln.d("Got start command");

        String action = intent != null ? intent.getAction() : null ;

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
            PreferencesUtilities.savePreference(this, R.string.pref_username_key, intent.getStringExtra(SipServiceContants.Extras.USERNAME_KEY));
            PreferencesUtilities.savePreference(this, R.string.pref_passwd_key, intent.getStringExtra(SipServiceContants.Extras.PASSWORD));
            PreferencesUtilities.savePreference(this, R.string.pref_domain_key, intent.getStringExtra(SipServiceContants.Extras.DOMAIN));

            PreferencesUtilities.savePreference(this, R.string.pref_codec_pcma_key, true);
            PreferencesUtilities.savePreference(this, R.string.pref_echo_cancellation_key, true);
            PreferencesUtilities.savePreference(this, R.string.pref_echo_canceller_calibration_key, true);
            PreferencesUtilities.savePreference(this, R.string.pref_video_enable_key, true);
            PreferencesUtilities.savePreference(this, R.string.pref_transport_udp_key, true);
            PreferencesUtilities.savePreference(this, R.string.pref_audio_use_specific_mode_key, "0");
            PreferencesUtilities.savePreference(this, R.string.pref_video_codec_mpeg4_key, true);
            PreferencesUtilities.savePreference(this, R.string.pref_codec_amr_key, true);
            PreferencesUtilities.savePreference(this, R.string.pref_codec_pcmu_key, true);
            PreferencesUtilities.savePreference(this, R.string.pref_codec_speex32_key, true);
//            PreferencesUtilities.savePreference(this, R.string.pref_codec_g722_key, false);
            PreferencesUtilities.savePreference(this, R.string.pref_codec_gsm_key, true);
            PreferencesUtilities.savePreference(this, R.string.pref_codec_speex16_key, true);
            PreferencesUtilities.savePreference(this, R.string.pref_codec_speex8_key, true);
            PreferencesUtilities.savePreference(this, R.string.pref_video_codec_vp8_key, true);
            PreferencesUtilities.savePreference(this, R.string.pref_codec_ilbc_key, true);
            PreferencesUtilities.savePreference(this, R.string.pref_video_initiate_call_with_video_key, true);
            PreferencesUtilities.savePreference(this, R.string.pref_video_automatically_share_my_video_key, true);
//            PreferencesUtilities.savePreference(this, R.string.pref_first, false);


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

            LinphonePreferenceManager.getInstance(this);
            linphoneManager = LinphoneManager.createAndStart(this, this);

            try {
                LinphoneManager.getInstance().initFromConf(getApplicationContext());
            } catch (Throwable e) {
                final String errorMessage = "Error while initializing sip config";
                org.linphone.core.Log.e(e, errorMessage);
                Intent i = new Intent(SipServiceContants.Actions.ERROR_REGISTERING);
                i.putExtra(SipServiceContants.Extras.ERROR_MESSAGE, errorMessage);
                sendBroadcast(i);
//            toast(R.string.error);;
            }

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
        PreferenceManager.setDefaultValues(this, R.xml.linphone_preferences, true);

        // Dump some debugging information to the logs
        Hacks.dumpDeviceInformation();
        dumpInstalledLinphoneInformation();

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
        if (state.equals(LinphoneCore.RegistrationState.RegistrationOk)) {
            sendBroadcast(new Intent(SipServiceContants.Actions.REGISTRATION_SUCCESS));
        } else if (state.equals(LinphoneCore.RegistrationState.RegistrationFailed)) {
            Intent i = new Intent(SipServiceContants.Actions.ERROR_REGISTERING);
            i.putExtra(SipServiceContants.Extras.ERROR_MESSAGE, message);
            sendBroadcast(i);
        }
        Log.d(LOGTAG, "Registration state changed: " + state + " : " + message);
    }

    public void onCallStateChanged(LinphoneCall call, LinphoneCall.State state, String message) {

        Log.d(LOGTAG, "Call state changed: " + state + " : " + message);

        try {
            if (state.equals(LinphoneCall.State.IncomingReceived)) {
                LinphoneManager.getInstance().acceptCallIfIncomingPending();
            } else if (state.equals(LinphoneCall.State.CallEnd) || state.equals(LinphoneCall.State.CallReleased)) {
                sendBroadcast(new Intent(SipServiceContants.Actions.CALL_ENDED));
            } else if (state.equals(LinphoneCall.State.Error)) {
                Intent i = new Intent(SipServiceContants.Actions.ERROR_REGISTERING);
                i.putExtra(SipServiceContants.Extras.ERROR_MESSAGE, message);
                sendBroadcast(i);
            }
        } catch (LinphoneCoreException e) {
            throw new RuntimeException(e);
        }

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
