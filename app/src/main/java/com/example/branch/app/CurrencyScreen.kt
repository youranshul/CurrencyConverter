package com.example.branch.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.branch.domain.Currency


@Composable
fun CurrencyScreen(
    currencyPayVm: CurrencyPayViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(5.dp)
    ) {
        val data by currencyPayVm.currencies.collectAsState()
        val amount by currencyPayVm.amount.collectAsState()

        when (data) {
            is CurrencyScreenUiResult.Success -> {
                val currencyData = (data as CurrencyScreenUiResult.Success).currency
                ShowMainScreen(currencyData, amount, {
                    currencyPayVm.onAmountChanged(it)
                }) {
                    currencyPayVm.onCurrencySelected(it)
                }
            }

            CurrencyScreenUiResult.Loading -> {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun ShowMainScreen(
    currencyData: List<Currency>,
    amount: String,
    onAmountChanged: (amount: String) -> Unit,
    onCurrencyClicked: (code: String) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        OutlinedTextField(
            value = amount,
            onValueChange = {
                onAmountChanged(it)
            },
            label = { Text("Enter Amount") }
        )
        CurrencyDropDown(currencyData, onCurrencyClicked)
    }
    ShowAllCurrencies(currencyData)
}

@Composable
fun ShowAllCurrencies(data: List<Currency>?) {
    if (data == null) {
        return
    }

    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(5.dp)
            .verticalScroll(rememberScrollState())
    ) {
        data.forEach { currency ->
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(top = 5.dp)
                    .background(Color.LightGray)

            ) {
                Text(text = currency.Country, fontWeight = FontWeight.Bold)
                Text(text = currency.code)
                Text(text = currency.amount.toString())
            }
        }
    }

}

@Composable
fun CurrencyDropDown(data: List<Currency>, onCurrencyClicked: (code: String) -> Unit) {
    Box(
        Modifier
            .width(120.dp)
            .height(50.dp)
            .padding(start = 5.dp, end = 5.dp, top = 10.dp)
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        var code: String by remember { mutableStateOf("Select") }
        Text(text = code, color = Color.White)
        var expanded by remember { mutableStateOf(false) }
        // Back arrow here
        Row(Modifier.clickable { // Anchor view
            expanded = !expanded
        }) {
            // Anchor view
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                data.forEach {
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            onCurrencyClicked(it.code)
                            code = it.code
                        }
                    ) {
                        Text(text = it.code)
                    }
                }
            }
        }
    }
}
