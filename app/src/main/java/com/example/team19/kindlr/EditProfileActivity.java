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
        String passwordStr = password.getText().toString();
        String cityStr = city.getText().toString();
        String stateStr = "CA";
        String phoneStr = phone.getText().toString();
        String emailStr = email.getText().toString();

        if(!firstNameStr.isEmpty()){
            UserManager.getUserManager().getCurrentUser().setFirstName(firstNameStr);
        }
        if(!lastNameStr.isEmpty()){
            UserManager.getUserManager().getCurrentUser().setLastName(lastNameStr);
        }
        if(!passwordStr.isEmpty()){
            UserManager.getUserManager().getCurrentUser().setHashedPassword(passwordStr);
        }
        if(!cityStr.isEmpty()){
            UserManager.getUserManager().getCurrentUser().setCity(cityStr);
        }
        if(!stateStr.isEmpty()){
            UserManager.getUserManager().getCurrentUser().setState(stateStr);
            Log.d("profile",UserManager.getUserManager().getCurrentUser().getCity());
        }
        if(!phoneStr.isEmpty()){
            UserManager.getUserManager().getCurrentUser().setPhoneNum(phoneStr);
        }
        if(!emailStr.isEmpty()){
            UserManager.getUserManager().getCurrentUser().setEmail(emailStr);
        }

        UserManager.getUserManager().updateChildFromMap(UserManager.getUserManager().getCurrentUser().getUsername());
    }

    public void navigateBack() {
        this.finish();
    }

}
