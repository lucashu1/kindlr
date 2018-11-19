package com.example.team19.kindlr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class SignupActivity extends Activity {

    private EditText firstName;
    private EditText lastName;
    private EditText username;
    private EditText password;
    private EditText city;
    private Spinner state;
    private EditText phone;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstName = (EditText)findViewById(R.id.first_name);
        lastName = (EditText)findViewById(R.id.last_name);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        city = (EditText)findViewById(R.id.city);
        state = (Spinner)findViewById(R.id.state);
        phone = (EditText)findViewById(R.id.phone);
        email = (EditText)findViewById(R.id.email);

        Button backBtn = (Button) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateBack();
            }
        });

        Button createBtn = (Button) findViewById(R.id.create_profile);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

    }

    public void navigateBack() {
        this.finish();
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void createAccount() {
        String firstNameStr = firstName.getText().toString();
        String lastNameStr = lastName.getText().toString();
        String usernameStr = username.getText().toString();
        String passwordStr = password.getText().toString();

        String cityStr = city.getText().toString();
        String stateStr = state.getSelectedItem().toString();
        String phoneStr = phone.getText().toString();
        String emailStr = email.getText().toString();

        if (firstNameStr.isEmpty() || lastNameStr.isEmpty() || usernameStr.isEmpty() ||
                passwordStr.isEmpty() || cityStr.isEmpty() || stateStr.isEmpty() ||
                phoneStr.isEmpty() || emailStr.isEmpty()) {
            ErrorHelper.displayError("Invalid", "Incomplete details", this);
            Log.d("Signup","empty error");
            return;


        }

        //check if phone number is valid
        if(!PhoneNumberUtils.isGlobalPhoneNumber(phoneStr)){
            ErrorHelper.displayError("Invalid","Invalid Phone Number",this);
            Log.d("success","wrong phone");
            return;
        }

        //check if email is valid
        if(!isEmailValid( emailStr)){
            ErrorHelper.displayError("Invalid","Invalid Email", this);
            Log.d("email1","bad email");
            return;
        }

        boolean success = UserManager.getUserManager().addUser(usernameStr, passwordStr, firstNameStr, lastNameStr,
                cityStr, stateStr, phoneStr, emailStr);


        if (!success) {
            ErrorHelper.displayError("Invalid", "Invalid details", this);

            return;

        }
        else {
            Intent intent = new Intent(SignupActivity.this, MainSwipingScreenActivity.class);
            startActivity(intent);
        }
    }

}
