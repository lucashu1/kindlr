package com.example.team19.kindlr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;




public class PostBookActivity extends AppCompatActivity {

    private EditText title;
    private EditText isbn;
    private EditText author;
    private EditText genre;
    private EditText number;
    private EditText pagecount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_book);

        title = (EditText)findViewById(R.id.title);
        isbn = (EditText) findViewById(R.id.isbn);
        author = (EditText) findViewById(R.id.author);
        genre = (EditText) findViewById(R.id.genre);
        pagecount = (EditText) findViewById(R.id.pagecount);

        Button postBtn = (Button) findViewById(R.id.post);
        postBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button backBtn = (Button) findViewById(R.id.back);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }




}
