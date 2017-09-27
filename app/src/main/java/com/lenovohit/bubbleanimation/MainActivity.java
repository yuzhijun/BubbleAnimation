package com.lenovohit.bubbleanimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnBubble;
    private BubbleLayout blBubble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBubble = (Button) findViewById(R.id.btnBubble);
        blBubble = (BubbleLayout) findViewById(R.id.blBubble);

        btnBubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blBubble.addBubbles();
            }
        });
    }
}
