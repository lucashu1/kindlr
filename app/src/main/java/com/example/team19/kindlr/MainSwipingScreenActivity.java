package com.example.team19.kindlr;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class MainSwipingScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_swiping_screen);

        Button likeBtn = (Button) findViewById(R.id.like_button);
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    
}
