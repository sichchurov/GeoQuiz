package com.shchurovsi.geoquiz

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.shchurovsi.geoquiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val questionBank = arrayListOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private var currentIndex = 0
    private var correctAnswers = 0
    private var incorrectAnswers = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateQuestion()

        binding.apply {
            btTrue.setOnClickListener { trueAnswer() }
            btFalse.setOnClickListener { falseAnswer() }
            btNext.setOnClickListener { nextQuestion() }
            btPrev.setOnClickListener { prevQuestion() }
            btRestartQuiz?.setOnClickListener { restartQuiz() }
        }

    }

    private fun trueAnswer() {
        checkAnswer(true)
        showResult()
    }

    private fun falseAnswer() {
        checkAnswer(false)
        showResult()
    }

    /**
     * Hide true and false buttons for showing the result
     */
    private fun showResult() = with(binding) {
        btTrue.visibility = View.GONE
        btFalse.visibility = View.GONE
        tvResult.visibility = View.VISIBLE
    }

    /**
     * Hide the result for showing buttons with a question
     */
    private fun hideResult() = with(binding) {
        tvTitle.visibility = View.VISIBLE
        btTrue.visibility = View.VISIBLE
        btFalse.visibility = View.VISIBLE
        tvResult.visibility = View.GONE
    }

    /**
     * After the last question the quiz is finished
     */
    private fun nextQuestion() {
        try {
            currentIndex++
            updateQuestion()
            hideResult()
        } catch (e: IndexOutOfBoundsException) {
            finishQuiz()
        }

    }

    /**
     * Show statistic with the answers
     */
    private fun finishQuiz() {
        binding.apply {
            showResult()
            tvTitle.visibility = View.GONE
            btNext.visibility = View.GONE
            btPrev.visibility = View.GONE
            btRestartQuiz?.visibility = View.VISIBLE
            tvResult.text = getString(R.string.statistic, "$incorrectAnswers", "$correctAnswers")
        }
    }

    /**
     * Here is an opportunity to restart a quiz
     */
    private fun restartQuiz() {
        incorrectAnswers = 0
        correctAnswers = 0
        currentIndex = 0

        binding.apply {
            hideResult()
            updateQuestion()
            btNext.visibility = View.VISIBLE
            btPrev.visibility = View.VISIBLE
            btRestartQuiz?.visibility = View.GONE
        }
    }

    // TODO show the questions that has already answered
    private fun prevQuestion() {
        if (currentIndex == 0) {
            return
        }
        currentIndex = (currentIndex - 1) % questionBank.size
        updateQuestion()
        hideResult()
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        binding.tvTitle.setText(questionTextResId)
    }


    private fun checkAnswer(userAnswer: Boolean) = with(binding) {
        val rightAnswer = questionBank[currentIndex].answer

        if (userAnswer == rightAnswer) {
            correctAnswers++
            tvResult.text = getString(R.string.true_answer)
        } else {
            incorrectAnswers++
            tvResult.text = getString(R.string.false_answer)
        }
    }

}