package com.shchurovsi.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.shchurovsi.geoquiz.databinding.ActivityCheatBinding

class CheatActivity : AppCompatActivity() {

    private var answerResult = false

    private val binding: ActivityCheatBinding by lazy {
        ActivityCheatBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btShowAnswer.setOnClickListener { getAnswerResult() }
    }

    private fun getAnswerResult() = with(binding) {
        answerResult = intent.getBooleanExtra(Constance.TRUE_ANSWER_KEY, false)
        tvAnswerResult.text = when(answerResult) {
            true -> getString(R.string.true_button)
            else -> getString(R.string.false_button)
        }
        tvAnswerResult.visibility = View.VISIBLE

        intent.putExtra(Constance.CHEATER, getString(R.string.cheater)).apply {
            setResult(Activity.RESULT_OK, intent)
        }
    }

    companion object {
        fun newIntent(context: Context, answerIsTrue: Boolean): Intent {
            return Intent(context, CheatActivity::class.java).apply {
                putExtra(Constance.TRUE_ANSWER_KEY, answerIsTrue)
            }
        }
    }
}