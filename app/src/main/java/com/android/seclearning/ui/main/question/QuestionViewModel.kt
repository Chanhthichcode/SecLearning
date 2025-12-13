package com.android.seclearning.ui.main.question

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.seclearning.Logger
import com.android.seclearning.common.utils.runInIO
import com.android.seclearning.data.enums.AnswerType
import com.android.seclearning.data.model.QuestionModel
import com.android.seclearning.data.repository.HomeDataRepository
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val homeDataRepository: Lazy<HomeDataRepository>,
) : ViewModel() {
    private val _navigatePage = MutableLiveData<Int>()
    val navigatePage: LiveData<Int> get() = _navigatePage

    private val _selectedOption = MutableStateFlow<AnswerType?>(null)
    val selectedOption = _selectedOption.asStateFlow()

    private val _listQuestion = MutableStateFlow<List<QuestionModel>>(emptyList())
    val listQuestion: StateFlow<List<QuestionModel>> = _listQuestion

    val fetchLoadingStatus = MutableLiveData<Boolean>(false)

    fun goTo(position: Int) {
        _navigatePage.value = position
    }

    fun setOption(option: AnswerType) {
        _selectedOption.value = option
    }

    fun fetchDataQuestion() {
        viewModelScope.launch {
            runInIO {
                fetchLoadingStatus.postValue(true)
                try {
                    val questionList = homeDataRepository.get().getListQuestions()
                    _listQuestion.value = questionList
                } catch (e: Exception) {
                    e.printStackTrace()
                    Logger.e("fetchQuestion", e, "fetch fetchQuestion error ${e.message}")
                } finally {
                    fetchLoadingStatus.postValue(false)
                }
            }
        }
    }
}