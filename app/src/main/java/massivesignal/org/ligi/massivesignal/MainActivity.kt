package massivesignal.org.ligi.massivesignal

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.ligi.kaxt.doOnProgressChanged

class MainActivity : AppCompatActivity() {

    lateinit var sirenThread: SirenThread

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sirenThread = SirenThread()

        parameterSeek.doOnProgressChanged { progress, fromUser ->
            if (fromUser) sirenThread.parameter = progress / parameterSeek.max.toDouble()
        }

        startButton.setOnClickListener {
            if ( sirenThread.isRunning) {
                sirenThread.isRunning = false
                sirenThread = SirenThread()
            } else {
                sirenThread.start()
            }
        }

    }

    public override fun onDestroy() {
        super.onDestroy()
        sirenThread.isRunning = false
    }

}