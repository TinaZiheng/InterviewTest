package com.ai.libraryapp

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ai.libraryapp.data.bean.Book
import com.ai.libraryapp.ui.theme.LibraryAppTheme
import com.ai.libraryapp.ui.viewmodel.CreateBookViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateBookActivity : ComponentActivity() {
    private val viewModel: CreateBookViewModel by viewModels()
    private var book: Book? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookEditor()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            book = intent.getParcelableExtra("Book", Book::class.java)
        } else {
            book = intent.getParcelableExtra<Book>("Book")
        }
        viewModel.updateAndRememberBook(book ?: Book(id = -1, name = "", remark = ""))

        viewModel.toastMsg.observe(this) {
            if(it.isNotEmpty()) {
                Toast.makeText(this, it,  Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.finishActivity.observe(this) { shouldFinish ->
            if (shouldFinish) {
                finish()
            }
        }
    }
}

@Composable
fun BookEditor(viewModel: CreateBookViewModel = hiltViewModel()) {
    var book by remember { mutableStateOf(viewModel.getRememberBook() ?: Book(0, "", "")) }
    val loading by viewModel.loading.observeAsState(false)
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.padding(16.dp)) {
        // Editable name field
        OutlinedTextField(
            value = book.name,
            onValueChange = { newName ->
                book = book.copy(name = newName)
                viewModel.updateAndRememberBook(book)
            },
            label = { Text("Name") },
            modifier = Modifier.padding(vertical = 8.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                // Focus next field when "Next" is pressed
                focusManager.moveFocus(FocusDirection.Down)
            })
        )

        // Editable remark field
        OutlinedTextField(
            value = book.remark,
            onValueChange = { newRemark ->
                book = book.copy(remark = newRemark)
                viewModel.updateAndRememberBook(book)
            },
            label = { Text("Remark") },
            modifier = Modifier.padding(vertical = 8.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                // Hide keyboard when "Done" is pressed
                focusManager.clearFocus()
            })
        )
        
        Button(modifier = Modifier.height(40.dp).width(200.dp), onClick = {
            viewModel.submitUpdate()
            focusManager.clearFocus()
        }) {
            Text(text = "Submit")
        }

        LoadingDialog(isLoading = loading, "Submitting")
    }
}

@Preview(showBackground = true)
@Composable
fun CreateBookPreview() {
    LibraryAppTheme {
        BookEditor()
    }
}