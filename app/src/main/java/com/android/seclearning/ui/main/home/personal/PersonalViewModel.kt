package com.android.seclearning.ui.main.home.personal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.seclearning.Logger
import com.android.seclearning.data.model.ProgressModel
import com.android.seclearning.data.repository.AppRepository
import com.android.seclearning.data.repository.HomeDataRepository
import com.android.seclearning.data.response.RoadmapResponse
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalViewModel @Inject constructor(
    private val homeRepository: Lazy<HomeDataRepository>,
    private val appRepository: Lazy<AppRepository>
) : ViewModel(){
    var career : String = ""
    var completed: Int = 0
    private val _progressList = MutableLiveData<List<ProgressModel>>()
    val progressList: LiveData<List<ProgressModel>> = _progressList

    private val _roadmap = MutableLiveData<RoadmapResponse>()
    val roadmap: LiveData<RoadmapResponse> = _roadmap

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _putProgressResult = MutableLiveData<Result<Int>>()
    val putProgressResult: LiveData<Result<Int>> = _putProgressResult

    fun putProgress(itemIndex: Int) {
        viewModelScope.launch {
            val studentId = appRepository.get().getUserId()

            Logger.d(
                "PUT_PROGRESS",
                "Start putProgress | studentId=$studentId | career=$career | itemIndex=$itemIndex"
            )

            runCatching {
                homeRepository.get().putProgress(
                    studentId = studentId,
                    career = career,
                    itemIndex = itemIndex
                )
            }.onSuccess {
                Logger.d(
                    "PUT_PROGRESS",
                    "SUCCESS | itemIndex=$itemIndex"
                )

                _putProgressResult.value = Result.success(itemIndex + 1)
            }.onFailure { throwable ->
                Logger.e(
                    "PUT_PROGRESS",
                    "FAILED | itemIndex=$itemIndex | message=${throwable.message}",
                    throwable
                )

                _putProgressResult.value = Result.failure(throwable)
            }
        }
    }


    fun loadProgress() {
        viewModelScope.launch {
            try {
                _loading.value = true

                val response = homeRepository.get().getListProgress(
                    id = appRepository.get().getUserId()
                )
                _progressList.value = response.progress

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }

    fun loaDetailProgress() {
        viewModelScope.launch {
            try {
                _loading.value = true

                val response = homeRepository.get().getProgress(
                    id = appRepository.get().getUserId(),
                    career = career
                )

                _roadmap.value = response
                completed = response.completed

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }

}