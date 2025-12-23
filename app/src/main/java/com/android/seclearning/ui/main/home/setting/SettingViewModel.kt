package com.android.seclearning.ui.main.home.setting

import androidx.lifecycle.ViewModel
import com.android.seclearning.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import dagger.Lazy

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val appRepository: Lazy<AppRepository>
) : ViewModel() {
    fun logout() {
        appRepository.get().clearUser()
    }
}