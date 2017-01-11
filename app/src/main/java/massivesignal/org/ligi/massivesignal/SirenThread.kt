package massivesignal.org.ligi.massivesignal

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack

class SirenThread : Thread() {

    internal var sampleRate = 44100

    internal var parameter: Double = 0.toDouble()
    var isRunning = false

    override fun run() {
        isRunning = true
        priority = MAX_PRIORITY
        val bufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT)

        val audioTrack = AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize,
                AudioTrack.MODE_STREAM)

        val samples = ShortArray(bufferSize)
        val amp = 10000
        val twopi = 8.0 * Math.atan(1.0)
        var ph = 0.0

        audioTrack.play()

        while (isRunning) {
            val fr = 440 + 440 * parameter
            for (i in 0..bufferSize - 1) {
                samples[i] = (amp * Math.sin(ph)).toShort()
                ph += twopi * fr / sampleRate
            }
            audioTrack.write(samples, 0, bufferSize)
        }
        audioTrack.stop()
        audioTrack.release()
    }
}