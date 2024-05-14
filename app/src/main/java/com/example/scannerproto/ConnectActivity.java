package com.example.scannerproto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ConnectActivity extends AppCompatActivity {
    private EditText editServerIP;
    private EditText editPort;

    private ITextValidator validator = new SimpleTextValidator();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        editServerIP = findViewById(R.id.editTextServerIP);
        editPort = findViewById(R.id.editTextPort);
    }

    public void onConnect(View view) {
        if (validator.validate(editServerIP.getText().toString()) && validator.validate((editPort.getText().toString()))) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("ServerIP", editServerIP.getText().toString());
            intent.putExtra("Port", editPort.getText().toString());
            finish();
            startActivity(intent);

        }
    }
}
