package uz.gita.lesson20.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import uz.gita.lesson20.model.SideEnum

class MyTouchListener(context: Context) : View.OnTouchListener {
    private val myGestureDetector = GestureDetector(context, MySimpleGestureDetector())
    private var handleSideEnumListener: ((SideEnum) -> Unit)? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        myGestureDetector.onTouchEvent(event)
        return true
    }

    inner class MySimpleGestureDetector : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (kotlin.math.abs(e1.x - e2.x) > 100 || kotlin.math.abs(e1.y - e2.y) > 100) {
                if (kotlin.math.abs(e1.x - e2.x) > kotlin.math.abs(e1.y - e2.y)) {   // horizontal
                    if (e1.x > e2.x) { // left
                        handleSideEnumListener?.invoke(SideEnum.LEFT)
                    } else { // right
                        handleSideEnumListener?.invoke(SideEnum.RIGHT)
                    }
                } else {   // vertical
                    if (e1.y > e2.y) { // up
                        handleSideEnumListener?.invoke(SideEnum.UP)
                    } else { // down
                        handleSideEnumListener?.invoke(SideEnum.DOWN)
                    }
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY)
        }
    }

    fun setHandleSideEnumListener(block: (SideEnum) -> Unit) {
        handleSideEnumListener = block
    }
}
