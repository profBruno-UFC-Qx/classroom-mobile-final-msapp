package com.marcos.myspentapp.ui.composable

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marcos.myspentapp.data.models.TypeGasto
import com.marcos.myspentapp.ui.theme.colorText
import com.marcos.myspentapp.ui.viewmodel.UserViewModel
import com.marcos.myspentapp.ui.viewmodel.UserViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalheGasto(
    imageRes: Int,
    imageUri: Uri?,
    title: String,
    tipo: String,
    value: String,
    onFechar: () -> Unit,
    onSalvar: (Uri?, String, String, TypeGasto) -> Unit
) {
    val context = LocalContext.current

    var currentImageUri by remember { mutableStateOf(imageUri) }
    var currentTitle by remember { mutableStateOf(title) }
    var currentValue by remember { mutableStateOf(value) }
    var expanded by remember { mutableStateOf(false) }
    var selectedTipo by remember { mutableStateOf(tipo) }


    val launch = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uriSelecionada: Uri? ->
        uriSelecionada?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            currentImageUri = uriSelecionada
        }
    }


    val bitmap: ImageBitmap? = remember(currentImageUri) {
        currentImageUri?.let { uri ->
            try {
                if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri).asImageBitmap()
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source).asImageBitmap()
                }
            } catch (_: Exception) {
                null
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Detalhes do Gasto",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (bitmap != null) {
                Image(
                    bitmap = bitmap,
                    contentDescription = "Imagem do gasto",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(180.dp)
                        .clickable { launch.launch(arrayOf("image/*")) }
                )
            } else {
                Image(
                    painter = painterResource(imageRes),
                    contentDescription = "Selecionar imagem",
                    modifier = Modifier
                        .size(180.dp)
                        .clickable { launch.launch(arrayOf("image/*")) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = currentTitle,
                onValueChange = { currentTitle = it },
                label = { Text("Título do gasto") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF827E7D),
                    focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedLabelColor = Color(0xFF827E7D),
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = Color(0xFF827E7D),
                    cursorColor = Color(0xFF827E7D)
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ---- DROPDOWN COM VISUAL DE OUTLINEDTEXTFIELD ----
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedTipo,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo do gasto") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF827E7D),
                        focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedLabelColor = Color(0xFF827E7D),
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = Color(0xFF827E7D),
                        cursorColor = Color(0xFF827E7D)
                    ),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    TypeGasto.entries.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.name) },
                            onClick = {
                                selectedTipo = option.name
                                expanded = false
                            }
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = currentValue,
                onValueChange = { currentValue = it },
                label = { Text("Valor em R$") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Decimal
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF827E7D),
                    focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedLabelColor = Color(0xFF827E7D),
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = Color(0xFF827E7D),
                    cursorColor = Color(0xFF827E7D)
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = onFechar,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text("Cancelar")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    onSalvar(currentImageUri, currentTitle, currentValue, TypeGasto.valueOf(selectedTipo))
                    onFechar()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text("Salvar")
            }
        }
    }
}

@Composable
fun DetalheInOut(
    cashIn: String,
    userViewModel: UserViewModel,
    onFechar: () -> Unit
) {

    var currentIn by remember { mutableStateOf(cashIn) }

    // Adicionar ganhos ao saldo
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Ganhos do mês",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = currentIn,
                onValueChange = { currentIn = it },
                label = { Text("Entrada em R$") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF827E7D),
                    focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedLabelColor = Color(0xFF827E7D),
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = Color(0xFF827E7D),
                    cursorColor = Color(0xFF827E7D)
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

        }

        // Botões de ação
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = onFechar,
                modifier = Modifier
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text("Cancelar")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    userViewModel.updateGanhos(currentIn.toDoubleOrNull() ?: 0.00)
                    userViewModel.updateUser(
                        userViewModel.userState.email,
                        userViewModel.userState.email,
                        userViewModel.userState.nome,
                        userViewModel.userState.senha,
                        userViewModel.userState.fotoUri.toString(),
                        userViewModel.userState.codeRescue,
                        currentIn.toDoubleOrNull() ?: 0.00,
                        userViewModel.userState.darkTheme,
                        userViewModel.userState.initApp
                        )
                    onFechar()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text("Salvar")
            }
        }
    }
}

@Composable
fun BottomBar(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .drawBehind {
                drawLine(
                    color = colorText.copy(alpha = 0.4f),
                    start = Offset(0f, 0f), // topo
                    end = Offset(size.width, 0f),
                    strokeWidth = 8f
                )
            }
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxSize()
        ) {

            // MainScreen
            NavigationBarItem(
                selected = selectedItem == 0,
                onClick = { },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Início",
                        modifier = Modifier.clickable(onClick = { onItemSelected(0) })
                    )
                },
                label = { Text("Início") },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.background,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                )
            )

            // PerfilScreen
            NavigationBarItem(
                selected = selectedItem == 1,
                onClick = { onItemSelected(1) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil"
                    )
                },
                label = { Text("Perfil") },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.background,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                )
            )
        }
    }
}

