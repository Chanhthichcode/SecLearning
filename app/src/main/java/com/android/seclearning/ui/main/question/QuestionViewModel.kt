package com.android.seclearning.ui.main.question

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.seclearning.Logger
import com.android.seclearning.common.utils.runInIO
import com.android.seclearning.data.enums.AnswerType
import com.android.seclearning.data.model.QuestionModel
import com.android.seclearning.data.model.QuizModel
import com.android.seclearning.data.repository.AppRepository
import com.android.seclearning.data.repository.HomeDataRepository
import com.android.seclearning.data.request.AnswerRequest
import com.android.seclearning.data.request.QuizAnswerRequest
import com.android.seclearning.data.request.QuizSubmitRequest
import com.android.seclearning.data.request.SubmissionRequest
import com.android.seclearning.data.response.QuizSubmitResponse
import com.android.seclearning.data.response.SubmissionResponse
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
    private val appRepository: Lazy<AppRepository>
) : ViewModel() {
    private val _navigatePage = MutableLiveData<Int>()
    val navigatePage: LiveData<Int> get() = _navigatePage

    private val _answerType = MutableStateFlow<AnswerType?>(null)
    val answerType = _answerType.asStateFlow()

    private val _listQuestion = MutableStateFlow<List<QuestionModel>>(emptyList())
    val listQuestion: StateFlow<List<QuestionModel>> = _listQuestion

    private val _submissionResult = MutableLiveData<SubmissionResponse>()
    val submissionResult: LiveData<SubmissionResponse> get() = _submissionResult

    private val _listQuiz = MutableStateFlow<List<QuizModel>>(emptyList())
    val listQuiz: StateFlow<List<QuizModel>> = _listQuiz

    private val _submissionQuiz = MutableLiveData<QuizSubmitResponse>()
    val submissionQuiz: LiveData<QuizSubmitResponse> get() = _submissionQuiz

    val fetchLoadingStatus = MutableLiveData<Boolean>(false)

    var domain: String = ""

    fun goTo(position: Int) {
        _navigatePage.value = position
    }

    fun setOption(option: AnswerType) {
        _answerType.value = option
    }

    fun fetchDataByAnswerType(answerType: AnswerType) {
        when (answerType) {
            AnswerType.BEGINNER -> fetchDataQuestion()
            AnswerType.IMPROVER -> fetchDataQuiz()
            else -> {}
        }
    }

    fun submitByAnswerType(answerType: AnswerType) {
        when (answerType) {
            AnswerType.BEGINNER -> submitAnswers()
            AnswerType.IMPROVER -> submitQuiz()
            else -> {}
        }
    }


    fun fetchDataQuestion() {
        viewModelScope.launch {
            runInIO {
                fetchLoadingStatus.postValue(true)
                try {
                    val questions = homeDataRepository.get()
                        .getListQuestions()
                        .map { it.copy(selectedOption = -1) }
                    _listQuestion.value = questions
                    Logger.e("fetchQuestion", "listQuestion=$questions")
                } catch (e: Exception) {
                    e.printStackTrace()
                    Logger.e("fetchQuestion", e, "fetch fetchQuestion error ${e.message}")
                } finally {
                    fetchLoadingStatus.postValue(false)
                }
            }
        }
    }

    fun updateAnswer(questionId: Int, optionIndex: Int) {
        val newList = _listQuestion.value.toMutableList()
        val index = newList.indexOfFirst { it.questionId == questionId }

        if (index != -1) {
            newList[index] = newList[index].copy(
                selectedOption = optionIndex
            )
            _listQuestion.value = newList
        }
    }

    fun submitAnswers() {
        val questions = _listQuestion.value


        val request = SubmissionRequest(
            studentId = appRepository.get().getUserId(),
            studentName = appRepository.get().getUserName(),
            answers = questions.map {
                AnswerRequest(
                    questionId = it.questionId,
                    optionIndex = it.selectedOption
                )
            }
        )

        viewModelScope.launch {
            try {
                fetchLoadingStatus.postValue(true)

                val response = homeDataRepository.get().submitAnswers(
                    studentId = request.studentId,
                    studentName = request.studentName,
                    answers = request.answers
                )
                Logger.e("submitAnswers", "response=$response")
                _submissionResult.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                fetchLoadingStatus.postValue(false)
            }
        }
    }

    fun fetchDataQuiz() {
        viewModelScope.launch {
            runInIO {
                fetchLoadingStatus.postValue(true)
                try {
                    val listQuiz = homeDataRepository.get()
                        .getListQuiz(10, domain).data
                        .map { it.copy(selectedOption = -1) }
                    _listQuiz.value = listQuiz
                    Logger.e("fetchQuiz", "listQuiz=$listQuiz")
                } catch (e: Exception) {
                    e.printStackTrace()
                    Logger.e("fetchQuiz", e, "fetch fetchQuiz error ${e.message}")
                } finally {
                    fetchLoadingStatus.postValue(false)
                }
            }
        }
    }

    fun updateQuiz(quizId: Int, optionIndex: Int) {
        val newList = _listQuiz.value.toMutableList()
        val index = newList.indexOfFirst { it.id == quizId }

        if (index != -1) {
            newList[index] = newList[index].copy(
                selectedOption = optionIndex
            )
            _listQuiz.value = newList
        }
    }

    fun submitQuiz() {
        val listQuiz = _listQuiz.value


        val request = QuizSubmitRequest(
            userId = appRepository.get().getUserId(),
            domain = domain,
            submissions = listQuiz.map {
                QuizAnswerRequest(
                    id = it.id,
                    userAnswer = it.selectedOption
                )
            }
        )

        viewModelScope.launch {
            try {
                fetchLoadingStatus.postValue(true)

                val response = homeDataRepository.get().submitQuiz(
                    userId = request.userId,
                    domain = request.domain,
                    submissions = request.submissions
                )
                _submissionQuiz.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                fetchLoadingStatus.postValue(false)
            }
        }
    }
}