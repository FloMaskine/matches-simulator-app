package com.flomaskine.matchessimulator.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.flomaskine.matchessimulator.databinding.ActivityDetailBinding
import com.flomaskine.matchessimulator.domain.Match

class DetailActivity : AppCompatActivity() {

    object Extras {
        const val MATCH = "EXTRA_MATCH"
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadMatchFromExtra()

    }

    private fun loadMatchFromExtra() {
        intent?.extras?.getParcelable<Match>(Extras.MATCH).let {
            supportActionBar?.title = it?.place?.name
            Glide.with(this).load(it?.place?.image).into(binding.ivPlace)
            binding.tvDescription.text = it?.description

            Glide.with(this).load(it?.homeTeam?.image).into(binding.ivHomeTeam)
            binding.tvHomeTeamName.text = it?.homeTeam?.name
            if (it != null) {
                binding.rbHomeTeamStars.rating = it.homeTeam.stars.toFloat()
            }
            if (it != null) {
                binding.tvHomeTeamScore.text = it.homeTeam.score.toString()
            }


            Glide.with(this).load(it?.awayTeam?.image).into(binding.ivAwayTeam)
            binding.tvAwayTeamName.text = it?.awayTeam?.name
            if (it != null) {
                binding.rbAwayTeamStars.rating = it.awayTeam.stars.toFloat()
            }
            if (it != null) {
                binding.tvAwayTeamScore.text = it.awayTeam.score.toString()
            }

        }
    }
}