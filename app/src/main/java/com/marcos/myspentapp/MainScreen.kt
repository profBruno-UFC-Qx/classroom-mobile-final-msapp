package com.marcos.myspentapp

import com.marcos.myspentapp.ui.viewmodel.CashViewModel
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.marcos.myspentapp.ui.database.CardData
import com.marcos.myspentapp.ui.state.CardUiState
import com.marcos.myspentapp.ui.state.TypeGasto
import com.marcos.myspentapp.ui.viewmodel.CardViewModel
import com.marcos.myspentapp.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(cashViewModel: CashViewModel) {

    var expanded by remember { mutableStateOf(false) }

    // Coletar valor na moeda selecionada
    val selectedCurrency by cashViewModel.selectedCurrency.collectAsState()

    TopAppBar(
        expandedHeight = 30.dp,
        modifier = Modifier,
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "MySpent",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 10.dp)
                )

            // Menu de seleção de moeda
                // Mostra cifra da moeda selecionada
                val currencySymbol = when (selectedCurrency) {
                    "BRL" -> "R$"
                    "USD" -> "$"
                    "EUR" -> "€"
                    "GBP" -> "£"
                    "JPY" -> "¥"
                    else -> selectedCurrency
                }

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable { expanded = true }
                ) {
                    Text(
                        currencySymbol,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 2.dp)
                    )

                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp),
                    )

                    // Escolher moeda
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        containerColor = MaterialTheme.colorScheme.surface,
                        offset = DpOffset((-10).dp, 0.dp),
                        modifier = Modifier.width(50.dp)
                    ) {
                        listOf("BRL", "USD", "EUR", "GBP", "JPY").forEach { currency ->
                            val symbol = when (currency) {
                                "BRL" -> "R$"
                                "USD" -> "$"
                                "EUR" -> "€"
                                "GBP" -> "£"
                                "JPY" -> "¥"
                                else -> ""
                            }
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        symbol,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                onClick = {
                                    expanded = false
                                    cashViewModel.setCurrency(currency)
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MySpentApp(
    modifier: Modifier = Modifier,
    cashViewModel: CashViewModel,
    cardViewModel: CardViewModel,
    userViewModel: UserViewModel
) {
    val cards by cardViewModel.cards.collectAsState()

    var showDialogCash by remember { mutableStateOf(false) }

    // valor original em BRL
    val ganhosOriginal = userViewModel.userState.ganhos

    // soma dos gastos em BRL
    val totalGastosOriginal = cards.sumOf { it.value }

    // observa API
    val currency by cashViewModel.selectedCurrency.collectAsState()
    val rates by cashViewModel.rates.collectAsState()

    // conversão
    val ganhosConvertidos = remember(ganhosOriginal, currency, rates) {
        cashViewModel.convertValue(ganhosOriginal, currency, rates)
    }

    val gastosConvertidos = remember(totalGastosOriginal, currency, rates) {
        cashViewModel.convertValue(totalGastosOriginal, currency, rates)
    }

    // formatados
    val ganhosFormatados = cashViewModel.formatCurrency(ganhosConvertidos)
    val gastosFormatados = cashViewModel.formatCurrency(gastosConvertidos)

    // Layout principal
    // TopBar -> Surface -> Lista/Gráfico -> BottomBar
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { TopBar(cashViewModel) }
    ) {
        Column(
            modifier = Modifier
                .padding(top = 70.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val orient = (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE)

            val tamHeight = if (orient) 0.6f else 0.18f
            val tamWidth = if (orient) 0.75f else 0.9f

            Spacer(modifier.height(22.dp))

            // Ganhos <--> Total de gastos
            Surface(
                modifier = modifier
                    .padding(2.dp)
                    .fillMaxWidth(tamWidth)
                    .clickable { showDialogCash = true }
                    .fillMaxHeight(tamHeight),
                tonalElevation = 30.dp,
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Ganhos convertidos
                    Column(
                        modifier = Modifier.weight(1f).padding(start = 20.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text("Entrada", fontSize = 16.sp)
                        Spacer(Modifier.height(10.dp))
                        Text(
                            ganhosFormatados,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    VerticalDivider(
                        Modifier.fillMaxHeight(0.7f).width(0.1f.dp),
                        thickness = 2.dp,
                        color = Color.Gray
                    )

                    // Gastos convertidos e somados
                    Column(
                        modifier = Modifier.weight(1f).padding(end = 20.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text("Gastos", fontSize = 16.sp)
                        Spacer(Modifier.height(10.dp))
                        Text(
                            gastosFormatados,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier.height(22.dp))

            // Estado do menu
            val pagerState = rememberPagerState(initialPage = 0) { 2 }
            val scope = rememberCoroutineScope()

            // Lista -- Gráfico
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = pagerState.currentPage == 0,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    },
                    text = { Text("Lista") }
                )

                Tab(
                    selected = pagerState.currentPage == 1,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                    text = { Text("Gráfico") }
                )
            }

            Spacer(Modifier.height(16.dp))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                key = { page -> cardViewModel.cards.value.hashCode() to page }
            ) { page ->
                when (page) {
                    0 -> {
                        ListaDeGastos(
                            cardViewModel = cardViewModel,
                            cashViewModel = cashViewModel,
                            userViewModel = userViewModel
                        )
                    }

                    1 -> {
                        SectorBalanco(
                            ganhos = ganhosConvertidos,
                            gastos = gastosConvertidos,
                            modifier = Modifier.padding(top = 24.dp)
                        )
                    }
                }
            }
        }

        // Dialog para adicionar ganhos
        if (showDialogCash) {
            Dialog( onDismissRequest = { showDialogCash = false } ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier.fillMaxWidth(0.9f).height(250.dp)
                ) {
                    DetalheInOut(
                        cashIn = "",
                        userViewModel = userViewModel,
                        onFechar = { showDialogCash = false }
                    )
                }
            }
        }
    }
}



@Composable
fun CardGasto(
    card: CardUiState,
    modifier: Modifier,
    cashViewModel: CashViewModel,
) {
    val context = LocalContext.current

    val currency by cashViewModel.selectedCurrency.collectAsState()
    val rates by cashViewModel.rates.collectAsState()

    val convertedValue = remember(card.value, currency, rates) {
        cashViewModel.convertValue(card.value, currency, rates)
    }
    val formatted = cashViewModel.formatCurrency(convertedValue)

    // Selecionar foto do card
    val bitmap: ImageBitmap? = remember(card.imageUri) {
        card.imageUri?.let { uri ->
            try {
                if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(
                        context.contentResolver,
                        uri
                    ).asImageBitmap()
                } else {
                    val source = ImageDecoder.createSource(
                        context.contentResolver,
                        uri
                    )
                    ImageDecoder.decodeBitmap(source).asImageBitmap()
                }
            } catch (_: Exception) {
                null
            }
        }
    }

    val modifierBase = modifier
        .width(140.dp)
        .height(140.dp)

    // Layout do card
    Card(
        modifier = modifierBase,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            if (bitmap != null) {
                Image(
                    bitmap = bitmap,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.ms1),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, top = 10.dp, end = 10.dp)
            ) {
                Text(text = card.title.uppercase(), fontSize = 16.sp)
                Text(text = formatted, fontSize = 16.sp)
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "FrequentlyChangingValue")
@Composable
fun ListaDeGastos(
    cardViewModel: CardViewModel,
    cashViewModel: CashViewModel,
    userViewModel: UserViewModel
) {
    val gastos by cardViewModel.cards.collectAsState()
    val selectedIds by cardViewModel.selectedIds.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val editingCard by cardViewModel.editingCard.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        cardViewModel.loadCards(
            context = context,
            userId = userViewModel.userState.email
        )
    }

    // Layout Lista
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            if (selectedIds.isNotEmpty()) {
                FloatingActionButton(
                    onClick = {
                        cardViewModel.removeSelected(
                            context = context,
                            userEmail = userViewModel.userState.email
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remover cards",
                        tint = Color.White
                    )
                }
            } else {
                FloatingActionButton(
                    onClick = { showDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Adicionar card",
                        tint = Color.White
                    )
                }
            }
        }
    ) {


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {

            // Aparência da lista (vazia ou não)
            if (gastos.isEmpty()) {
                Text(
                    text = "Nenhum gasto adicionado ainda.",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                val gridState = rememberLazyGridState()
                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 24.dp, end = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(1.dp)
                ) {

                    // Organização dos cards
                    itemsIndexed(
                        items = gastos,
                        key = { _, item -> item.id }
                    ) { index, gasto ->

                        val itemInfo = gridState.layoutInfo.visibleItemsInfo
                            .firstOrNull { it.index == index }

                        val isSelected = selectedIds.contains(gasto.id)

                        val modifierBase = Modifier
                            .border(
                                width = if (isSelected) 1.5.dp else 0.dp,
                                color = if (isSelected)
                                    MaterialTheme.colorScheme.error
                                else Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .combinedClickable(
                                onClick = {
                                    if (selectedIds.isEmpty()) {
                                        cardViewModel.openEdit(gasto)
                                    } else {
                                        cardViewModel.toggleSelection(gasto.id)
                                    }
                                },
                                onLongClick = {
                                    cardViewModel.toggleSelection(gasto.id)
                                }
                            )


                        // ENTRADA
                        if (itemInfo == null) {
                            CardGasto(
                                card = gasto,
                                cashViewModel = cashViewModel,
                                modifier = modifierBase.graphicsLayer {
                                    alpha = 0f
                                    scaleX = 0.85f
                                    scaleY = 0.85f
                                }
                            )
                            return@itemsIndexed
                        }

                        val viewportTop = gridState.layoutInfo.viewportStartOffset
                        val viewportBottom = gridState.layoutInfo.viewportEndOffset

                        val itemTop = itemInfo.offset.y
                        val itemBottom = itemTop + itemInfo.size.height

                        val fadeOutDistance = 200f

                        val topExit = (viewportTop - itemTop).toFloat()
                        val bottomExit = (itemBottom - viewportBottom).toFloat()

                        val exitAmount = maxOf(topExit, bottomExit, 0f)
                        val progress = (exitAmount / fadeOutDistance).coerceIn(0f, 1f)

                        val alpha = 1f - progress
                        val scale = 1f - (progress * 0.15f)

                        CardGasto(
                            card = gasto,
                            cashViewModel = cashViewModel,
                            modifier = modifierBase.graphicsLayer {
                                this.alpha = alpha
                                scaleX = scale
                                scaleY = scale
                            }
                        )
                    }
                }
            }
        }
    }

    // Dialog para adicionar card
    if (showDialog) {

        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(600.dp)
            ) {
                DetalheGasto(
                    imageRes = R.drawable.ms1,
                    imageUri = null,
                    title = "",
                    value = "",
                    tipo = TypeGasto.NONE,
                    onFechar = { showDialog = false }
                ) { imageUri, title, value, type ->

                    cardViewModel.addCard(
                        context = context,
                        card = CardData(
                            imageUri = imageUri.toString(),
                            title = title,
                            value = value.toDoubleOrNull() ?: 0.0,
                            type = type.name,
                            userEmail = userViewModel.userState.email,
                        ),
                        onResult = { showDialog = false }
                    )
                }
            }
        }
    }

    // Dialog para editar card
    if (editingCard != null) {
        Dialog(onDismissRequest = { cardViewModel.closeEdit() }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(600.dp)
            ) {
                DetalheGasto(
                    imageRes = R.drawable.ms1,
                    imageUri = editingCard!!.imageUri,
                    title = editingCard!!.title,
                    value = editingCard!!.value.toString(),
                    tipo = editingCard!!.type,
                    onFechar = { cardViewModel.closeEdit() }
                ) { newImage, newTitle, newValue, newType->

                    cardViewModel.updateCardInBase(
                        context = context,
                        updatedCard = CardData(
                        id = editingCard!!.id,
                        userEmail = userViewModel.userState.email,
                        imageUri = newImage.toString(),
                        title = newTitle,
                        value = newValue.toDoubleOrNull() ?: 0.0,
                        type = newType.name,
                        ),
                        onResult = { cardViewModel.closeEdit() }
                    )

                }
            }
        }
    }
}





