package com.github.terrakok.oefoef.ui.welcome

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.oefoef.DataService
import com.github.terrakok.oefoef.LessonHeader
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.measureTimedValue

@ContributesIntoMap(AppScope::class)
@ViewModelKey(WelcomeViewModel::class)
@Inject
class WelcomeViewModel(
    private val dataService: DataService
) : ViewModel() {
    var items = mutableStateListOf<LessonHeader>()
        private set

    var loading by mutableStateOf(false)
        private set
    var isRefreshing by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    var languageSettings by mutableStateOf(dataService.getLanguageSettings())
        private set
    val languageName by derivedStateOf {
        languageSettings.availableLanguages.firstOrNull {
            it.first == languageSettings.currentLanguageCode
        }?.second ?: "???"
    }

    init {
        loadItems()
    }

    fun loadItems() {
        viewModelScope.launch {
            try {
                loading = true
                error = null
                items.clear()
                items.addAll(dataService.getLessons(forceRefresh = true))
            } catch (e: Throwable) {
                error = e.message
            } finally {
                loading = false
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                isRefreshing = true
                val (freshList, duration) = measureTimedValue {
                    dataService.getLessons(forceRefresh = true)
                }
                if (duration.inWholeMilliseconds < 300) {
                    // A delay for smoother refresh UX (otherwise the indicator disappears to fast)
                    delay(500 - duration.inWholeMilliseconds)
                }
                items.clear()
                items.addAll(freshList)
            } catch (e: Throwable) {
                error = e.message
            } finally {
                isRefreshing = false
            }
        }
    }

    fun onLanguageSelected(languageCode: String) {
        languageSettings = dataService.updateCurrentLanguage(languageCode)
        loadItems()
    }
}