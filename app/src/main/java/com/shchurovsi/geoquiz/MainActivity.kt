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
        }

    }

    /**
     * После нажатия на любую кнопку ответа, попыток на ответ больше не будет
     */
    private fun trueAnswer() {
        checkAnswer(true)
        showResult()
    }

    private fun falseAnswer() {
        checkAnswer(false)
        showResult()
    }

    private fun showResult() = with(binding) {
        btTrue.visibility = View.GONE
        btFalse.visibility = View.GONE
        tvResult.visibility = View.VISIBLE
    }

    private fun hideResult() = with(binding) {
        btTrue.visibility = View.VISIBLE
        btFalse.visibility = View.VISIBLE
        tvResult.visibility = View.GONE
    }

    private fun nextQuestion() {

        try {
            currentIndex++
            updateQuestion()
            hideResult()
        } catch (e: IndexOutOfBoundsException) {
            binding.apply {
                showResult()
                tvTitle.visibility = View.GONE
                btNext.visibility = View.GONE
                btPrev.visibility = View.GONE
                tvResult.text = "Wrong answers: $incorrectAnswers, Right answers: $correctAnswers"
            }

        }

    }

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

    // TODO После того как пользователь введет ответ на все вопросы, отобразите уведомление с процентом правильных ответов
}