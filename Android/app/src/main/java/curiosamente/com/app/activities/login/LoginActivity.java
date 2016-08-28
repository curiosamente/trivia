package curiosamente.com.app.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import curiosamente.com.app.R;
import curiosamente.com.app.activities.main.MainActivity;
import curiosamente.com.app.manager.LogInManager;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    private boolean newActivityAlreadyCreated = false;

    private LoginButton facebookLoginButton;
    private SignInButton googleSignInButton;
    private FacebookCallback<LoginResult> callback;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login_fragment);

        callbackManager = CallbackManager.Factory.create();
        initTrackers();

        callback = createCallBack();
        facebookLoginButton = (LoginButton) findViewById(R.id.login_button);
        facebookLoginButton.registerCallback(callbackManager, callback);
        facebookLoginButton.setReadPermissions("user_friends");

        mGoogleApiClient = LogInManager.getGoogleApiClient(this);
        googleSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        setGooglePlusButtonText(googleSignInButton, getResources().getString(R.string.login_activity_login_with_google));
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                mGoogleApiClient.connect();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }


    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

    public FacebookCallback<LoginResult> createCallBack() {
        return new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                initTrackers();
                facebookLoginButton.setVisibility(View.INVISIBLE);
                googleSignInButton.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.main_activity_loading), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        };
    }

    private void initTrackers() {

        if (accessTokenTracker == null && profileTracker == null) {
            accessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken1) {

                }
            };
            accessTokenTracker.startTracking();

            profileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile profile, Profile profile1) {
                    if (newActivityAlreadyCreated) {
                        newActivityAlreadyCreated = false;
                    } else {
                        nextActivity();
                    }
                }
            };
            profileTracker.startTracking();
        }
    }


    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
        handleGoogleSignInResult(result);
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }

    private void nextActivity() {
        newActivityAlreadyCreated = true;
        Intent main = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(main);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        boolean isConnected = LogInManager.isConnected(this);
        if (isConnected) {
            if (!LogInManager.isConnectedToFacebook() && !mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }
            nextActivity();
        }
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        if (result != null && result.isSuccess()) {
            facebookLoginButton.setVisibility(View.INVISIBLE);
            googleSignInButton.setVisibility(View.INVISIBLE);
            GoogleSignInAccount acct = result.getSignInAccount();
            LogInManager.storeGoogleSingInAccount(acct, this);
            nextActivity();
        }
    }
}