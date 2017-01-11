package massivesignal.org.ligi.massivesignal

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jsyn.JSyn
import com.jsyn.devices.android.AndroidAudioForJSyn
import com.jsyn.unitgen.LineOut
import com.jsyn.unitgen.SineOscillator
import com.jsyn.unitgen.SineOscillatorPhaseModulated
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

        val carrier = SineOscillatorPhaseModulated()
        synthesizer.add(carrier)

        val modulator = SineOscillator()
        synthesizer.add(modulator)

        modulator.output.connect(carrier.modulation)

        modulator.amplitude.setup(0.0, 1.0, 10.0)
        carrier.amplitude.setup(0.0, 1.0, 10.0)

        carrier.output.connect(0, lineOut.input, 0)
        carrier.output.connect(0, lineOut.input, 1)

        lineOut.start()

        parameterSeek.doOnProgressChanged { progress, fromUser ->
            carrier.frequency.set(.4 * progress)
        }

        parameter2Seek.doOnProgressChanged { progress, fromUser ->
            modulator.frequency.set(.4 * progress)
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        synthesizer.stop()
    }

}