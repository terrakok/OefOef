package com.github.terrakok.oefoef

import androidx.window.core.layout.WindowSizeClass
import co.touchlab.kermit.NoTagFormatter
import co.touchlab.kermit.Severity
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter


const val MAILTO_LINK = "mailto:oefoefapp@gmail.com?subject=OefOef%20app%20support&body="

private const val WIDE_SCREEN = 800
fun WindowSizeClass.isWide() = isWidthAtLeastBreakpoint(WIDE_SCREEN)

const val DEBUG = false
val Log = object : co.touchlab.kermit.Logger(
    config = loggerConfigInit(
        platformLogWriter(NoTagFormatter),
        minSeverity = if (DEBUG) Severity.Verbose else Severity.Error,
    ),
    tag = "OefOef"
) {}