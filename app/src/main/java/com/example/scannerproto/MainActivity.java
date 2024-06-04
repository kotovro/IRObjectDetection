package com.example.scannerproto;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
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

import com.example.scannerproto.anlaysis.helpers.DetectionBound;
import com.example.scannerproto.anlaysis.helpers.IObjectInfoGetter;
import com.example.scannerproto.anlaysis.helpers.db.SQLiteInfoGetter;
import com.example.scannerproto.anlaysis.helpers.db.Thing;
import com.example.scannerproto.anlaysis.helpers.detection.ObjectDetectionResult;
import com.example.scannerproto.anlaysis.helpers.drone_utils.ServerConnection;
import com.example.scannerproto.anlaysis.helpers.drone_utils.UDPServerDrone;
import com.example.scannerproto.anlaysis.helpers.overlays.Chat;

import com.example.scannerproto.anlaysis.helpers.overlays.StateTable;
import com.example.scannerproto.anlaysis.helpers.overlays.StaticChat;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class MainActivity extends AppCompatActivity {

    private @SuppressLint("RestrictedApi") ImageAnalysis imageAnalysis;
    private static final int PERMISSION_REQUEST_CAMERA = 100;
    ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private CameraSelector cameraSelector;
    private ImageView preview;
    private Bitmap bitmap;
    private final int[] rgba = new int[1920 * 1080];  //todo
    public final IObjectInfoGetter infoGetter = new SQLiteInfoGetter(MainActivity.this);
    private final Chat chat = new Chat();
    private final StaticChat staticChat = new StaticChat();
    public static AtomicBoolean isNewObjectFound = new AtomicBoolean(false);
    public static final Integer DECAY_TIME = 40;
    private BarcodeScanner barcodeScanner;
    private StateTable table = new StateTable();
    private Map<ObjectDetectionResult, Integer> barcodeList = new ConcurrentHashMap<>();
    private UDPServerDrone connection = new UDPServerDrone();
    private ServerConnection handConnection = new ServerConnection();
//    public AtomicBoolean isDrone = new AtomicBoolean(false);
//    public AtomicBoolean isHand = new AtomicBoolean(false);
//    private AtomicBoolean isDetecting = new AtomicBoolean(true);
    private int currentState = 2;
public class States {
    public static final int DRONE = 0;
    public static final int HAND  = 1;
    public static final int BARCODE = 2;
}

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        BarcodeScannerOptions bOptions =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_QR_CODE,
                                Barcode.FORMAT_AZTEC)
                        .build();
        barcodeScanner = BarcodeScanning.getClient(bOptions);
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
        connection.execute();
        handConnection.execute();


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

    public void onSettings(View view) {
        Button btn = findViewById(R.id.button2);
        if (currentState == States.BARCODE) {
            btn.setText("Дрон");
            currentState = States.DRONE;
            return;
        }
        if (currentState == States.DRONE) {
            btn.setText("Рука");
            currentState = States.HAND;
            return;
        }
        if (currentState == States.HAND) {
            btn.setText("Детект");
            currentState = States.BARCODE;
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
                            InputImage inputImage = InputImage.fromBitmap(newBitmap, 0);

                            for (ObjectDetectionResult bc: barcodeList.keySet()) {
                                Integer time = barcodeList.get(bc);
                                barcodeList.put(bc, time - 1);
                                if (time == 0) {
                                    barcodeList.remove(bc);
                                }
                            }
                            if (currentState == States.BARCODE) {
                                barcodeScanner.process(inputImage).addOnSuccessListener(barcodes -> {


                                    if (!barcodes.isEmpty()) {
                                        Point screenCenter = new Point(inputImage.getWidth() / 2, inputImage.getHeight() / 2);
                                        Barcode curBarcode = getClosest(barcodes, screenCenter);
                                        if (barcodes.size() == 2) {
                                            Log.println(Log.VERBOSE, TAG, "wrong");
                                        }


                                        ObjectDetectionResult detectionResult = new ObjectDetectionResult(infoGetter);
                                        detectionResult.setBarcodeMessage(curBarcode.getRawValue());
                                        detectionResult.setBarcode(curBarcode);

                                        LinkedList<Barcode> activeBarcodes = new LinkedList<>();
                                        for (ObjectDetectionResult result: barcodeList.keySet()) {
                                            activeBarcodes.add(result.getBarcode());
                                        }
                                        Barcode mainBarcode = getClosest(activeBarcodes, screenCenter);

                                        // new barcode
                                        if (detectionResult != null) {
                                            Thing info = detectionResult.infoGetter.getObjectInfo(detectionResult.getBarcodeMessage());
                                            if (info == null) {
                                                if (!isNewObjectFound.get()) {
                                                    isNewObjectFound.set(true);
                                                    Intent intent = new Intent(this, AddObjectActivity.class);
                                                    intent.putExtra("ObjectName", detectionResult.getBarcodeMessage());
                                                    startActivity(intent);
                                                }
                                            }
                                        }


                                        if (mainBarcode == null || mainBarcode.getRawValue().equals(curBarcode.getRawValue())) {
                                            barcodeList.remove(detectionResult);
                                            barcodeList.put(detectionResult, DECAY_TIME);
                                        } else {
                                            Log.println(Log.VERBOSE, TAG, String.valueOf(mainBarcode));
                                        }
                                    }

                                }).addOnFailureListener(e -> Log.e(TAG, "Error processing Image", e));
                            }



                            newBitmap = DetectionBound.extractBitmap(bitmap);
                            preview.setRotation(image.getImageInfo().getRotationDegrees());

                            Bitmap temp = DetectionBound.prepareBitmap(newBitmap);

                            if (!barcodeList.isEmpty()) {
                                List<Thing> curInfo = new LinkedList<>();
                                for (ObjectDetectionResult bCode : barcodeList.keySet()) {
                                    Thing info = bCode.infoGetter.getObjectInfo(bCode.getBarcodeMessage());
                                    curInfo.add(info);
                                }
                                if (!curInfo.isEmpty() && !isNewObjectFound.get()) {
                                    DetectionBound.drawDetection(newBitmap,
                                            barcodeList.keySet(),
                                            barcodeList,
                                            curInfo,
                                            (int) preview.getRotation());
                                }
                            }

                            preview.setImageBitmap(newBitmap);

                            if (currentState == States.DRONE) {
                                connection.updateChat(chat);
                                chat.drawChat(new Canvas(newBitmap));
                                chat.tick();
                            }

                            else if (currentState == States.HAND){
                                handConnection.updateChat(staticChat);
                                staticChat.drawChat(new Canvas(newBitmap));
                                staticChat.tick();
                            }

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


    private int distance(Point from, Point to) {
        return  (from.x - to.x) * (from.x - to.x) + (from.y - to.y) * (from.y - to.y);
    }

    private Point calcCenter(Barcode barcode) {
        Point[] points = barcode.getCornerPoints();
        int xMax = Math.max(points[1].x, points[2].x);
        int xMin = Math.min(points[0].x, points[3].x);
        int yMin = Math.min(points[0].y, points[1].y);
        int yMax = Math.max(points[2].y, points[3].y);

        return new Point((xMax + xMin) / 2, (yMax + yMin) / 2);
    }

    private Barcode getClosest(List<Barcode> barcodes, Point center) {
        Barcode targetBarcode = null;
        AtomicInteger minDist = new AtomicInteger(Integer.MAX_VALUE);
        AtomicInteger curDist = new AtomicInteger();

        for (Barcode barcode: barcodes) {
            curDist.set(distance(calcCenter(barcode), center));
            if (curDist.get() <  minDist.get()) {
                minDist.set(curDist.get());
                targetBarcode = barcode;
            }
        }

        return targetBarcode;
    }
}