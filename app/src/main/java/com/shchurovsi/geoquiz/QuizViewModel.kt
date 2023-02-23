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

    var currentIndex = 0
    var correctAnswers = 0
    var incorrectAnswers = 0

    val currentQuestionText get() = questionBank[currentIndex].textResId
    val currentQuestionAnswer get() = questionBank[currentIndex].answer

    fun moveNext() {
        currentIndex++
    }

    fun moveBack() {
        currentIndex--
    }

}