package com.example.scannerproto;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.View;
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

import com.example.scannerproto.anlaysis.helpers.DetectionBound;
import com.example.scannerproto.anlaysis.helpers.IObjectInfoGetter;
import com.example.scannerproto.anlaysis.helpers.ObjectDetectionResult;
import com.example.scannerproto.anlaysis.helpers.db.SQLiteManager;
import com.example.scannerproto.anlaysis.helpers.db.ThingWithId;
import com.example.scannerproto.anlaysis.helpers.filedb.FileObjectGetter;
import com.example.scannerproto.anlaysis.helpers.mockdb.SimpleObjectInfoGetter;
import com.example.scannerproto.anlaysis.helpers.mockdb.Thing;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetector;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends AppCompatActivity {

    private ImageView preview;
    private @SuppressLint("RestrictedApi") ImageAnalysis imageAnalysis;
    private static final int PERMISSION_REQUEST_CAMERA = 100;
    ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ObjectDetector objectDetector;
    private DetectedObject detObj;

    public static AtomicBoolean isNewObjectFound = new AtomicBoolean(false);
    private BarcodeScanner barcodeScanner;
    private CameraSelector cameraSelector;
    public static String newObject = new String();
    public final static IObjectInfoGetter infoGetter = new ThingWithId();
    YUVtoRGB translator = new YUVtoRGB();
    private Bitmap bitmap = null;
    private final int UPDATE_RATE = 1;
    private int frameCount = 0;
    private List<ObjectDetectionResult> barcodeList = new CopyOnWriteArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFromDBToMemory();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        BarcodeScannerOptions bOptions =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_QR_CODE,
                                Barcode.FORMAT_AZTEC)
                        .build();
        barcodeScanner = BarcodeScanning.getClient(bOptions);
//        ObjectDetectorOptions options =
//                new ObjectDetectorOptions.Builder()
//                        .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
////                        .enableClassification()  // Optional
//                        .build();
//        objectDetector = ObjectDetection.getClient(options);
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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CAMERA);
        } else {
            initializeCamera();
        }
    }

    public void onAdd(View view) {
        Intent intent = new Intent(this, AddObjectActivity.class);
        startActivity(intent);
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

    public void loadFromDBToMemory() {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(MainActivity.this);
        sqLiteManager.populateNoteListArray();
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
                            bitmap = translator.translateYUV(img, MainActivity.this);
                            Bitmap newBitmap = DetectionBound.extractBitmap(bitmap);
                            InputImage inputImage = InputImage.fromBitmap(newBitmap, image.getImageInfo().getRotationDegrees());


                            if ((frameCount % UPDATE_RATE == 0) && !isNewObjectFound.get()) {
                                barcodeScanner.process(inputImage).addOnSuccessListener(barcodes -> {
                                    barcodeList.clear();
                                    if (!barcodes.isEmpty()) {
                                        for (Barcode barcode : barcodes) {
                                            ObjectDetectionResult detectionResult = new ObjectDetectionResult(infoGetter);
                                            detectionResult.setBarcodeMessage(barcode.getRawValue());
                                            detectionResult.setBarcode(barcode);
                                            barcodeList.add(detectionResult);
                                        }
                                    }
                                }).addOnFailureListener(e -> Log.e(TAG, "Error processing Image", e));
                            }

                            preview.setRotation(image.getImageInfo().getRotationDegrees());
                            if (!barcodeList.isEmpty()) {
                                List<Thing> curInfo = new LinkedList<>();
                                for (ObjectDetectionResult bCode : barcodeList) {
                                    Thing info = bCode.infoGetter.getObjectInfo(bCode.getBarcodeMessage());
                                    if (info == null) {
                                        if (!isNewObjectFound.get()) {
                                            isNewObjectFound.set(true);
                                            Log.println(Log.VERBOSE, TAG, newObject);
                                            newObject = bCode.getBarcodeMessage();
                                            Intent addNewIntent = new Intent(MainActivity.this, AddObjectActivity.class);
                                            addNewIntent.putExtra("ObjectName", newObject);
                                            startActivity(addNewIntent);
                                            addNewIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            break;
                                        }
                                    } else {
                                        curInfo.add(info);
                                    }
                                }
                                if (!curInfo.isEmpty()) {
                                    DetectionBound.drawDetection(newBitmap,
                                            barcodeList,
                                            curInfo,
                                            (int) preview.getRotation());
                                }
                            }
//                            newBitmap = DetectionBound.prepareBitmap(newBitmap);
                            preview.setImageBitmap(newBitmap);

                            image.close();
                            frameCount++;
                        });

                cameraProviderFuture.get().bindToLifecycle(MainActivity.this, cameraSelector, imageAnalysis);

            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);


            } catch (Exception e) {
                Log.e(TAG, "Bind Error", e);
                Toast.makeText(MainActivity.this, "PermissionError", Toast.LENGTH_SHORT).show();
            }

        }, ContextCompat.getMainExecutor(this));
    }

}