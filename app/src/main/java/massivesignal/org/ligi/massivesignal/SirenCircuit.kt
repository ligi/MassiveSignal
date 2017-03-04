package massivesignal.org.ligi.massivesignal

import com.jsyn.unitgen.Circuit
import com.jsyn.unitgen.SineOscillator
import com.jsyn.unitgen.SquareOscillator
import com.jsyn.unitgen.UnitSource

class SirenCircuit : Circuit(), UnitSource {

    internal val myLFO = SineOscillator()
    internal val carrier = SquareOscillator()
    internal val reverb = ReverbCircuit()

    init {
        add(myLFO)
        add(carrier)
        add(reverb)

        addPort(myLFO.frequency, "freq")
        addPort(myLFO.amplitude, "amp")
        addPort(reverb.intDelay.delay, "reverb delay")
        addPort(reverb.m1.inputB, "reverb b")

        myLFO.frequency.setup(0.1, 0.42, 2.0)
        myLFO.amplitude.setup(1.0, 500.0, 900.0)
        reverb.m1.inputB.setup(0.0,0.0,1.0)

        reverb.input.connect(carrier.output)
        myLFO.output.connect(carrier.frequency)
    }

    override fun getOutput() = reverb.output!!
}
