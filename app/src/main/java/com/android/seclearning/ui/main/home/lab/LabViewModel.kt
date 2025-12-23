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

    private val _tryHackMeCount = MutableLiveData<Int>()
    val tryHackMeCount: LiveData<Int> = _tryHackMeCount

    private val _hackTheBoxCount = MutableLiveData<Int>()
    val hackTheBoxCount: LiveData<Int> = _hackTheBoxCount


    private val _newLab = MutableLiveData<LabModel>()
    val newLab: LiveData<LabModel> = _newLab

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun fromDetailLab(): OpenLabFrom {
        return when (openLabFrom) {
            OpenLabFrom.SEED_LAB.from -> OpenLabFrom.SEED_LAB
            OpenLabFrom.BLUE_TEAM.from -> OpenLabFrom.BLUE_TEAM
            OpenLabFrom.LABTAINER.from -> OpenLabFrom.LABTAINER
            OpenLabFrom.CYBER.from -> OpenLabFrom.CYBER
            OpenLabFrom.TRY_HACK_ME.from -> OpenLabFrom.TRY_HACK_ME
            OpenLabFrom.HACK_THE_BOX.from -> OpenLabFrom.HACK_THE_BOX
            else -> OpenLabFrom.PORT_SWIGGER
        }
    }

    fun loadLabs() {
        viewModelScope.launch {
            try {
                _loading.value = true

                val response = homeRepository.get().getListLab(
                    platform = openLabFrom,
                    difficulty = null,
                    search = null
                )

                _count.value = response.count
                _labList.value = response.data

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }

    fun createLab(link: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = homeRepository.get().postLab(link)
                _newLab.value = response.data
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Logger.e("LabViewModel", "HTTP ${e.code()} Error Body: $errorBody")
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }


    fun checkTryHackMeAndHTB() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val tryHackMeResponse = homeRepository.get().getListLab(
                    platform = OpenLabFrom.TRY_HACK_ME.from,
                    difficulty = null,
                    search = null
                )
                _tryHackMeCount.value = tryHackMeResponse.count

                val hackTheBoxResponse = homeRepository.get().getListLab(
                    platform = OpenLabFrom.HACK_THE_BOX.from,
                    difficulty = null,
                    search = null
                )
                _hackTheBoxCount.value = hackTheBoxResponse.count

            } catch (e: Exception) {
                e.printStackTrace()
                _tryHackMeCount.value = 0
                _hackTheBoxCount.value = 0
            }
            _loading.value = false
        }
    }



    fun copyText(label: String, text: String) {
        copyPasteManager.get().copyToClipboard(label, text)
    }
}

