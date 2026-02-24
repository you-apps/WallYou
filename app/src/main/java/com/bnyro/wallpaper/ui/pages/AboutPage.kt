package com.bnyro.wallpaper.ui.pages

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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

    val appVersion = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.22f)
                    )
                )
            )
            .padding(bottom = 12.dp)
    ) {
        AboutHeroCard(
            appVersion = appVersion,
            onOpenRepo = {
                openLinkFromHref(SocialLinks.REPO_GITHUB)
            },
            onOpenAuthor = {
                openLinkFromHref(SocialLinks.AUTHOR_GITHUB)
            }
        )

        AboutContainer {
            Text(
                text = stringResource(R.string.about_app_details),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            AboutRow(
                title = stringResource(R.string.version),
                summary = appVersion,
                imageVector = Icons.Default.Info
            )
            AboutRow(
                title = stringResource(R.string.package_name),
                summary = BuildConfig.APPLICATION_ID,
                imageVector = Icons.Default.Info
            )
            AboutRow(
                title = stringResource(R.string.github),
                summary = "github.com/you-apps/wallyou",
                painterResource = painterResource(R.drawable.ic_github)
            ) {
                openLinkFromHref(SocialLinks.REPO_GITHUB)
            }
            AboutRow(
                title = stringResource(R.string.license),
                summary = "GNU GPL v3.0",
                painterResource = painterResource(R.drawable.ic_license)
            ) {
                openLinkFromHref(SocialLinks.LICENSE)
            }
        }

        AboutContainer {
            Text(
                text = stringResource(R.string.about_author_links),
                modifier = Modifier.padding(start = 10.dp, bottom = 5.dp),
                style = MaterialTheme.typography.labelLarge
            )
            AboutRow(
                title = stringResource(R.string.author),
                summary = "you-apps",
                painterResource = painterResource(R.drawable.ic_author)
            ) {
                openLinkFromHref(SocialLinks.AUTHOR_GITHUB)
            }
            AboutRow(
                title = stringResource(R.string.website),
                summary = "you-apps.github.io",
                painterResource = painterResource(R.drawable.ic_web)
            ) {
                openLinkFromHref(SocialLinks.AUTHOR_WEBSITE)
            }
            AboutRow(
                title = stringResource(R.string.github),
                summary = "github.com/you-apps",
                painterResource = painterResource(R.drawable.ic_github)
            ) {
                openLinkFromHref(SocialLinks.AUTHOR_GITHUB)
            }
        }
    }
}

@Composable
private fun AboutHeroCard(
    appVersion: String,
    onOpenRepo: () -> Unit,
    onOpenAuthor: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.large,
        tonalElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                )
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.about_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "${stringResource(R.string.version)}: $appVersion",
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = BuildConfig.APPLICATION_ID,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                FilledTonalButton(
                    modifier = Modifier.weight(1f),
                    onClick = onOpenRepo
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_github),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = stringResource(R.string.open_repo))
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onOpenAuthor
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_author),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = stringResource(R.string.author_profile))
                }
            }
        }
    }
}
