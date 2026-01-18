package com.marcos.myspentapp.ui.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marcos.myspentapp.ui.theme.colorLogo1
import com.marcos.myspentapp.ui.theme.colorNegativo

@Composable
fun SectorBalanco(
    ganhos: Double,
    gastos: Double,
    modifier: Modifier = Modifier
) {
    val total = ganhos + gastos
    if (total <= 0) return

    // Normaliza os valores
    val maxValue = maxOf(ganhos, gastos).coerceAtLeast(1.0)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "Balanço Financeiro",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {

            // Barra de Ganhos
            BarColumn(
                value = ganhos,
                maxValue = maxValue,
                color = colorLogo1
            )

            // Barra de Gastos
            BarColumn(
                value = gastos,
                maxValue = maxValue,
                color = colorNegativo
            )
        }

        Spacer(Modifier.height(16.dp))

        // LEGENDA
        Row(horizontalArrangement = Arrangement.spacedBy(30.dp)) {
            LegendItem(colorLogo1, "Ganhos")
            LegendItem(colorNegativo, "Gastos")
        }
    }
}

@Composable
fun BarColumn(
    value: Double,
    maxValue: Double,
    color: Color
) {
    val proportion = (value / maxValue).toFloat().coerceIn(0f, 1f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight(fraction = proportion)
                .background(color, shape = RoundedCornerShape(6.dp))
        )
    }
}



@Composable
fun SectorGastos(
    gastosPorTipo: Map<String, Double>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val total = gastosPorTipo.values.sum()
    if (total <= 0) return

    val strokeWidth = 60.dp

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "Distribuição de Gastos",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Canvas(
            modifier = Modifier.size(280.dp)
        ) {
            val strokePx = strokeWidth.toPx()
            val radiusOffset = strokePx / 2

            val arcSize = size.minDimension - strokePx
            val topLeft = Offset(radiusOffset, radiusOffset)

            var startAngle = -90f

            gastosPorTipo.entries.forEachIndexed { index, entry ->
                val percentage = (entry.value / total * 360f).toFloat()
                val color = colors.getOrElse(index) { Color.Gray }

                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = percentage,
                    useCenter = false,
                    topLeft = topLeft,
                    size = Size(arcSize, arcSize),
                    style = Stroke(width = strokePx)
                )

                startAngle += percentage
            }
        }

        Spacer(Modifier.height(16.dp))

        // LEGENDA
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            gastosPorTipo.entries.forEachIndexed { index, entry ->
                val color = colors.getOrElse(index) { Color.Gray }
                LegendItem(color, "${entry.key} — R$ %.2f".format(entry.value))
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(color, CircleShape)
        )
        Spacer(Modifier.width(8.dp))
        Text(text, fontSize = 14.sp)
    }
}