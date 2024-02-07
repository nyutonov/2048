package uz.gita.lesson20.utils

import uz.gita.lesson20.R

class FindDrawable {

    private val hashMap = hashMapOf(
        0 to R.drawable.btn_0 ,
        2 to R.drawable.btn_2 ,
        4 to R.drawable.btn_4 ,
        8 to R.drawable.btn_8 ,
        16 to R.drawable.btn_16 ,
        32 to R.drawable.btn_32 ,
        64 to R.drawable.btn_64 ,
        128 to R.drawable.btn_128 ,
        256 to R.drawable.btn_256 ,
        512 to R.drawable.btn_512 ,
        1024 to R.drawable.btn_1024 ,
        2028 to R.drawable.btn_2048
    )

    fun getDrawable(num : Int) : Int = hashMap.getOrDefault(num , R.drawable.btn_default)
}