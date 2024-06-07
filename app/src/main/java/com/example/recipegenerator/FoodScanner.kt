package com.example.recipegenerator

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector


class FoodScanner : AppCompatActivity() {
    var barcodeInfo: TextView? = null
    var cameraView: SurfaceView? = null
    var cameraSource: CameraSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_food_scanner)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        cameraView = findViewById<View>(R.id.camera_view) as SurfaceView
        barcodeInfo = findViewById<View>(R.id.txtContent) as TextView


        val barcodeDetector: BarcodeDetector =
            BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.CODE_128) //QR_CODE)
                .build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(640, 480)
            .build()

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    barcodeInfo!!.text = getString(R.string.scanning_requires_camera_permission)
                }
            }

        cameraView!!.getHolder().addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                when {
                    ContextCompat.checkSelfPermission(
                        this@FoodScanner,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        // You can use the API that requires the permission.

                        cameraSource?.start(cameraView!!.holder)
                    }
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        this@FoodScanner, Manifest.permission.CAMERA) -> {
                        // In an educational UI, explain to the user why your app requires this
                        // permission for a specific feature to behave as expected, and what
                        // features are disabled if it's declined. In this UI, include a
                        // "cancel" or "no thanks" button that lets the user continue
                        // using your app without granting the permission.

                    }
                    else -> {
                        // You can directly ask for the permission.
                        // The registered ActivityResultCallback gets the result of this request.
                        requestPermissionLauncher.launch(
                            Manifest.permission.CAMERA)
                    }
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource?.stop()
            }
        })


        barcodeDetector.setProcessor(object : Detector.Processor<Barcode?> {
            override fun release() {
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode?>) {
                val barcodes: SparseArray<Barcode?> = detections.detectedItems

                if (barcodes.size() != 0) {
                    barcodeInfo!!.post(Runnable
                    // Use the post method of the TextView
                    {
                        barcodeInfo!!.text = barcodes.valueAt(0)?.displayValue ?: "No Value"
                    })
                }
            }
        })
    }
}