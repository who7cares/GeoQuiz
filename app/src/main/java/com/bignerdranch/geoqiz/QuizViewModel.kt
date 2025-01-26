package com.bignerdranch.geoqiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QizViewModel"

class QuizViewModel : ViewModel() {

//    init {
//        Log.d(TAG, "Создан экземмпляр QizViewModel")
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        Log.d(TAG,"вью модель уничтожена")
//    }

    var currentIndex = 0
    var buttonState: MutableList<ButtonState> = mutableListOf()

    var userCorrectAnswer = 0
    var isScoreVisible: Boolean = false
    var isReStartVisible: Boolean = false
    var scoreText: String = ""


    private val questionBank: List<Question> = listOf(
        Question(R.string.question_australia, false),
        Question(R.string.question_kama, true),
        Question(R.string.question_moon, true),
        Question(R.string.question_izhevsk, false),
        Question(R.string.question_dublin, false)
    )


    val questionBankSize: Int
        get() = questionBank.size

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        currentIndex = (currentIndex - 1 + questionBank.size) % questionBank.size
    }


    fun makeButtonsState(questionBankSize: Int) {

        buttonState = MutableList(questionBankSize) { ButtonState() }
    }
}

