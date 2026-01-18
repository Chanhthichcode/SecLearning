package com.android.seclearning.ui.main.home.lab

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.seclearning.Logger
import com.android.seclearning.common.utils.CopyPasteManager
import com.android.seclearning.data.enums.OpenLabFrom
import com.android.seclearning.data.model.LabModel
import com.android.seclearning.data.repository.AppRepository
import com.android.seclearning.data.repository.HomeDataRepository
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LabViewModel @Inject constructor(
    private val homeRepository: Lazy<HomeDataRepository>,
    private val appRepository: Lazy<AppRepository>,
    private val copyPasteManager: Lazy<CopyPasteManager>
) : ViewModel() {

    fun isAdmin(): Boolean = appRepository.get().isAdmin()

    var openLabFrom: String = ""

    private val _labList = MutableLiveData<List<LabModel>>()
    val labList: LiveData<List<LabModel>> = _labList

    private val _count = MutableLiveData<Int>()
    val count: LiveData<Int> = _count

    private val _newLabCount = MutableLiveData<Int>()
    val newLabCount: LiveData<Int> = _newLabCount

    private val _newLab = MutableLiveData<LabModel>()
    val newLab: LiveData<LabModel> = _newLab

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun fromDetailLab(): OpenLabFrom {
        return OpenLabFrom.values().firstOrNull {
            it.from == openLabFrom
        } ?: OpenLabFrom.NEW_LAB
    }

    fun loadLabs() {
        launchWithLoading {
            val response = homeRepository.get().getListLab(
                platform = openLabFrom,
                difficulty = null,
                search = null
            )
            _count.value = response.count
            _labList.value = response.data
        }
    }

    fun createLab(link: String) {
        launchWithLoading {
            try {
                val response = homeRepository.get().postLab(link)
                _newLab.value = response.data
            } catch (e: HttpException) {
                logHttpError(e)
            }
        }
    }

    fun checkTryHackMeAndHTB() {
        launchWithLoading {
            try {
                val response = homeRepository.get().getListLab(
                    platform = OpenLabFrom.NEW_LAB.from,
                    difficulty = null,
                    search = null
                )
                _newLabCount.value = response.count
            } catch (e: Exception) {
                e.printStackTrace()
                _newLabCount.value = 0
            }
        }
    }

    fun copyText(label: String, text: String) {
        copyPasteManager.get().copyToClipboard(label, text)
    }

    private fun launchWithLoading(block: suspend () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            try {
                block()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }

    private fun logHttpError(e: HttpException) {
        val errorBody = e.response()?.errorBody()?.string()
        Logger.e(
            "LabViewModel",
            "HTTP ${e.code()} Error Body: $errorBody"
        )
    }
}
