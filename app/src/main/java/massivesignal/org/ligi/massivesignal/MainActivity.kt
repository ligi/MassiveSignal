package massivesignal.org.ligi.massivesignal

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jsyn.JSyn
import com.jsyn.devices.android.AndroidAudioForJSyn
import com.jsyn.unitgen.LineOut
import com.jsyn.unitgen.SineOscillator
import kotlinx.android.synthetic.main.activity_main.*
import org.ligi.kaxt.doOnProgressChanged

class MainActivity : AppCompatActivity() {

    private val synthesizer by lazy { JSyn.createSynthesizer(AndroidAudioForJSyn()) }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        synthesizer.start()

        val lineOut = LineOut()
        synthesizer.add(lineOut)


        val sineOscillator = SineOscillator()
        synthesizer.add(sineOscillator)

        sineOscillator.output.connect(0,lineOut.input,0)
        sineOscillator.output.connect(0,lineOut.input,1)

        sineOscillator.frequency.set(400.0)
        sineOscillator.amplitude.set(0.5)

        lineOut.start()

        parameterSeek.doOnProgressChanged { progress, fromUser ->
            sineOscillator.frequency.set(.4*progress)
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        synthesizer.stop()
    }

}