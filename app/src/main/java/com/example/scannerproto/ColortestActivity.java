package com.example.scannerproto;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scannerproto.anlaysis.helpers.drone_utils.DroneActionState;

public class ColortestActivity extends AppCompatActivity {

    private TextView color;

    private TextView intensity;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_addobject);

        intensity = findViewById(R.id.textView);
        color = findViewById(R.id.textView2);
    }

    public void onChangeColor(View view) {
        DroneActionState state = new DroneActionState();
        finish();
        startActivity(intent);

    }
}
