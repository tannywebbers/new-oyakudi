package com.example.ui.screens.statement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CreditTransaction
import com.example.ui.components.AppHeader
import com.example.ui.theme.PixTextMuted
import com.example.ui.viewmodel.MainViewModel

@Composable
fun UsedCreditScreen(
    mainViewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    var history by remember { mutableStateOf<List<CreditTransaction>>(emptyList()) }

    LaunchedEffect(Unit) {
        history = mainViewModel.getUsedCreditHistory()
    }

    val entries = history.map { tx ->
        LedgerEntry(
            id = tx.id,
            title = tx.title,
            amount = tx.repaymentAmount.ifBlank { tx.formattedAmount },
            date = tx.dueDate.ifBlank { tx.date },
            status = tx.status
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LedgerBackground)
            .testTag("used_credit_screen")
    ) {
        AppHeader(
            title = stringResource(R.string.ledger_title),
            onBackClick = onBackClick
        )

        if (entries.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.ledger_empty),
                    color = PixTextMuted,
                    fontSize = 16.sp,
                    modifier = Modifier.testTag("empty_used_credit")
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(entries, key = { it.id }) { entry ->
                    LedgerRowCard(entry = entry, showRepayButton = false)
                }
            }
        }
    }
}
