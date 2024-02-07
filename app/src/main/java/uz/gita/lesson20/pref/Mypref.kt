package uz.gita.lesson20.pref

import android.content.Context
import android.content.SharedPreferences

class Mypref {

    private constructor(context : Context) {
        mypref = context.getSharedPreferences("MyPref" , Context.MODE_PRIVATE)
    }

    companion object {
        var mypref : SharedPreferences ?= null
        private var pref : Mypref?= null

        fun init(context: Context) {
            pref = Mypref(context)
        }

        fun getShared() : SharedPreferences? = mypref
    }

}