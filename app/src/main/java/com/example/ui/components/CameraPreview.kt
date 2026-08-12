package com.example.ui.components

import android.annotation.SuppressLint
import android.graphics.ImageFormat
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.ui.theme.CyanLight
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import java.util.concurrent.Executors

@Composable
fun CameraPreview(
    onQrScanned: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }

                val executor = ContextCompat.getMainExecutor(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    val qrReader = MultiFormatReader()

                    imageAnalysis.setAnalyzer(
                        Executors.newSingleThreadExecutor()
                    ) { imageProxy ->
                        processImageProxy(qrReader, imageProxy, onQrScanned)
                    }

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalysis
                        )
                    } catch (e: Exception) {
                        Log.e("CameraPreview", "Camera binding failed: ${e.message}")
                    }
                }, executor)

                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        // Viewfinder reticle overlay
        QrScanOverlay()
    }
}

@Composable
fun QrScanOverlay() {
    val infiniteTransition = rememberInfiniteTransition(label = "scan_laser")
    val laserOffsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 240f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_pos"
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Transparent cutout frame
        Canvas(modifier = Modifier.fillMaxSize()) {
            val scanBoxSize = 250.dp.toPx()
            val left = (size.width - scanBoxSize) / 2
            val top = (size.height - scanBoxSize) / 2

            // Dim dark overlay around reticle
            drawRect(color = Color.Black.copy(alpha = 0.5f))

            // Clear box area
            drawRect(
                color = Color.Transparent,
                topLeft = Offset(left, top),
                size = Size(scanBoxSize, scanBoxSize)
            )

            // Cyan reticle border
            drawRoundRect(
                color = CyanLight,
                topLeft = Offset(left, top),
                size = Size(scanBoxSize, scanBoxSize),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx()),
                style = Stroke(width = 4.dp.toPx())
            )

            // Animated cyan laser line
            drawLine(
                color = CyanLight,
                start = Offset(left + 20.dp.toPx(), top + laserOffsetY.dp.toPx()),
                end = Offset(left + scanBoxSize - 20.dp.toPx(), top + laserOffsetY.dp.toPx()),
                strokeWidth = 3.dp.toPx()
            )
        }

        Box(
            modifier = Modifier
                .offset(y = 160.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Align Student QR Code within frame",
                color = Color.White,
                fontSize = 13.sp
            )
        }
    }
}

@SuppressLint("UnsafeOptInUsageError")
private fun processImageProxy(
    reader: MultiFormatReader,
    imageProxy: ImageProxy,
    onQrScanned: (String) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null && mediaImage.format == ImageFormat.YUV_420_888) {
        val buffer = mediaImage.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)

        val source = PlanarYUVLuminanceSource(
            bytes,
            mediaImage.width,
            mediaImage.height,
            0, 0,
            mediaImage.width,
            mediaImage.height,
            false
        )

        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
        try {
            val result = reader.decode(binaryBitmap)
            onQrScanned(result.text)
        } catch (e: NotFoundException) {
            // Frame did not contain a QR code
        } catch (e: Exception) {
            Log.e("CameraPreview", "QR decode error: ${e.message}")
        } finally {
            imageProxy.close()
        }
    } else {
        imageProxy.close()
    }
}
