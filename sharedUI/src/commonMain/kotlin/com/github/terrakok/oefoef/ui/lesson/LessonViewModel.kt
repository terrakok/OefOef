package com.github.terrakok.oefoef.ui.lesson

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.oefoef.domain.DataRepository
import com.github.terrakok.oefoef.entity.Lesson
import dev.zacsweers.metro.*
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import kotlinx.coroutines.launch

@AssistedInject
class LessonViewModel(
    @Assisted val id: String,
    val dataRepository: DataRepository,
) : ViewModel() {
    @AssistedFactory
    @ManualViewModelAssistedFactoryKey(Factory::class)
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(id: String): LessonViewModel
    }

    var lesson by mutableStateOf<Lesson?>(null)
        private set

    var loading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            try {
                loading = true
                error = null
                lesson = dataRepository.getLesson(id)
            } catch (e: Throwable) {
                error = e.message
            } finally {
                loading = false
            }
        }
    }
}
