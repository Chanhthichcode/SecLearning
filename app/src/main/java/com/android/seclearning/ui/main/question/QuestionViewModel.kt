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

    fun goTo(position: Int) {
        _navigatePage.value = position
    }

    private val _answerType = MutableStateFlow<AnswerType?>(null)
    val answerType = _answerType.asStateFlow()

    fun setOption(option: AnswerType) {
        _answerType.value = option
    }

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

    fun fetchDataByAnswerType(answerType: AnswerType) {
        when (answerType) {
            AnswerType.BEGINNER -> fetchDataQuestion()
            AnswerType.IMPROVER -> fetchDataQuiz()
            else -> Unit
        }
    }

    fun submitByAnswerType(answerType: AnswerType) {
        when (answerType) {
            AnswerType.BEGINNER -> submitAnswers()
            AnswerType.IMPROVER -> submitQuiz()
            else -> Unit
        }
    }

    fun fetchDataQuestion() {
        launchWithLoading {
            val questions = homeDataRepository.get()
                .getListQuestions()
                .map { it.copy(selectedOption = -1) }

            _listQuestion.value = questions
            Logger.d("fetchQuestion", "listQuestion=$questions")
        }
    }

    fun updateAnswer(questionId: Int, optionIndex: Int) {
        _listQuestion.value = updateItem(
            _listQuestion.value,
            { it.questionId == questionId }
        ) {
            it.copy(selectedOption = optionIndex)
        }
    }

    fun submitAnswers() {
        val request = SubmissionRequest(
            studentId = appRepository.get().getUserId(),
            studentName = appRepository.get().getUserName(),
            answers = _listQuestion.value.map {
                AnswerRequest(
                    questionId = it.questionId,
                    optionIndex = it.selectedOption
                )
            }
        )

        launchWithLoading {
            val response = homeDataRepository.get().submitAnswers(
                studentId = request.studentId,
                studentName = request.studentName,
                answers = request.answers
            )
            _submissionResult.postValue(response)
        }
    }

    fun fetchDataQuiz() {
        launchWithLoading {
            val quizzes = homeDataRepository.get()
                .getListQuiz(10, domain)
                .data
                .map { it.copy(selectedOption = -1) }

            _listQuiz.value = quizzes
            Logger.d("fetchQuiz", "listQuiz=$quizzes")
        }
    }

    fun updateQuiz(quizId: Int, optionIndex: Int) {
        _listQuiz.value = updateItem(
            _listQuiz.value,
            { it.id == quizId }
        ) {
            it.copy(selectedOption = optionIndex)
        }
    }

    fun submitQuiz() {
        val request = QuizSubmitRequest(
            userId = appRepository.get().getUserId(),
            domain = domain,
            submissions = _listQuiz.value.map {
                QuizAnswerRequest(
                    id = it.id,
                    userAnswer = it.selectedOption
                )
            }
        )

        launchWithLoading {
            val response = homeDataRepository.get().submitQuiz(
                userId = request.userId,
                domain = request.domain,
                submissions = request.submissions
            )
            _submissionQuiz.postValue(response)
        }
    }

    private inline fun launchWithLoading(crossinline block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                fetchLoadingStatus.postValue(true)
                runInIO { block() }
            } catch (e: Exception) {
                Logger.e("QuestionViewModel", e, e.message ?: "")
            } finally {
                fetchLoadingStatus.postValue(false)
            }
        }
    }

    private fun <T> updateItem(
        list: List<T>,
        predicate: (T) -> Boolean,
        update: (T) -> T
    ): List<T> {
        val index = list.indexOfFirst(predicate)
        if (index == -1) return list

        return list.toMutableList().apply {
            this[index] = update(this[index])
        }
    }
}
