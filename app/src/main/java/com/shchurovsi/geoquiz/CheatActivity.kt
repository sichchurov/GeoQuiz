package com.shchurovsi.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.shchurovsi.geoquiz.CheatActivity.ConstanceCheatActivity.ANSWER_STRING
import com.shchurovsi.geoquiz.CheatActivity.ConstanceCheatActivity.CHEATER
import com.shchurovsi.geoquiz.CheatActivity.ConstanceCheatActivity.TRUE_ANSWER_KEY
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
        answerResult = intent.getBooleanExtra(TRUE_ANSWER_KEY, false)

        tvAnswerResult.text = when(answerResult) {
            true -> getString(R.string.true_button)
            else -> getString(R.string.false_button)
        }
        tvAnswerResult.visibility = View.VISIBLE

        intent.putExtra(CHEATER, true).apply {
            setResult(Activity.RESULT_OK, intent)
        }

        intent.putExtra(ANSWER_STRING, tvAnswerResult.text).apply {
            setResult(Activity.RESULT_OK, intent)
        }
    }

    companion object {
        fun newIntent(context: Context, answerIsTrue: Boolean): Intent {
            return Intent(context, CheatActivity::class.java).apply {
                putExtra(TRUE_ANSWER_KEY, answerIsTrue)
            }
        }
    }

    object ConstanceCheatActivity {
        const val CHEATER = "answer_show"
        const val TRUE_ANSWER_KEY = "answer_is_true"
        const val ANSWER_STRING = "answer_string"
    }
}