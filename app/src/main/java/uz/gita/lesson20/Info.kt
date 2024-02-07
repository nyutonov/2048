package uz.gita.lesson20

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uz.gita.lesson20.databinding.InfoBinding

class Info : AppCompatActivity() {
    private var _binding: InfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = InfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }
    }
}