import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.skydoves.orbital.animateSharedElementTransition
import com.skydoves.orbital.rememberContentWithOrbitalScope
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import com.skydoves.orbital.Orbital
import com.skydoves.orbital.OrbitalScope

data class ScreenA(val sharedContent: @Composable () -> Unit) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Column(modifier = Modifier.width(200.dp)) {
            Button(onClick = { navigator.push(ScreenB(sharedContent)) }) {
                Text("Go to Screen B")
            }

            sharedContent()
        }
    }
}

data class ScreenB(val sharedContent: @Composable () -> Unit) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Column {
            Button(onClick = { navigator.push(ScreenA(sharedContent)) }) {
                Text("Go to Screen A")
            }

            sharedContent()
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {

        val rememberContentWithOrbitalScope = rememberContentWithOrbitalScope {
            KamelImage(
                resource = asyncPainterResource(data = "https://miro.medium.com/v2/resize:fit:1400/1*QjQTzohDz9gznswKpuqUXQ.jpeg"),
                contentDescription = "",
                modifier = Modifier.fillMaxWidth().padding(10.dp)
                    .animateSharedElementTransition(
                        this,
                        SpringSpec(stiffness = 500f, dampingRatio = Spring.DampingRatioMediumBouncy),
                        SpringSpec(stiffness = 500f, dampingRatio = Spring.DampingRatioMediumBouncy)
                    )
            )
        }

        Orbital {
            Navigator(ScreenA { rememberContentWithOrbitalScope() })
        }
    }
}
