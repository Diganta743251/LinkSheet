package fe.linksheet.composable.page.edit

import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.widget.addTextChangedListener
import fe.android.compose.system.rememberSystemService
import fe.composekit.component.page.SaneSettingsScaffold
import fe.linksheet.composable.util.BottomDrawer
import fe.linksheet.R


private val editorPadding = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextEditorPage(
    source: TextSource = TextSource.ClipboardCard,
    validator: TextValidator = WebUriTextValidator,
    initialText: String,
    onDone: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val view = LocalView.current
    val inputMethodManager = rememberSystemService<InputMethodManager>()

    var text by rememberSaveable { mutableStateOf(initialText) }
    val isValid = rememberSaveable(text) { validator.isValid(text) }
    var showEmojiPicker by rememberSaveable { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    SaneSettingsScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.navigationBars),
        topBar = {
            EditorAppBar(
                enabled = isValid,
                scrollBehavior = scrollBehavior,
                onDone = {
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                    onDone(text)
                },
                onDismiss = onDismiss
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Editor(
                source = source,
                initialText = text,
                onTextChanged = { text = it }
            )

            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                onClick = {
                    showEmojiPicker = true
                }
            ) {
                Icon(imageVector = Icons.Outlined.EmojiEmotions, contentDescription = null)
            }

            if (showEmojiPicker) {
                EmojiBottomSheet(
                    onSelect = { emoji ->
                        val editText = view.findFocus() as? EditText
                        if (editText != null) {
                            val start = editText.selectionStart.coerceAtLeast(0)
                            val end = editText.selectionEnd.coerceAtLeast(0)
                            val new = StringBuilder(editText.text.toString()).apply {
                                replace(start, end, emoji)
                            }.toString()
                            editText.setText(new)
                            val newPos = (start + emoji.length).coerceAtMost(new.length)
                            editText.setSelection(newPos)
                            onTextChanged(new)
                        }
                        showEmojiPicker = false
                    },
                    onDismiss = { showEmojiPicker = false }
                )
            }
        }
    }
}

@Composable
private fun EmojiBottomSheet(onSelect: (String) -> Unit, onDismiss: () -> Unit) {
    BottomDrawer(
        hide = onDismiss,
    ) {
        val emojis = listOf(
            "😀","😄","😁","😆","🥹","😊","😍","😎","🤩","🥳",
            "😇","🙂","😉","😌","🤗","🤭","🤫","🤔","🤨","😐",
            "😴","🤤","😪","😮","😯","😲","🥱","🤯","😳","🙄",
            "😢","😭","😤","😠","😡","🤬","🤒","🤕","🤧","🤮",
            "👍","👎","👏","🙌","🤝","🙏","💪","🫶","🫰","👌"
        )
        FlowRow(emojis, onSelect)
    }
}

@Composable
private fun FlowRow(items: List<String>, onSelect: (String) -> Unit) {
    val chunk = 6
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        for (row in items.chunked(chunk)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                for (emoji in row) {
                    AssistChip(onClick = { onSelect(emoji) }, label = { Text(emoji) })
                }
            }
        }
    }
}

const val EDITOR_APP_BAR_DONE_TEST_TAG = "editor_app_bar__done_test_tag"
const val EDITOR_APP_BAR_CANCEL_TEST_TAG = "editor_app_bar__cancel_test_tag"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditorAppBar(
    enabled: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    onDone: () -> Unit,
    onDismiss: () -> Unit,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        scrollBehavior = scrollBehavior,
        title = {},
        navigationIcon = {
            Row(
                modifier = Modifier.padding(horizontal = editorPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    modifier = Modifier.testTag(EDITOR_APP_BAR_DONE_TEST_TAG),
                    enabled = enabled, onClick = onDone
                ) {
                    Text(text = stringResource(id = R.string.generic__button_text_done))
                }

                TextButton(
                    modifier = Modifier.testTag(EDITOR_APP_BAR_CANCEL_TEST_TAG),
                    onClick = onDismiss
                ) {
                    Text(text = stringResource(id = R.string.generic__button_text_cancel))
                }
            }
        }
    )
}

@Composable
private fun Editor(source: TextSource, initialText: String, onTextChanged: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(
                top = editorPadding,
                start = editorPadding,
                end = editorPadding
            )
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier,
            text = stringResource(id = source.id)
        )

        // BasicTextField still doesn't have "native" behavior (https://issuetracker.google.com/issues/137321832)
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                EditText(context).apply {
                    background = null
                    minHeight = TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48f, context.resources.displayMetrics)
                        .toInt()

                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    gravity = Gravity.TOP
                    requestFocus()

                    setText(initialText)
                    addTextChangedListener(onTextChanged = { text, start, count, after ->
                        onTextChanged(text.toString())
                    })
                }
            },
            update = { view ->

            }
        )
    }
}


@Preview(apiLevel = 34)
@Composable
private fun TextEditorRoutePreview() {
    TextEditorPage(onDone = {}, onDismiss = {}, initialText = "Hello fren")
}
