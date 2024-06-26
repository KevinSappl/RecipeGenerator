package com.example.recipegenerator

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException


class FoodScanner : AppCompatActivity() {
    var barcodeInfo: TextView? = null
    var cameraView: SurfaceView? = null
    var cameraSource: CameraSource? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var barcodeAdapter: BarcodeAdapter
    private val barcodeList = mutableListOf<String>()
    private var readyToDetect = true;

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
        recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView

        recyclerView.layoutManager = LinearLayoutManager(this)
        barcodeAdapter = BarcodeAdapter(barcodeList)
        recyclerView.adapter = barcodeAdapter

        val barcodeDetector: BarcodeDetector =
            BarcodeDetector.Builder(this@FoodScanner)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(800, 800)
            .build()

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    startCameraSource()
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
                if (barcodes.size() != 0 && readyToDetect) {
                    readyToDetect = false
                    barcodeInfo!!.post {
                        Log.d(
                            "FoodScanner",
                            "Barcode detected: ${barcodes.valueAt(0)?.displayValue}"
                        )
                        val barcodeValue = barcodes.valueAt(0)?.displayValue ?: "No Value"
                        barcodeInfo!!.text = barcodeValue
                        if (!barcodeList.contains(barcodeValue)) {
                            val builder: AlertDialog.Builder = AlertDialog.Builder(this@FoodScanner)
                            builder
                                .setTitle("Do you want to add $barcodeValue to the List?")
                                .setPositiveButton("Yes") { dialog, which ->
                                    barcodeList.add(barcodeValue)
                                    barcodeAdapter.notifyItemInserted(barcodeList.size - 1)
                                    readyToDetect = true
                                }
                                .setNegativeButton("No") { dialog, which ->
                                    readyToDetect = true
                                }
                            val dialog: AlertDialog = builder.create()
                            dialog.show()
                        } else {
                            val alertDialog = AlertDialog.Builder(this@FoodScanner).create()
                            alertDialog.setTitle("Item already in List")
                            alertDialog.setMessage("You have already scanned that item")
                            alertDialog.setButton(
                                AlertDialog.BUTTON_NEUTRAL, "OK"
                            ) { dialog, which ->
                                dialog.dismiss()
                                readyToDetect = true
                            }
                            alertDialog.show()
                        }

                    }
                }
            }
        })
    }
    private fun startCameraSource() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            cameraSource?.start(cameraView?.holder)
            Log.d("FoodScanner", "Camera source started.")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("FoodScanner", "Error starting camera source.", e)
        }
    }

    fun processBarcode(view: View) {

    }
}