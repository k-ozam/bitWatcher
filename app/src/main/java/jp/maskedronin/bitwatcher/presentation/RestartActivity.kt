package jp.maskedronin.bitwatcher.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import androidx.appcompat.app.AppCompatActivity

/**
 * @see [https://qiita.com/Shiozawa/items/85f078ed57aed46f6b69]
 */
private const val EXTRA_MAIN_PID = "RestartActivity.main_pid"

class RestartActivity : AppCompatActivity() {
    companion object {
        fun start(context: Context) {
            Intent(context, RestartActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                // メインプロセスの PID を Intent に保存しておく
                .putExtra(EXTRA_MAIN_PID, Process.myPid())
                .also { intent ->
                    context.startActivity(intent)
                }
        }
    }

    private val mainPid: Int
        get() = run {
            require(intent.hasExtra(EXTRA_MAIN_PID))
            intent.getIntExtra(EXTRA_MAIN_PID, Int.MIN_VALUE)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1. メインプロセスを Kill する
        Process.killProcess(mainPid)

        // 2. MainActivity を再起動する
        val restartIntent = Intent(this, MainActivity::class.java)
            .setAction(Intent.ACTION_MAIN)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(restartIntent)

        // 3. RestartActivity を終了する
        finish()
        Process.killProcess(Process.myPid())
    }
}