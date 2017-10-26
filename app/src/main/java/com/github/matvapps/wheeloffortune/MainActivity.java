package com.github.matvapps.wheeloffortune;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.wheeloffortune.R;

public class MainActivity extends AppCompatActivity {

    WOFView wofView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wofView = (WOFView) findViewById(R.id.fortune_view);

        Button rotateButton = (Button) findViewById(R.id.rotate_btn);

        for (int i = 0; i < 10; i++) {
            wofView.addSector(R.mipmap.ic_launcher);
        }

        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wofView.startRotate();
            }
        });

        wofView.setRotationListener(new CircleView.RotationListener() {
            @Override
            public void rotationStart() {

            }

            @Override
            public void rotationEnd(int sectorIndex) {
                Toast.makeText(MainActivity.this, sectorIndex + "", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
