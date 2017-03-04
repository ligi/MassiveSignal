package massivesignal.org.ligi.massivesignal

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import android.widget.TextView
import com.jsyn.JSyn
import com.jsyn.devices.android.AndroidAudioForJSyn
import com.jsyn.ports.UnitInputPort
import com.jsyn.unitgen.LineOut
import kotlinx.android.synthetic.main.activity_main.*
import org.ligi.kaxt.doOnProgressChanged

class MainActivity : AppCompatActivity() {

    private val synthesizer by lazy { JSyn.createSynthesizer(AndroidAudioForJSyn()) }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lineOut = LineOut()
        synthesizer.add(lineOut)

        val sirenCircuit = SirenCircuit()
        synthesizer.add(sirenCircuit.unitGenerator)

        sirenCircuit.output.connect(0, lineOut.input, 0)
        sirenCircuit.output.connect(0, lineOut.input, 1)

        sirenCircuit.ports.forEachIndexed { i, unitPort ->
            if (unitPort is UnitInputPort) {
                val textView = TextView(this)
                val baseText = unitPort.name + " (${unitPort.minimum} / ${unitPort.maximum})"
                textView.text = baseText
                controlContainer.addView(textView)

                val seek = SeekBar(this)
                seek.max = (1000).toInt()
                val range = unitPort.maximum - unitPort.minimum
                seek.progress = ((unitPort.value / range) * 1000).toInt()

                seek.doOnProgressChanged { progress, fromUser ->
                    val value = unitPort.minimum + (range / 1000.0) * progress
                    textView.text = baseText + value
                    unitPort.set(value)
                }

                controlContainer.addView(seek)

            }
        }

        lineOut.start()

        startButton.setOnClickListener {
            synthesizer.start()
        }


    }

    public override fun onDestroy() {
        super.onDestroy()
        synthesizer.stop()
    }

}