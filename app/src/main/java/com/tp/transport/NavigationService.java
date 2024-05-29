package com.tp.transport;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

public class NavigationService extends IntentService {

    public static final String ACTION_NAVIGATE = "com.tp.transport.action.NAVIGATE";
    public static final String EXTRA_PAGE = "com.tp.transport.extra.PAGE";
    private static final String TAG = "NavigationService";

    public NavigationService() {
        super("NavigationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_NAVIGATE.equals(action)) {
                final String page = intent.getStringExtra(EXTRA_PAGE);
                assert page != null;
                handleActionNavigate(page);
            }
        }
    }

    private void handleActionNavigate(String page) {
        Intent intent;
        switch (page) {
            case "Profile":
                intent = new Intent(this, ProfileActivity.class);
                break;
            case "Signalements":
                intent = new Intent(this, SignalementActivity.class);
                break;
            case "Trafic":
                intent = new Intent(this, TraficInfoActivity.class);
                break;
            case "Responsable":
                intent = new Intent(this, EspResActivity.class);
                break;
            case "Logout":
                intent = new Intent(this, LoginActivity.class);

                break;
            default:
                Log.e(TAG, "Page non reconnue: " + page);
                return;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
