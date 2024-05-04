package com.example.scannerproto;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scannerproto.anlaysis.helpers.IObjectInfoAdder;
import com.example.scannerproto.anlaysis.helpers.IObjectInfoGetter;
import com.example.scannerproto.anlaysis.helpers.db.SQLiteManager;
import com.example.scannerproto.anlaysis.helpers.db.ThingWithId;
import com.example.scannerproto.anlaysis.helpers.filedb.FileObjectGetter;
import com.example.scannerproto.anlaysis.helpers.filedb.FileUtils;
import com.example.scannerproto.anlaysis.helpers.filedb.Filethings;
import com.example.scannerproto.anlaysis.helpers.mockdb.SimpleObjectAdder;
import com.example.scannerproto.anlaysis.helpers.mockdb.SimpleObjectInfoGetter;
import com.example.scannerproto.anlaysis.helpers.mockdb.Thing;

import java.io.File;
import java.io.IOException;

public class AddObjectActivity extends AppCompatActivity {

    private IObjectInfoGetter infoGetter = new ThingWithId();
    private IObjectInfoAdder<Thing> adder = new SimpleObjectAdder();
    private EditText nameEditText;
    private EditText infoEditText;

    private String receivedString;
    private ITextValidator validator = new SimpleTextValidator();

    public void onReturn(View view) {
        MainActivity.isNewObjectFound.set(false);
        finish();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
        setContentView(R.layout.activity_addobject);

        nameEditText = findViewById(R.id.nameEditText);
        infoEditText = findViewById(R.id.objectInfo);

        receivedString = getIntent().getStringExtra("ObjectName");
        if (receivedString != null) {
            Log.println(Log.VERBOSE, TAG, receivedString);
            nameEditText.setText(receivedString);
        }
    }

    public void onAdd(View view) {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        String name = nameEditText.getText().toString();
        String info = infoEditText.getText().toString();
        if (validator.validate(info)) {
            if (receivedString != null) {
                sqLiteManager.addNoteToDatabase(new ThingWithId(ThingWithId.thingsArrayList.size() - 1, receivedString, name, info));
                ThingWithId.thingsArrayList.add(new ThingWithId(ThingWithId.thingsArrayList.size() - 1, receivedString, name, info));
                finish();
                MainActivity.isNewObjectFound.set(false);
            }
            else {
                finish();
            }
        } else {
            Toast.makeText(this, "Not enough info for object", Toast.LENGTH_SHORT).show();
        }

    }
    public void clearEditTexts() {
        nameEditText.getText().clear();
        infoEditText.getText().clear();
    }

}
