package com.bignerdranch.geoqiz

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    private lateinit var questionTextView: TextView
    private lateinit var numberOfQuestionTextView: TextView
    private lateinit var scoreText: TextView


    private lateinit var nextButton: ImageView
    private lateinit var previousButton: ImageView
    private lateinit var reStart: ImageView


    private val questionBank: List<Question> = listOf(
        Question(R.string.question_australia, false),
        Question(R.string.question_kama, true),
        Question(R.string.question_moon, true),
        Question(R.string.question_izhevsk, false),
        Question(R.string.question_dublin, false)
    )

    // Добавляем кнопки. Количество кнопок = количеству вопросов
    private var buttonState = ButtonState.addButton(questionBank.size)
    private var currentIndex = 0
    private var userCorrectAnswer = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)

        questionTextView = findViewById(R.id.question_text_view)
        numberOfQuestionTextView = findViewById(R.id.question_number)
        scoreText = findViewById(R.id.score_text)

        nextButton = findViewById(R.id.right_arrow)
        previousButton = findViewById(R.id.left_arrow)
        reStart = findViewById(R.id.reStart)


        trueButton.setOnClickListener {
            val buttonColor = chekAnswer(true)
            buttonState[currentIndex].trueButtonColor = buttonColor
            trueButton.backgroundTintList = ContextCompat.getColorStateList(this, buttonColor)
            lastQuestion()

        }

        // сообщение о неверном ответе
        falseButton.setOnClickListener {
            val buttonColor = chekAnswer(false)
            buttonState[currentIndex].falseButtonColor = buttonColor
            falseButton.backgroundTintList = ContextCompat.getColorStateList(this, buttonColor)
            lastQuestion()
        }

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        previousButton.setOnClickListener {
            currentIndex = (currentIndex - 1 + questionBank.size) % questionBank.size
            updateQuestion()
        }


        reStart.setOnClickListener {
            reStartQuizz()
            updateQuestion()
        }

        updateQuestion()
    }

    private fun updateQuestion() {

        // устанавливаем цвет кнопок
        val currentTrueButtonColor = buttonState[currentIndex].trueButtonColor
        val currentFalseButtonColor = buttonState[currentIndex].falseButtonColor

        trueButton.backgroundTintList =
            ContextCompat.getColorStateList(this, currentTrueButtonColor)
        falseButton.backgroundTintList =
            ContextCompat.getColorStateList(this, currentFalseButtonColor)

        // устанавливаем активность кнопки для нового вопроса
        trueButton.isEnabled = buttonState[currentIndex].buttonState
        falseButton.isEnabled = buttonState[currentIndex].buttonState

        // устанавливаем вопрос
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
        numberOfQuestionTextView.setText("№ вопроса: ${currentIndex + 1}")
    }

    private fun chekAnswer(userAnswer: Boolean): Int {

        var messageResId: Int // текст для тоста
        val correctAnswer = questionBank[currentIndex].answer
        val buttonColor: Int

        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            userCorrectAnswer++
            buttonColor = R.color.correctAnswer
        } else {
            messageResId = R.string.wrong_toast
            buttonColor = R.color.wrongAnswer
        }

        // вывводим тост
        val toast = Toast.makeText(
            this,
            messageResId,
            Toast.LENGTH_SHORT
        )
        toast.setGravity(Gravity.TOP, 0, 250)
        toast.show()

        // блокируем кнопки после ответа
        buttonState[currentIndex].buttonState = false

        trueButton.isEnabled = buttonState[currentIndex].buttonState
        falseButton.isEnabled = buttonState[currentIndex].buttonState

        return buttonColor
    }


    fun lastQuestion() {
        for (i in 0 until buttonState.size - 1) {
            if (buttonState[i].buttonState) return
        }
        reStart.visibility = View.VISIBLE
        scoreText.setText("Количество верных ответов: $userCorrectAnswer / ${questionBank.size}")
        scoreText.visibility = View.VISIBLE
    }

    fun reStartQuizz() {
        buttonState = ButtonState.addButton(questionBank.size)

        trueButton.isEnabled = buttonState[currentIndex].buttonState
        falseButton.isEnabled = buttonState[currentIndex].buttonState

        userCorrectAnswer = 0
        currentIndex = 0
        reStart.visibility = View.INVISIBLE
        scoreText.visibility = View.INVISIBLE

    }
}