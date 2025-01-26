package com.bignerdranch.geoqiz

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bignerdranch.geoqiz.databinding.ActivityMainBinding

private const val TAG = "мы внутри мейн"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




        if (quizViewModel.buttonState.isEmpty()) {
            quizViewModel.makeButtonsState(quizViewModel.questionBankSize)
        }


        binding.scoreText.text = quizViewModel.scoreText

        binding.reStart.visibility = if (quizViewModel.isReStartVisible) View.VISIBLE else View.INVISIBLE
        binding.scoreText.visibility = if (quizViewModel.isScoreVisible) View.VISIBLE else View.INVISIBLE


        binding.trueButton.setOnClickListener {
            val buttonColor = chekAnswer(true)
            quizViewModel.buttonState[quizViewModel.currentIndex].trueButtonColor = buttonColor
            binding.trueButton.backgroundTintList = ContextCompat.getColorStateList(this, buttonColor)
            lastQuestion()

        }

        // сообщение о неверном ответе
        binding.falseButton.setOnClickListener {
            val buttonColor = chekAnswer(false)
            quizViewModel.buttonState[quizViewModel.currentIndex].falseButtonColor = buttonColor
            binding.falseButton.backgroundTintList = ContextCompat.getColorStateList(this, buttonColor)
            lastQuestion()
        }

        binding.rightArrow.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.leftArrow.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }


        binding.reStart.setOnClickListener {
            reStartQuizz()
            updateQuestion()
        }

        updateQuestion()
    }

    private fun updateQuestion() {
        // устанавливаем цвет кнопок
        val currentTrueButtonColor = quizViewModel.buttonState[quizViewModel.currentIndex].trueButtonColor
        val currentFalseButtonColor = quizViewModel.buttonState[quizViewModel.currentIndex].falseButtonColor

        binding.trueButton.backgroundTintList =
            ContextCompat.getColorStateList(this, currentTrueButtonColor)
        binding.falseButton.backgroundTintList =
            ContextCompat.getColorStateList(this, currentFalseButtonColor)

        // устанавливаем активность кнопки для нового вопроса
        binding.trueButton.isEnabled = quizViewModel.buttonState[quizViewModel.currentIndex].clicable
        binding.falseButton.isEnabled = quizViewModel.buttonState[quizViewModel.currentIndex].clicable

        // устанавливаем вопрос
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
        binding.questionNumber.setText("№ вопроса: ${quizViewModel.currentIndex + 1}")
    }


    private fun chekAnswer(userAnswer: Boolean): Int {
        var messageResId: Int // текст для тоста
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val buttonColor: Int

        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            quizViewModel.userCorrectAnswer ++
            buttonColor = R.color.correctAnswer
        } else {
            messageResId = R.string.wrong_toast
            buttonColor = R.color.wrongAnswer
        }

        // вывводим тост
        Toast.makeText(
            this,
            messageResId,
            Toast.LENGTH_SHORT
        ).show()

        // блокируем кнопки после ответа
        quizViewModel.buttonState[quizViewModel.currentIndex].clicable = false

        binding.trueButton.isEnabled = quizViewModel.buttonState[quizViewModel.currentIndex].clicable
        binding.falseButton.isEnabled = quizViewModel.buttonState[quizViewModel.currentIndex].clicable

        return buttonColor
    }



    fun lastQuestion() {
        if (quizViewModel.buttonState.all { !it.clicable }) {

            quizViewModel.isScoreVisible = true
            quizViewModel.isReStartVisible = true

            // Обновляем интерфейс
            quizViewModel.scoreText = "Количество верных ответов: ${quizViewModel.userCorrectAnswer} / ${quizViewModel.questionBankSize}"
            binding.scoreText.text = quizViewModel.scoreText
            updateVisibility()
        }

    }


    fun reStartQuizz() {
        quizViewModel.makeButtonsState(quizViewModel.questionBankSize)

        binding.trueButton.isEnabled = quizViewModel.buttonState[quizViewModel.currentIndex].clicable
        binding.falseButton.isEnabled = quizViewModel.buttonState[quizViewModel.currentIndex].clicable

        quizViewModel.userCorrectAnswer = 0
        quizViewModel.currentIndex = 0
        quizViewModel.isScoreVisible = false
        quizViewModel.isReStartVisible = false
        updateVisibility()
    }

    private fun updateVisibility() {
        binding.scoreText.visibility = if (quizViewModel.isScoreVisible) View.VISIBLE else View.INVISIBLE
        binding.reStart.visibility = if (quizViewModel.isReStartVisible) View.VISIBLE else View.INVISIBLE
    }
}