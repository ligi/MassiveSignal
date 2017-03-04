/*
 * Copyright 1997 Phil Burk, Mobileer Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package massivesignal.org.ligi.massivesignal;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.Add;
import com.jsyn.unitgen.Circuit;
import com.jsyn.unitgen.InterpolatingDelay;
import com.jsyn.unitgen.MixerMonoRamped;
import com.jsyn.unitgen.Multiply;
import com.jsyn.unitgen.UnitSource;

/**
 * Wind Sound Create a wind-like sound by feeding white noise "shshshshsh" through a randomly
 * varying state filter to make a "whooowhoosh" sound. The cuttoff frequency of the low pass filter
 * is controlled by a RedNoise delay which creates a slowly varying random control signal.
 * 
 * @author (C) 1997 Phil Burk, SoftSynth.com
 */

public class ReverbCircuit extends Circuit implements UnitSource {

    public final UnitInputPort input;
    public final UnitOutputPort output;

    Add adder1;
    Add adder2;
    MixerMonoRamped mix;
    InterpolatingDelay intDelay;
    Multiply m1;
    Multiply m2;
    Multiply initialInputSplitter;
    float fade = 0.9f;
    double delayValue = 0.05;

    public ReverbCircuit(){
        initialInputSplitter = new Multiply();
        add(initialInputSplitter);
        m1 = new Multiply();
        m2 = new Multiply();
        add(m1);
        add(m2);

        intDelay = new InterpolatingDelay();
        add(intDelay);

        input = initialInputSplitter.inputA;
        initialInputSplitter.inputB.set(1);
        adder1 = new Add();
        adder2 = new Add();

        add(adder2);
        add(adder1);

        adder1.inputA.connect(initialInputSplitter.output);

        intDelay.input.connect(adder1.output);
        intDelay.allocate(88200);

        intDelay.delay.set(delayValue);


        m1.inputA.connect(intDelay.output);
        m1.inputB.set(fade);

        adder1.inputB.connect(m1.output);

        m2.inputA.connect(intDelay.output);
        m2.inputB.set(fade);


        mix=new MixerMonoRamped(2);
        add(mix);
        mix.input.connect(0,m2.output,0);
        mix.input.connect(1,initialInputSplitter.output,0);
        //adder2.inputA.connect(m2.output);
        //adder2.inputB.connect(initialInputSplitter.output);

        output = mix.output;
    }

    public void setDelay(double sec){
        intDelay.delay.set(sec);
    }

    public void setFade(double val){
        m1.inputB.set(val);
    }
    @Override
    public UnitOutputPort getOutput() {
        return output;
    }
}
