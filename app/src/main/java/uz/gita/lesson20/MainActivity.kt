package uz.gita.lesson20

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import uz.gita.lesson20.controller.AppController
import uz.gita.lesson20.databinding.ActivityMainBinding
import uz.gita.lesson20.model.SideEnum
import uz.gita.lesson20.pref.Mypref
import uz.gita.lesson20.utils.FindDrawable
import uz.gita.lesson20.utils.MyTouchListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val views = ArrayList<TextView>(4)
    private val controller = AppController.getInstance()
    private val myPref = Mypref.getShared()

    @SuppressLint("DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.numScore.text = myPref?.getInt("score", 0).toString()
        binding.numHigh.text = myPref?.getInt("record", 0).toString()

        if (myPref?.getBoolean("isLose", false)!!) {
            binding.text.text = "Вы проиграли"
            binding.mainLayout.alpha = 0.5f
            binding.text.isVisible = true
            binding.mainLayout.isEnabled = false
            binding.btnRestart.background.setColorFilter(
                Color.parseColor("#949AAA"),
                PorterDuff.Mode.SRC_ATOP
            );
        }

        if (!myPref.getBoolean("check", false)) {
            attachTouchListener()
            loadViews()
            describeMatrixToUI()
        } else {
            val s = myPref.getString("numbers", "")?.split("/")
            loadViews()
            attachTouchListener()

            for (i in 0 until 16) {
                views[i].apply {
                    if (!s?.get(i).equals("")) {
                        if (s!![i] == "0") {
                            text = ""
                        } else {
                            this.setTextColor(
                                resources.getInteger(
                                    resources.getIdentifier(
                                        "_${s[i]}",
                                        "color",
                                        packageName
                                    )
                                )
                            )
                            text = s[i]
                            this.setBackgroundResource(FindDrawable().getDrawable(s[i].toInt()))
                        }
                    }
                }
            }
            controller.saveNewMatrix()
        }

        onClick()
        error()
    }

    private fun loadViews() {
        for (i in 0 until binding.mainLayout.childCount) {
            val linear = binding.mainLayout.getChildAt(i) as LinearLayoutCompat
            for (j in 0 until linear.childCount) {
                views.add(linear.getChildAt(j) as TextView)
            }
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun describeMatrixToUI() {
        val matrix = controller.getMatrix()
        for (i in matrix.indices) {
            for (j in 0 until matrix[i].size) {
                views[i * matrix.size + j].apply {
                    if (matrix[i][j] == 0) {
                        this.text = ""
                    } else {
                        this.text = matrix[i][j].toString()
                        this.setTextColor(
                            resources.getInteger(
                                resources.getIdentifier(
                                    "_${matrix[i][j]}",
                                    "color",
                                    packageName
                                )
                            )
                        )
                    }
                    this.setBackgroundResource(FindDrawable().getDrawable(matrix[i][j]))
                }

                if (matrix[i][j] == 2048) {
                    if (myPref!!.getBoolean("isWin", true)) {
                        binding.mainLayout.alpha = 0.5f
                        binding.text.text = "Вы выиграли"
                        binding.text.isVisible = true
                        binding.text2.isVisible = true
                        myPref.edit().putBoolean("isWin", false).apply()

                        binding.root.isEnabled = false

                        binding.mainLayout.setOnClickListener {
                            binding.text.isVisible = false
                            binding.text2.isVisible = false
                            binding.mainLayout.alpha = 1f
                            binding.root.isEnabled = true
                            binding.mainLayout.isClickable = false
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun attachTouchListener() {
        val listener = MyTouchListener(this)
        binding.root.setOnTouchListener(listener)
        listener.setHandleSideEnumListener { side ->
            when (side) {
                SideEnum.RIGHT -> {
                    controller.movementToRight()
                    describeMatrixToUI()
                    showCount()
                }

                SideEnum.LEFT -> {
                    controller.movementToLeft()
                    describeMatrixToUI()
                    showCount()
                }

                SideEnum.UP -> {
                    controller.movementToUp()
                    describeMatrixToUI()
                    showCount()
                }

                SideEnum.DOWN -> {
                    controller.movementToDown()
                    describeMatrixToUI()
                    showCount()
                }
            }

            if (controller.score!! > controller.record!!) {
                controller.record = controller.score
                binding.numHigh.text = controller.record.toString()
            }
        }
    }

    private fun showCount() {
        binding.numScore.text = controller.score.toString()
    }

    private fun onClick() {
        binding.btnRestart.setOnClickListener {
            binding.btnRestart.background.setColorFilter(
                Color.parseColor("#475882"),
                PorterDuff.Mode.SRC_ATOP
            );
            binding.root.isEnabled = true
            binding.mainLayout.isClickable = false

            binding.text.isVisible = false
            binding.text2.isVisible = false
            binding.mainLayout.alpha = 1f
            myPref!!.edit().putBoolean("isWin", true).apply()
            myPref.edit()?.putBoolean("isLose", false)?.apply()
            controller.clear()
            binding.numScore.text = "0"
            showCount()
            controller.reverseAllItem()
            describeMatrixToUI()
        }

        binding.home.setOnClickListener {
            finish()
        }
    }

    private fun error() {
        controller.setErrorListener {
            binding.text.text = "Вы проиграли"
            binding.mainLayout.alpha = 0.5f
            binding.text.isVisible = true
            myPref?.edit()?.putBoolean("isLose", true)?.apply()
            binding.btnRestart.background.setColorFilter(
                Color.parseColor("#949AAA"),
                PorterDuff.Mode.SRC_ATOP
            );
        }
    }

    override fun onPause() {
        super.onPause()
        controller.save()
    }
}