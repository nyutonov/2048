package uz.gita.lesson20

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uz.gita.lesson20.databinding.MainMenuBinding

class MainMenu : AppCompatActivity() {
    private var _binding: MainMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = MainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.play.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.info.setOnClickListener {
            startActivity(Intent(this, Info::class.java))
        }

        binding.exit.setOnClickListener {
            finish()
        }
    }
}