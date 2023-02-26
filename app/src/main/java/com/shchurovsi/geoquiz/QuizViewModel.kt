package com.shchurovsi.geoquiz

import androidx.lifecycle.ViewModel
import com.shchurovsi.geoquiz.model.Question

class QuizViewModel : ViewModel() {

    private val questionBank = arrayListOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private var _currentIndex = 0
    private var _correctAnswers = 0
    private var _incorrectAnswers = 0

    val currentIndex: Int
        get() = _currentIndex

    val correctAnswers: Int
        get() = _correctAnswers

    val incorrectAnswers: Int
        get() = _incorrectAnswers

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer


    fun increaseIndex() {
        _currentIndex++
    }

    fun decreaseIndex() {
        _currentIndex--
    }

    fun increaseCorrectAnswers() {
        _correctAnswers++
    }

    fun increaseIncorrectAnswers() {
        _incorrectAnswers++
    }

    fun reset() {
        _currentIndex = 0
        _correctAnswers = 0
        _incorrectAnswers = 0
    }

}