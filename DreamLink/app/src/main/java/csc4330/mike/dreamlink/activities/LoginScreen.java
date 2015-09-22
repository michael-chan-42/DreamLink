package csc4330.mike.dreamlink.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.text.ParseException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import csc4330.mike.dreamlink.R;
import csc4330.mike.dreamlink.components.Contact;

/**
 * Created by Mike on 9/4/15.
 */
public class LoginScreen extends ActionBarActivity {

    @Bind(R.id.user_ET)
    EditText userEditText;
    @Bind(R.id.password_ET)
    EditText passwordEditText;
    @Bind(R.id.email_ET)
    EditText emailEditText;
    @Bind(R.id.submit_button)
    Button submitButton;

    private String userField;
    private String passwordField;
    private String emailField;

    private LoginButton facebookLoginButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this);

        ParseFacebookUtils.initialize(this);

        setContentView(R.layout.activity_user_login);

        facebookLoginButton = (LoginButton)findViewById(R.id.fb_button);

        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginScreen.this,
                        Arrays.asList("email", "user_friends", "public_profile"), new LogInCallback() {
                    @Override
                    public void done(final ParseUser user, com.parse.ParseException e) {
                        if (user == null) {
                            Log.d("MyApp", "The user cancelled the Facebook login.");

                            Toast.makeText(getApplicationContext(), "Log-out from Facebook and try again please!", Toast.LENGTH_SHORT).show();

                            ParseUser.logOut();
                        } else if (user.isNew()) {
                            Log.d("MyApp", "User signed up and logged in through Facebook!");

                            if (!ParseFacebookUtils.isLinked(user)) {
                                ParseFacebookUtils.linkWithReadPermissionsInBackground(user, LoginScreen.this,
                                        Arrays.asList("email", "user_friends", "public_profile"), new SaveCallback() {
                                    @Override
                                    public void done(com.parse.ParseException e) {
                                        if (ParseFacebookUtils.isLinked(user)) {
                                            Log.d("MyApp", "User logged in with Facebook!");
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "You can change your personal data in Settings tab!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d("MyApp", "User logged in through Facebook!");

                            if (!ParseFacebookUtils.isLinked(user)) {
                                ParseFacebookUtils.linkWithReadPermissionsInBackground(user, LoginScreen.this,
                                        Arrays.asList("email", "user_friends", "public_profile"), new SaveCallback() {
                                    @Override
                                    public void done(com.parse.ParseException e) {
                                        if (ParseFacebookUtils.isLinked(user)) {
                                            Log.d("MyApp", "User logged in with Facebook!");
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });

        ButterKnife.bind(this);

        userEditText.setHint("username");
        passwordEditText.setHint("password");
        emailEditText.setHint("email");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Contact contact = new Contact();

                    //Check for a username
                    if (userEditText.getText().toString().isEmpty()) {
                        userEditText.setError("UserName cannot be blank");
                        //Check for password
                    } else if (passwordEditText.getText().toString().isEmpty()) {
                        passwordEditText.setError("Password field cannot not be blank");
                        //Check for email
                    } else if (emailEditText.getText().toString().isEmpty()) {
                        emailEditText.setError("Email field cannot be blank");
                        //Create the contact and make it into ParseUser
                    } else if (emailCheck(emailEditText.getText().toString()) == false) {
                        emailEditText.setError("Your entry is not a valid email address");

                    } else {
                        contact.setUserName(userEditText.getText().toString());
                        contact.setUserPassword(passwordEditText.getText().toString());
                        contact.setUserEmail(emailEditText.getText().toString());

                        createParseUser(contact);

                    }
                } catch (Exception e) {

                    e.printStackTrace();
                    Toast.makeText(LoginScreen.this, "Please correct your entries and resubmit", Toast.LENGTH_SHORT);
                    return;
                }
            }
        });
    }

    public static ParseUser createParseUser(Contact contact) {

        ParseUser user = new ParseUser();
        user.setUsername(contact.getUserName());
        user.setPassword(contact.getUserPassword());
        user.setEmail(contact.getUserEmail());

        // other fields can be set just like with ParseObject
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {

            }

            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
        return user;
    }

    public static boolean emailCheck(String email) {

        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}

