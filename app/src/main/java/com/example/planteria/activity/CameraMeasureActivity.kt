package com.example.planteria.activity


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.planteria.databinding.ActivityCameraMeasureBinding
import org.opencv.android.OpenCVLoader
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import java.util.concurrent.Executors


class CameraMeasureActivity : AppCompatActivity() {

    lateinit var binding: ActivityCameraMeasureBinding
    private val cameraExecutor = Executors.newSingleThreadExecutor()
    private val CAMERA_PERMISSION_REQUEST_CODE = 1001

    private val sensorHeight = 4.8 // Sensor height in millimeters
    private val focalLength = 4.3 // Focal length in millimeters

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraMeasureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Permissions granted, proceed with camera initialization
            if (OpenCVLoader.initDebug()) {
                startCamera()
            } else {
                // Handle OpenCV initialization failure
            }
        } else {
            // Request camera permission from the user
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    // Other methods from the provided code go here...

    // Handle the result of the permission request
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, proceed with camera initialization
                if (OpenCVLoader.initDebug()) {
                    startCamera()
                } else {
                    // Handle OpenCV initialization failure
                }
            } else {
                // Camera permission denied, handle accordingly (e.g., show a message, disable camera features)
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                val inputMat = imageProxyToMat(imageProxy)
                val objectHeightCm = measureObjectHeight(inputMat)

                // Display or process the objectHeightCm as needed

                imageProxy.close()
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalysis
                )

            } catch (e: Exception) {
                // Handle camera setup failure
            }
        }, applicationContext.mainExecutor)
    }

    private fun imageProxyToMat(imageProxy: ImageProxy): Mat {
        val buffer = imageProxy.planes[0].buffer
        val mat = Mat(imageProxy.height + imageProxy.height / 2, imageProxy.width, CvType.CV_8UC1, buffer)
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_YUV2RGBA_NV21, 4)
        return mat
    }

    private fun measureObjectHeight(inputMat: Mat): Double {
        // Convert the image to grayscale
        val grayMat = Mat()
        Imgproc.cvtColor(inputMat, grayMat, Imgproc.COLOR_RGBA2GRAY)

        // Apply Canny edge detection
        val edgesMat = Mat()
        Imgproc.Canny(grayMat, edgesMat, 50.0, 150.0)

        // Find contours in the edges
        val contours: List<MatOfPoint> = ArrayList()
        val hierarchy = Mat()
        Imgproc.findContours(edgesMat, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)

        // Sort contours by area and find the largest contour
        val sortedContours = contours.sortedByDescending { Imgproc.contourArea(it) }
        val largestContour = sortedContours.firstOrNull()

        // Check if the largestContour is null
        if (largestContour == null) {
                Log.e("ObjectHeight", "No contour found")
            return 0.0 // You might want to handle this case differently based on your requirements
        }

        // Get the bounding box of the largest contour
        val boundingRect = Imgproc.boundingRect(largestContour)

        // Draw the bounding box on the original image (for visualization)
        Imgproc.rectangle(inputMat, boundingRect.tl(), boundingRect.br(), Scalar(0.0, 255.0, 0.0), 2)

        // Calculate the height in pixels
        val objectHeightPixels = boundingRect.height.toDouble()

        // Convert pixels to centimeters using camera calibration parameters
        return pixelsToCentimeters(objectHeightPixels)
    }


    private fun pixelsToCentimeters(pixels: Double): Double {
        // Calculate the distance from the camera to the object using similar triangles
        val distance = (focalLength * sensorHeight) / pixels
        // Convert distance to centimeters
        return distance / 10.0
    }
}