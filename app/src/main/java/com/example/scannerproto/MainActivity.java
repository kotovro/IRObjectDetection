package com.example.scannerproto;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.scannerproto.anlaysis.helpers.drone_utils.ServerConnection;
import com.example.scannerproto.anlaysis.helpers.drone_utils.UDPServerDrone;
import com.example.scannerproto.anlaysis.helpers.overlays.Chat;
import com.example.scannerproto.anlaysis.helpers.DetectionBound;
import com.example.scannerproto.anlaysis.helpers.overlays.StateTable;
import com.example.scannerproto.anlaysis.helpers.overlays.StaticChat;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends AppCompatActivity {

    private @SuppressLint("RestrictedApi") ImageAnalysis imageAnalysis;
    private static final int PERMISSION_REQUEST_CAMERA = 100;
    ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private CameraSelector cameraSelector;
    private ImageView preview;
    private Bitmap bitmap;
    private final int[] rgba = new int[1920 * 1080];  //todo
    private final Chat chat = new Chat();
    private final StaticChat staticChat = new StaticChat();
    public static AtomicBoolean isNewObjectFound = new AtomicBoolean(false);
    public static final Integer DECAY_TIME = 40;
    private StateTable table = new StateTable();
    private UDPServerDrone connection = new UDPServerDrone();
    private ServerConnection handConnection = new ServerConnection();
    public static volatile AtomicBoolean isChat = new AtomicBoolean(true);

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        preview = findViewById(R.id.preview);
        imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1024, 768))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        handConnection.execute();
        connection.execute();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CAMERA);
        } else {
            initializeCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializeCamera();
        } else {
            Toast.makeText(this, "PermissionError", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void onSettings(View view){
        isChat.set(!isChat.get());
        Button btn = findViewById(R.id.button2);
        if (isChat.get()) {
            btn.setText("Дрон".toCharArray(), 0, 4);
        } else {
            btn.setText("Рука".toCharArray(), 0, 4);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initializeCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(MainActivity.this),
                        image -> {
                            @OptIn(markerClass = androidx.camera.core.ExperimentalGetImage.class)
                            Image img = image.getImage();
                            byte[] bytes = YUVtoRGB.imageToByteBuffer(img).array();

                            //updates rgba
                            YUVtoRGB.decodeYUV420SP(rgba, bytes, img.getWidth(), img.getHeight());

                            bitmap = bitmap == null ?
                                    Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888) : bitmap;
                            bitmap.setPixels(rgba, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

                            Bitmap newBitmap = DetectionBound.extractBitmap(bitmap);
                            preview.setRotation(image.getImageInfo().getRotationDegrees());

                            if (isChat.get()) {
                                connection.updateChat(chat);
                                chat.drawChat(new Canvas(newBitmap));
                                chat.tick();
                            }
                            else {
                                handConnection.updateChat(staticChat);
                                staticChat.drawChat(new Canvas(newBitmap));
                                staticChat.tick();
                            }

                            Bitmap temp = DetectionBound.prepareBitmap(newBitmap);
                            preview.setImageBitmap(newBitmap);
                            image.close();
                        });
                cameraProviderFuture.get().bindToLifecycle(MainActivity.this, cameraSelector, imageAnalysis);
            } catch (Exception e) {
                Log.e(TAG, "Bind Error", e);
                Toast.makeText(MainActivity.this, "PermissionError", Toast.LENGTH_SHORT).show();
            }

        }, ContextCompat.getMainExecutor(this));
    }
}