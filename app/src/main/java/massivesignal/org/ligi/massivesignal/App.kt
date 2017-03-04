package massivesignal.org.ligi.massivesignal

import android.app.Application
import android.support.v7.app.AppCompatDelegate

class App : Application(){
    override fun onCreate() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}