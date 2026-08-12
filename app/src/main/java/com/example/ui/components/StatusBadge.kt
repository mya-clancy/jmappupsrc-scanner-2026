package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberPendingBg
import com.example.ui.theme.AmberPendingText
import com.example.ui.theme.CoralExpiredBg
import com.example.ui.theme.CoralExpiredText
import com.example.ui.theme.MintValidBg
import com.example.ui.theme.MintValidText

@Composable
fun StatusBadge(status: String, modifier: Modifier = Modifier) {
    val (bgColor, textColor, icon, label) = when (status.uppercase()) {
        "VALID", "ACTIVE" -> Quadruple(
            MintValidBg,
            MintValidText,
            Icons.Default.CheckCircle,
            "ACTIVE"
        )
        "EXPIRED_SEMESTER", "EXPIRED" -> Quadruple(
            CoralExpiredBg,
            CoralExpiredText,
            Icons.Default.HourglassTop,
            "EXPIRED"
        )
        "PAYMENT_PENDING", "PENDING", "UNPAID" -> Quadruple(
            AmberPendingBg,
            AmberPendingText,
            Icons.Default.Error,
            "UNPAID"
        )
        else -> Quadruple(
            CoralExpiredBg,
            CoralExpiredText,
            Icons.Default.Cancel,
            "INVALID"
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = textColor,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                color = textColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

