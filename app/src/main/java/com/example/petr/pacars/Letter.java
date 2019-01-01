package com.example.petr.pacars;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Letter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter);
        Bundle arguments = getIntent().getExtras();
        String text = arguments.getString ("text");
        String name = arguments.getString ("name");

        TextView txt = findViewById(R.id.letter_text);
        TextView nme = findViewById(R.id.letter_name);
        txt.setText(text);
        nme.setText(name);
    }
}
