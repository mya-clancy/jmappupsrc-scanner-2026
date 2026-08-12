package com.example.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.model.Student
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object JpgCardExporter {

    fun createMembershipCardBitmap(student: Student): Bitmap {
        val width = 1000
        val height = 630 // Standard ID Card Aspect Ratio 1.588

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Card Background: Dark Maroon / Red Card
        val cardRect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        val cardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#260808") // Deep Maroon Dark
        }
        canvas.drawRoundRect(cardRect, 32f, 32f, cardPaint)

        // Top Accent Banner Gradient (PUP Red to Deep Maroon with Gold Accent Line)
        val headerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, width.toFloat(), 0f,
                Color.parseColor("#800000"), Color.parseColor("#5A0000"),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(RectF(0f, 0f, width.toFloat(), 135f), 32f, 32f, headerPaint)
        canvas.drawRect(RectF(0f, 80f, width.toFloat(), 135f), headerPaint)

        // Gold Accent Divider Line below banner
        val goldLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#D4AF37") // PUP Gold
            strokeWidth = 6f
        }
        canvas.drawLine(0f, 135f, width.toFloat(), 135f, goldLinePaint)

        // Header Titles (JMAP - PUP Santa Rosa)
        val headerTitlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#FFD700") // Gold Text
            textSize = 32f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText("JUNIOR MARKETING ASSOCIATION OF THE PHILIPPINES", 40f, 60f, headerTitlePaint)

        val headerSubPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 22f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText("PUP SANTA ROSA • OFFICIAL MEMBER PASS", 40f, 105f, headerSubPaint)

        // Student Details Text
        val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#D4AF37") // Gold Labels
            textSize = 19f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val valuePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 28f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        var startY = 195f
        val startX = 45f

        // Name
        canvas.drawText("MEMBER NAME", startX, startY, labelPaint)
        startY += 36f
        canvas.drawText(student.fullName, startX, startY, valuePaint)

        // Student ID
        startY += 52f
        canvas.drawText("STUDENT ID", startX, startY, labelPaint)
        startY += 36f
        canvas.drawText(student.studentId, startX, startY, valuePaint)

        // Program / Major (BSBA - Marketing Management)
        startY += 52f
        canvas.drawText("PROGRAM", startX, startY, labelPaint)
        startY += 36f
        val deptText = "BSBA - Marketing Management"
        canvas.drawText(deptText, startX, startY, valuePaint)

        // Semester & Status Badge
        startY += 52f
        canvas.drawText("VALID SEMESTER", startX, startY, labelPaint)
        startY += 36f

        // Badge Container
        val badgeColor = if (student.isMembershipPaid) Color.parseColor("#1C3E2A") else Color.parseColor("#800000")
        val badgeBorderColor = if (student.isMembershipPaid) Color.parseColor("#34D399") else Color.parseColor("#FFC107")
        val badgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = badgeColor
        }
        val badgeText = "${student.activeSemester} • ${if (student.isMembershipPaid) "VALID MEMBER" else "UNPAID/EXPIRED"}"
        val badgeTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 21f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val textWidth = badgeTextPaint.measureText(badgeText)
        val badgeRect = RectF(startX, startY - 28f, startX + textWidth + 28f, startY + 14f)
        canvas.drawRoundRect(badgeRect, 10f, 10f, badgePaint)
        
        val badgeBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = badgeBorderColor
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }
        canvas.drawRoundRect(badgeRect, 10f, 10f, badgeBorderPaint)
        canvas.drawText(badgeText, startX + 14f, startY, badgeTextPaint)

        // QR Code Container on Right
        val qrSize = 330
        val qrX = width - qrSize - 50
        val qrY = 170

        // White & Gold Border Box for QR Code
        val qrBgRect = RectF(qrX.toFloat() - 12f, qrY.toFloat() - 12f, (qrX + qrSize).toFloat() + 12f, (qrY + qrSize).toFloat() + 12f)
        val qrBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
        }
        canvas.drawRoundRect(qrBgRect, 16f, 16f, qrBgPaint)
        
        val qrBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#D4AF37")
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }
        canvas.drawRoundRect(qrBgRect, 16f, 16f, qrBorderPaint)

        // Generate UNIQUE QR Code containing student verification string
        val qrPayload = "{\"org\":\"JMAP-PUP-SR\",\"studentId\":\"${student.studentId}\",\"fullName\":\"${student.fullName}\",\"semester\":\"${student.activeSemester}\"}"
        val qrBitmap = QrCodeGenerator.generateQrBitmap(
            content = qrPayload,
            width = qrSize,
            height = qrSize,
            foregroundColor = Color.parseColor("#800000"), // Maroon QR Code pattern!
            backgroundColor = Color.WHITE
        )

        if (qrBitmap != null) {
            canvas.drawBitmap(qrBitmap, qrX.toFloat(), qrY.toFloat(), null)
        }

        // QR Code Label
        val qrLabelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#FFD700")
            textSize = 18f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText("SCAN TO VALIDATE", qrX.toFloat() + 60f, (qrY + qrSize + 40).toFloat(), qrLabelPaint)

        // Bottom Footer line
        val footerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#5A0000")
            strokeWidth = 2f
        }
        canvas.drawLine(40f, height - 48f, width - 40f, height - 48f, footerPaint)

        val footerTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#E0D0D0")
            textSize = 16f
        }
        canvas.drawText("Polytechnic University of the Philippines • JMAP - PUP Santa Rosa Branch", 40f, height - 18f, footerTextPaint)

        return bitmap
    }

    fun exportAndShareJpgCard(context: Context, student: Student) {
        try {
            val bitmap = createMembershipCardBitmap(student)
            val fileName = "Card_${student.studentId}_${System.currentTimeMillis()}.jpg"

            val file = File(context.cacheDir, fileName)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)
            outputStream.flush()
            outputStream.close()

            // Save copy to MediaStore Pictures gallery if possible
            saveToGallery(context, bitmap, "Card_${student.studentId}")

            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                putExtra(Intent.EXTRA_SUBJECT, "Membership Card - ${student.fullName}")
                putExtra(Intent.EXTRA_TEXT, "Here is the official membership card for ${student.fullName} (${student.studentId}).")
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(shareIntent, "Share Membership Card JPG"))
            Toast.makeText(context, "Card saved & ready to share!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to export JPG card: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveToGallery(context: Context, bitmap: Bitmap, title: String) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "$title.jpg")
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/OrgMembershipCards")
                }
                val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                if (imageUri != null) {
                    val stream: OutputStream? = resolver.openOutputStream(imageUri)
                    if (stream != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)
                        stream.close()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
