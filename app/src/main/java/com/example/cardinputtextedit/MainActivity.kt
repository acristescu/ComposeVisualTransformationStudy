package com.example.cardinputtextedit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import com.example.cardinputtextedit.ui.theme.CardInputTextEditTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardInputTextEditTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Screen()
                }
            }
        }
    }
}

@Composable
fun Screen() {
    var cardNumber by rememberSaveable { mutableStateOf("") }
    Column {
        Row (modifier = Modifier.padding(all = 10.dp)) {
            Text(
                text = "Card number",
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            BasicTextField(
                value = cardNumber,
                onValueChange = { cardNumber = it.take(16) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = { creditCardFilter(it) }
            )
        }
        Box(modifier = Modifier
            .height(1.dp)
            .padding(start = 10.dp)
            .fillMaxWidth()
            .background(Color.Gray)
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Actual value:\n$cardNumber")
    }
}

val mask = "1234  5678  1234  5678"

fun creditCardFilter(text: AnnotatedString): TransformedText {
    val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text

    val annotatedString = AnnotatedString.Builder().run {
        for (i in trimmed.indices) {
            append(trimmed[i])
            if (i % 4 == 3 && i != 15) {
                append("  ")
            }
        }
        pushStyle(SpanStyle(color = Color.LightGray))
        append(mask.takeLast(mask.length - length))
        toAnnotatedString()
    }

    val creditCardOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 3) return offset
            if (offset <= 7) return offset + 2
            if (offset <= 11) return offset + 4
            if (offset <= 16) return offset + 6
            return 22
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 4) return offset
            if (offset <= 9) return offset - 2
            if (offset <= 14) return offset - 4
            if (offset <= 19) return offset - 6
            return 16
        }
    }

    return TransformedText(annotatedString, creditCardOffsetTranslator)
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CardInputTextEditTheme {
        Screen()
    }
}