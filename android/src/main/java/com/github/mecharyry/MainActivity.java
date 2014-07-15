package com.github.mecharyry;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(onButtonClicked);

    }

    private final View.OnClickListener onButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, "Lets Have Toast!", Toast.LENGTH_SHORT).show();
        }
    };
}
