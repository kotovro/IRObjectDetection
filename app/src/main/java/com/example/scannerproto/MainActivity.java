package com.example.scannerproto;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.CameraProfile;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.scannerproto.anlaysis.helpers.BarcodeScanningActivity;
import com.example.scannerproto.anlaysis.helpers.DetectionBound;
import com.example.scannerproto.anlaysis.helpers.ObjectDetectionResult;
import com.example.scannerproto.anlaysis.helpers.ThingsBase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.ObjectDetectorOptionsBase;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    private static final int MEMORY_DELAY = 10;
    public static ThingsBase base = new ThingsBase();


    private ImageView preview;

    private TextView output;
    private  BarcodeScanningActivity barCodeActivity = new BarcodeScanningActivity();
    private @SuppressLint("RestrictedApi") ImageAnalysis imageAnalysis;
    private static final int PERMISSION_REQUEST_CAMERA = 100;
    ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ObjectDetector objectDetector;

    private DetectedObject detObj;
    private BarcodeScanner barcodeScanner;
    private CameraSelector cameraSelector;

    YUVtoRGB translator = new YUVtoRGB();
    private Bitmap bitmap = null;

    private  final int UPDATE_RATE = 2;
    private int frameCount = 0;
    private Map<ObjectDetectionResult, Integer> barcodeList = new ConcurrentHashMap<>();


//    public void onIR(View view) {
//        //Toast.makeText(this, "IR ->", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//    }
//    public void onQRScanner(View view) {
//        //Toast.makeText(this, "QR ->", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, QRScanActivity.class);
//        startActivity(intent);
//    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        BarcodeScannerOptions bOptions =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_QR_CODE,
                                Barcode.FORMAT_AZTEC)
                        .build();
        barcodeScanner = BarcodeScanning.getClient(bOptions);
        ObjectDetectorOptions options =
                new ObjectDetectorOptions.Builder()
                        .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
//                        .enableClassification()  // Optional
                        .build();
        objectDetector = ObjectDetection.getClient(options);
        preview = findViewById(R.id.preview);
        imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1024, 768))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},
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


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void initializeCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
                ProcessCameraProvider cameraProvider = null;
                try {
                    cameraProvider = cameraProviderFuture.get();
                    imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(MainActivity.this),
                            image -> {
                                @OptIn(markerClass = androidx.camera.core.ExperimentalGetImage.class)
                                Image img = image.getImage();
                                bitmap = translator.translateYUV(img, MainActivity.this);
                                InputImage inputImage = InputImage.fromBitmap(bitmap, image.getImageInfo().getRotationDegrees());


                                for (ObjectDetectionResult obj: barcodeList.keySet()) {
                                    int value = barcodeList.get(obj);
                                    if (value == MEMORY_DELAY) {
                                        barcodeList.remove(obj);
                                    } else {
                                        barcodeList.put(obj, value+1);
                                    }
                                }

                                if ((frameCount % UPDATE_RATE == 0)) {
                                    detObj = null;
//                                    Log.println(Log.DEBUG, TAG, detObj.toString() + "111u9ipodcesaqoidawoy");
                                    objectDetector.process(inputImage).addOnSuccessListener(detectedObjects -> {
                                        if (!detectedObjects.isEmpty()) {
                                            detObj = detectedObjects.get(0);
                                        }
                                    }).addOnFailureListener(e -> Log.e(TAG, "Error processing Image", e));

                                    if (detObj != null) {
                                        Rect temp = detObj.getBoundingBox();
                                        inputImage = InputImage.fromBitmap(Bitmap.createBitmap(bitmap,
                                                        temp.left,
                                                        temp.top,
                                                        temp.width(),
                                                        temp.height()),
                                                        image.getImageInfo().getRotationDegrees());
                                        Log.println(Log.VERBOSE, TAG, inputImage.getWidth() + " " + inputImage.getHeight());
                                        Log.println(Log.VERBOSE, TAG, temp.left + " " + " " + temp.top + " " + temp.width() + " " + temp.height());
                                    } else {
                                        Log.println(Log.VERBOSE, TAG, "No obj detected");
                                    }
                                    barcodeScanner.process(inputImage).addOnSuccessListener(barcodes -> {
                                        if (!barcodes.isEmpty()) {
                                                for (Barcode barcode : barcodes) {
                                                    ObjectDetectionResult detectionResult = new ObjectDetectionResult();
                                                    detectionResult.setBarcodeMessage(barcode.getRawValue());
                                                    detectionResult.setBarcode(barcode);
                                                    barcodeList.put(detectionResult, 0);                                                }
                                        }
                                    }).addOnFailureListener(e -> Log.e(TAG, "Error processing Image", e));
                                }

                                preview.setRotation(image.getImageInfo().getRotationDegrees());
                                if (!barcodeList.isEmpty() || detObj != null) {
//                                    for (ObjectDetectionResult detectionResult : barcodeList.keySet()) {
//                                        if (detObj != null) {
                                    DetectionBound.drawDetection(bitmap, detObj,
                                            barcodeList.keySet(),
                                            (int) preview.getRotation());
//                                        }
//                                     }
                                }
                                preview.setImageBitmap(bitmap);

                                image.close();
                                frameCount++;
                            });

                    cameraProvider.bindToLifecycle(MainActivity.this, cameraSelector, imageAnalysis);

                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);

                } catch (Exception e) {
                    Log.e(TAG, "Bind Error", e);
                    Toast.makeText(MainActivity.this, "PermissionError", Toast.LENGTH_SHORT).show();
                }

            }, ContextCompat.getMainExecutor(this));
    }

}