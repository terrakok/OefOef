import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.github.terrakok.oefoef.App
import com.github.terrakok.oefoef.spellcheck.WebClientSpellcheck

@OptIn(ExperimentalComposeUiApi::class)
fun main() = ComposeViewport {
    App(
        clientSpellcheck = WebClientSpellcheck()
    )
}
