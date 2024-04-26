package com.example.scannerproto;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddObjectActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText infoEditText;
    public void onReturn(View view) {
        finish();
    }
    public  AddObjectActivity() {

    }

//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//            EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_addobject);
//
//        nameEditText = findViewById(R.id.nameEditText);
//        infoEditText = findViewById(R.id.objectInfo);
//
//        String receivedString = getIntent().getStringExtra("ID");
//        if (receivedString != null) {
//            if (!MainActivity.newObjects.isEmpty()) {
//                String obj = MainActivity.newObjects.get(0);
//            }
//            idEditText.setText(p.getPhoneNumber());
//            LocalDate birthday = p.getBirthday();
//            if (birthday != null) {
//                ageEditText.setText(birthday.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
//            }
//            nameEditText.setText(p.getName());
//            String info = p.getInfo();
//            if (!info.matches("\\s")) {
//                infoEditText.setText(info);
//            }
//    }
//
//    Button selectDateButton = findViewById(R.id.selectDateButton);
//        selectDateButton.setOnClickListener(v -> showDatePickerDialog(AddFriendActivity.this));
//}
}
