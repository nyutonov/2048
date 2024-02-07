package uz.gita.lesson20.app

import android.app.Application
import uz.gita.lesson20.pref.Mypref

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Mypref.init(this)
    }
}