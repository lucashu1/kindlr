package com.example.team19.kindlr;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class EditProfileActivity extends Activity {

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
        setContentView(R.layout.activity_edit_profile);

        firstName = (EditText)findViewById(R.id.first_name_edit);
        lastName = (EditText)findViewById(R.id.last_name_edit);
        username = (EditText)findViewById(R.id.username_edit);
        password = (EditText)findViewById(R.id.password_edit);
        city = (EditText)findViewById(R.id.city_edit);
        state = (Spinner)findViewById(R.id.state_edit);
        phone = (EditText)findViewById(R.id.phone_edit);
        email = (EditText)findViewById(R.id.email_edit);

        Button backBtn = (Button) findViewById(R.id.back_edit);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateBack();
            }
        });

        Button createBtn = (Button) findViewById(R.id.edit_profile);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editAccount();
                navigateBack();
            }
        });


    }

    public void editAccount() {
        String firstNameStr = firstName.getText().toString();
        String lastNameStr = lastName.getText().toString();
        String usernameStr = username.getText().toString();
        String passwordStr = password.getText().toString();
        String cityStr = city.getText().toString();
        String stateStr = "WA";
        String phoneStr = phone.getText().toString();
        String emailStr = email.getText().toString();

        User currUser = UserManager.getUserManager().getCurrentUser();
        if(!firstNameStr.isEmpty()){
            currUser.setFirstName(firstNameStr);
        }
        if(!lastNameStr.isEmpty()){
            currUser.setLastName(lastNameStr);
        }
        if(!lastNameStr.isEmpty()){
            currUser.setUsername(usernameStr);
        }
        if(!passwordStr.isEmpty()){
            currUser.setHashedPassword(passwordStr);
        }
        if(!cityStr.isEmpty()){
            currUser.setCity(cityStr);
        }
        if(!stateStr.isEmpty()){
            currUser.setState(stateStr);
            Log.d("profile",currUser.getCity());
        }
        if(!phoneStr.isEmpty()){
            currUser.setPhoneNum(phoneStr);
        }
        if(!emailStr.isEmpty()){
            currUser.setEmail(emailStr);
        }

        UserManager.getUserManager().updateUser(currUser);

    }

    public void navigateBack() {
        this.finish();
    }

}
