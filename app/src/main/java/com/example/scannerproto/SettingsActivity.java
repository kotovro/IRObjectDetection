package com.example.scannerproto;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private RadioButton enableChat;
    private RadioButton enableTable;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        enableChat = findViewById(R.id.radioChat);
        enableTable = findViewById(R.id.radioTable);
        enableTable.setActivated(MainActivity.isTableUi.get());
        enableChat.setActivated(!MainActivity.isTableUi.get());
    }

    public void onApply(View view) {
        if (enableTable.isActivated()) {
            MainActivity.isTableUi.set(true);
            enableChat.setActivated(false);
        }
        if (enableChat.isActivated()) {
            MainActivity.isTableUi.set(false);
            enableTable.setActivated(false);
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
