package com.example.dices
import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pl.droidsonroids.gif.GifImageView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var myButton: Button
    private lateinit var dice: ImageView
    private lateinit var counter: TextView
    private lateinit var partyPopperSound: MediaPlayer
    private lateinit var partyPopperImageView: GifImageView

    private val one = R.drawable.one
    private val two = R.drawable.two
    private val three = R.drawable.three
    private val four = R.drawable.four
    private val five = R.drawable.five
    private val six = R.drawable.six

    private val handler = Handler(Looper.getMainLooper())

    private var currentImage = 0
    private var delay: Long = 100
    private var isAnimating = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        myButton = findViewById(R.id.button)
        dice = findViewById(R.id.imageView)
        counter = findViewById(R.id.counter)
        partyPopperImageView = findViewById(R.id.partyPopperImageView)
        partyPopperSound = MediaPlayer.create(this, R.raw.party_popper)

        partyPopperImageView.visibility = GifImageView.INVISIBLE

        dice.setImageResource(one)
        counter.visibility = TextView.INVISIBLE


        myButton.setOnClickListener {
            if (!isAnimating) {
                startRandomizationAnimation()

            }
        }
    }
    private fun startRandomizationAnimation() {
        isAnimating = true
        currentImage = 1
        delay = 100
        counter.visibility = TextView.INVISIBLE
        partyPopperImageView.visibility = GifImageView.INVISIBLE
        handler.post(randomizeImageRunnable)
    }


    private val randomizeImageRunnable = object : Runnable {
        override fun run() {
            currentImage = Random.nextInt(1, 7)


            when (currentImage) {
                1 -> dice.setImageResource(one)
                2 -> dice.setImageResource(two)
                3 -> dice.setImageResource(three)
                4 -> dice.setImageResource(four)
                5 -> dice.setImageResource(five)
                6 -> dice.setImageResource(six)
            }


            delay += 100

            if (delay <= 1000) {
                handler.postDelayed(this, delay)
            } else {

                isAnimating = false

                when (currentImage) {
                    1 -> counter.text = "Your dice is 1!"
                    2 -> counter.text = "Your dice is 2!"
                    3 -> counter.text = "Your dice is 3!"
                    4 -> counter.text = "Your dice is 4!"
                    5 -> counter.text = "Your dice is 5!"
                    6 -> counter.text = "Your dice is 6!"
                }
                counter.visibility = TextView.VISIBLE
                partyPopperImageView.visibility = GifImageView.VISIBLE
                partyPopperSound.start()
                handler.postDelayed({
                    partyPopperImageView.visibility = GifImageView.INVISIBLE
                }, 2000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::partyPopperSound.isInitialized) {
            partyPopperSound.release()
        }
    }
}
