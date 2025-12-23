package com.android.seclearning.ui.main.home.main.learning

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.seclearning.Logger
import com.android.seclearning.common.utils.CopyPasteManager
import com.android.seclearning.data.enums.OpenDetailFrom
import com.android.seclearning.data.model.LabModel
import com.android.seclearning.data.model.ResourceModel
import com.android.seclearning.data.repository.AppRepository
import com.android.seclearning.data.repository.HomeDataRepository
import com.android.seclearning.data.response.RegisterRoadmapResponse
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LearningViewModel @Inject constructor(
    private val homeRepository: Lazy<HomeDataRepository>,
    private val appRepository: Lazy<AppRepository>,
    private val copyPasteManager: Lazy<CopyPasteManager>
) : ViewModel() {
    var openDetailFrom: String = ""

    fun isAdmin(): Boolean = appRepository.get().isAdmin()

    private val _registerResult = MutableLiveData<Result<RegisterRoadmapResponse>>()
    val registerResult: LiveData<Result<RegisterRoadmapResponse>> = _registerResult

    private val _resourceList = MutableLiveData<List<ResourceModel>>()
    val resourceList: LiveData<List<ResourceModel>> = _resourceList

    private val _count = MutableLiveData<Int>()
    val count: LiveData<Int> = _count

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun fromDetailPackage(): OpenDetailFrom {
        return when (openDetailFrom) {
            OpenDetailFrom.SOC.from -> OpenDetailFrom.SOC
            OpenDetailFrom.WEB.from -> OpenDetailFrom.WEB
            OpenDetailFrom.NETWORK.from -> OpenDetailFrom.NETWORK
            OpenDetailFrom.MALWARE.from -> OpenDetailFrom.MALWARE
            else -> OpenDetailFrom.DFIR
        }
    }

    fun platformResource(): String {
        return when (openDetailFrom) {
            OpenDetailFrom.SOC.from -> "soc_analyst"
            OpenDetailFrom.WEB.from -> "web_pentester"
            OpenDetailFrom.NETWORK.from -> "network_security"
            OpenDetailFrom.MALWARE.from -> "malware_analyst"
            else -> "dfir"
        }
    }

    fun registerRoadmap() {
        viewModelScope.launch {
            try {
                Logger.d(
                    "RegisterRoadmap",
                    "Start register: userId=${
                        appRepository.get().getUserId()
                    }, career=$openDetailFrom"
                )

                val res = homeRepository.get()
                    .registerRoadmap(
                        appRepository.get().getUserId(),
                        openDetailFrom,
                        null
                    )

                Logger.d(
                    "RegisterRoadmap",
                    "SUCCESS → message=${res.message}"
                )

                _registerResult.postValue(Result.success(res))
            } catch (e: Exception) {

                Logger.e(
                    "RegisterRoadmap",
                    "FAILED → ${e.message}",
                    e
                )

                _registerResult.postValue(Result.failure(e))
            }
        }
    }

    fun loadResource() {
        viewModelScope.launch {
            try {
                _loading.value = true

                val response = homeRepository.get().getListResource(
                    platform = platformResource(),
                    search = null,
                    category = null,
                    level = null,
                    language = null
                )

                _count.value = response.count
                _resourceList.value = response.data

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loading.value = false
            }

        }
    }

    fun copyText(label: String, text: String) {
        copyPasteManager.get().copyToClipboard(label, text)
    }
}