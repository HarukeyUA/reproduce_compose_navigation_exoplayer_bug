package com.github.whitescent.exoplayer_navigation_bug

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.whitescent.exoplayer_navigation_bug.databinding.PlayerLayoutBinding
import com.github.whitescent.exoplayer_navigation_bug.player.ExoPlayerLifecycleEvents
import com.github.whitescent.exoplayer_navigation_bug.player.rememberExoPlayerInstance
import com.github.whitescent.exoplayer_navigation_bug.ui.theme.Exoplayer_navigation_bugTheme

const val url = "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3"

@SuppressLint("UnsafeOptInUsageError")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Exoplayer_navigation_bugTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "home") {
                    composable("home") {
                        Box(Modifier.fillMaxSize().background(Color.Red), Alignment.Center) {
                            Column {
                                Text("HOME")
                                Button(onClick = { navController.navigate("video") }) {
                                }
                            }
                        }
                    }
                    composable("video") {
                        val exoPlayer = rememberExoPlayerInstance()
                        ExoPlayerLifecycleEvents(exoPlayer)
                        Box(Modifier.fillMaxSize(), Alignment.Center) {
                            AndroidViewBinding(
                                PlayerLayoutBinding::inflate,
                                update = {
                                    root.player = exoPlayer
                                    root.setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
                                    root.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                                    root.useController = false
                                },
                                onReset = {
                                    root.player = exoPlayer
                                },
                            )

                            LaunchedEffect(Unit) {
                                exoPlayer.run {
                                    setMediaItem(
                                        MediaItem.Builder().setMediaId(url).setUri(url).build(),
                                    )
                                    prepare()
                                    repeatMode = Player.REPEAT_MODE_ONE
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
