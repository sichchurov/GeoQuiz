package com.shchurovsi.geoquiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.shchurovsi.geoquiz.CheatActivity.ConstanceCheatActivity.ANSWER_STRING
import com.shchurovsi.geoquiz.CheatActivity.ConstanceCheatActivity.CHEATER
import com.shchurovsi.geoquiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this)[QuizViewModel::class.java]
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var getContent: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        updateQuestion()

        binding.apply {
            btTrue.setOnClickListener { trueAnswer() }
            btFalse.setOnClickListener { falseAnswer() }
            btNext.setOnClickListener { nextQuestion() }
            btPrev.setOnClickListener { prevQuestion() }
            btReset.setOnClickListener { restartQuiz() }
            btCheat.setOnClickListener { cheatIntentLaunch() }
        }

        cheatIntentResult()
        viewCheaterAchievement()

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
            tvCheater.visibility = View.INVISIBLE
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
        btReset.visibility = View.VISIBLE
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
            btReset.visibility = View.GONE
        }
    }

    private fun cheatIntentLaunch() {
        getContent?.launch(
            CheatActivity.newIntent(
                this@MainActivity,
                quizViewModel.currentQuestionAnswer
            )
        )
    }

    private fun cheatIntentResult() {

        if (applicationContext == null) return

        getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

                if (result.resultCode == RESULT_OK) {

                    quizViewModel.isCheater =
                        result.data?.getBooleanExtra(CHEATER, false) ?: false

                    quizViewModel.answerString =
                        result.data?.getStringExtra(ANSWER_STRING) ?: ""

                    viewCheaterAchievement()
                }
            }
    }

    private fun viewCheaterAchievement() {
        if (quizViewModel.isCheater) {
            binding.apply {
                tvCheater.visibility = View.VISIBLE
                tvResult.visibility = View.VISIBLE
                tvResult.text = quizViewModel.answerString
                showResult()
            }
        }
    }

}