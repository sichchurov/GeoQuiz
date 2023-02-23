package com.shchurovsi.geoquiz

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.shchurovsi.geoquiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this)[QuizViewModel::class.java]
    }

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
            quizViewModel.moveNext()
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
            tvResult.text = getString(
                R.string.statistic,
                "${quizViewModel.incorrectAnswers}",
                "${quizViewModel.correctAnswers}"
            )
        }
    }

    /**
     * Here is an opportunity to restart a quiz
     */
    private fun restartQuiz() {
        quizViewModel.incorrectAnswers = 0
        quizViewModel.correctAnswers = 0
        quizViewModel.currentIndex = 0

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
        if (quizViewModel.currentIndex == 0) {
            return
        }
        quizViewModel.moveBack()
        updateQuestion()
        hideResult()
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.tvTitle.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) = with(binding) {
        val rightAnswer = quizViewModel.currentQuestionAnswer

        if (userAnswer == rightAnswer) {
            quizViewModel.correctAnswers++
            tvResult.text = getString(R.string.true_answer)
        } else {
            quizViewModel.incorrectAnswers++
            tvResult.text = getString(R.string.false_answer)
        }
    }

}