package com.shchurovsi.geoquiz

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.shchurovsi.geoquiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this)[QuizViewModel::class.java]
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        updateQuestion()

        binding.apply {
            btTrue.setOnClickListener { trueAnswer() }
            btFalse.setOnClickListener { falseAnswer() }
            btNext.setOnClickListener { nextQuestion() }
            btPrev.setOnClickListener { prevQuestion() }
            btReset?.setOnClickListener { restartQuiz() }
            btCheat.setOnClickListener {

            }
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

    private fun mark() {
        binding.apply {
            btTrue.isActivated = false
            btTrue.alpha = 0.5f
        }
    }

    /**
     * Hide answers buttons for showing the result
     */
    private fun showResult() {
        binding.apply {
            btTrue.visibility = View.GONE
            btFalse.visibility = View.GONE
            btCheat.visibility = View.GONE
            tvResult.visibility = View.VISIBLE
            
        }
    }

    /**
     * Hide the result for showing buttons with answers
     */
    private fun hideResult() {
        binding.apply {
            tvTitle.visibility = View.VISIBLE
            btTrue.visibility = View.VISIBLE
            btFalse.visibility = View.VISIBLE
            btCheat.visibility = View.VISIBLE
            tvResult.visibility = View.GONE
        }
    }

    private fun updateQuestion() {
        binding.tvTitle.setText(quizViewModel.currentQuestionText)
    }

    private fun checkAnswer(userAnswer: Boolean) = with(binding) {
        val rightAnswer = quizViewModel.currentQuestionAnswer

        if (userAnswer == rightAnswer) {
            quizViewModel.increaseCorrectAnswers()
            tvResult.text = getString(R.string.true_answer)
        } else {
            quizViewModel.increaseIncorrectAnswers()
            tvResult.text = getString(R.string.false_answer)
        }
    }

    /**
     * After the last question the quiz is finished
     */
    private fun nextQuestion() {
        try {
            quizViewModel.increaseIndex()
            updateQuestion()
            hideResult()
        } catch (e: IndexOutOfBoundsException) {
            finishQuiz()
        }
    }

    // TODO don't show the questions that has already answered
    private fun prevQuestion() {
        if (quizViewModel.currentIndex == 0) {
            return
        }
        quizViewModel.decreaseIndex()
        updateQuestion()
        hideResult()
    }

    /**
     * Show statistic with the answers
     */
    private fun finishQuiz() = with(binding) {
        showResult()
        tvTitle.visibility = View.GONE
        btNext.visibility = View.GONE
        btPrev.visibility = View.GONE
        btReset?.visibility = View.VISIBLE
        tvResult.text = getString(
            R.string.statistic,
            "${quizViewModel.incorrectAnswers}",
            "${quizViewModel.correctAnswers}"
        )

        quizViewModel.reset()
    }

    /**
     * Here is an opportunity to restart a quiz
     */
    private fun restartQuiz() {
        binding.apply {
            hideResult()
            updateQuestion()
            btNext.visibility = View.VISIBLE
            btPrev.visibility = View.VISIBLE
            btReset?.visibility = View.GONE
        }
    }



}