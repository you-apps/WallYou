package com.bnyro.wallpaper.ui.activities

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bnyro.wallpaper.BuildConfig
import com.bnyro.wallpaper.R
import com.bnyro.wallpaper.constants.SocialLinks
import com.bnyro.wallpaper.ui.components.about.AboutContainer
import com.bnyro.wallpaper.ui.components.about.AboutRow

@Composable
fun AboutPage() {
    val context = LocalContext.current

    fun openLinkFromHref(href: String) {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(href))
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AboutContainer(
            modifier = Modifier.padding(10.dp)
        ) {
            AboutRow(
                title = stringResource(R.string.version),
                summary = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                imageVector = Icons.Default.Info
            )
            AboutRow(
                title = stringResource(R.string.github),
                painterResource = painterResource(R.drawable.ic_github)
            ) {
                openLinkFromHref(SocialLinks.REPO_GITHUB)
            }
            AboutRow(
                title = stringResource(R.string.license),
                painterResource = painterResource(R.drawable.ic_license)
            ) {
                openLinkFromHref(SocialLinks.LICENSE)
            }
        }
        Spacer(
            modifier = Modifier
                .height(10.dp)
        )
        AboutContainer(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = stringResource(R.string.author),
                modifier = Modifier.padding(
                    start = 10.dp,
                    bottom = 5.dp
                )
            )
            AboutRow(
                title = stringResource(R.string.website),
                painterResource = painterResource(R.drawable.ic_web)
            ) {
                openLinkFromHref(SocialLinks.AUTHOR_WEBSITE)
            }
            AboutRow(
                title = stringResource(R.string.github),
                painterResource = painterResource(R.drawable.ic_github)
            ) {
                openLinkFromHref(SocialLinks.AUTHOR_GITHUB)
            }
        }
    }
}
