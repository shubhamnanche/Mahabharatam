package com.ssk.mahabharatam.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.mahabharatam.source.Book
import com.ssk.mahabharatam.source.BookFactory
import com.ssk.mahabharatam.source.Mahabharatam
import com.ssk.mahabharatam.ui.theme.MahabharatamTheme
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class BookDetails : ComponentActivity() {

    @Inject
    lateinit var bookFactory: BookFactory
    private lateinit var book: Book

    private var bookNumber by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        bookNumber = intent.getIntExtra("book_number", 1)
        book = bookFactory.createBook(Mahabharatam.getBookNames()[bookNumber - 1])

        setContent {
            BookDetailsScreen(book, bookNumber)
        }
    }

    @Composable
    fun BookDetailsScreen(book: Book, bookNumber: Int) {
        MahabharatamTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Surface(modifier = Modifier.padding(innerPadding)) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .semantics { isTraversalGroup = true }
                    ) {
                        LazyVerticalGrid(
                            contentPadding = PaddingValues(10.dp),
                            columns = GridCells.Fixed(2)
                        ) {
                            val verses = book.getVerses()
                            val chapters = verses[verses.size - 1].chapter
                            items(chapters) {
                                val chapter = it + 1
                                ItemCard(
                                    "Chapter $chapter",
                                    Modifier
                                        .fillMaxSize()
                                        .padding(2.dp)
                                        .align(Alignment.Center)
                                        .clip(RoundedCornerShape(20.dp))
                                        .clickable(true) {},
                                    Modifier
                                        .fillMaxSize()
                                        .align(Alignment.Center)
                                        .padding(0.dp, 20.dp),
                                ) {

                                }

                            }
                        }
                    }
                }

            }
        }
    }

    @Composable
    private fun ItemCard(
        text: String,
        cardModifier: Modifier = Modifier.fillMaxSize(),
        textModifier: Modifier = Modifier.fillMaxSize(),
        onClick: () -> Unit
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(),
            onClick = onClick,
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
            colors = CardDefaults.outlinedCardColors(),
            modifier = cardModifier,
        ) {
            Text(text, textAlign = TextAlign.Center, modifier = textModifier)
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun BookDetailsPreview() {
//    BookDetailsScreen( )
    }

}