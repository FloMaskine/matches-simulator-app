package com.flomaskine.matchessimulator.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.flomaskine.matchessimulator.R
import com.flomaskine.matchessimulator.data.MatchesAPI
import com.flomaskine.matchessimulator.databinding.ActivityMainBinding
import com.flomaskine.matchessimulator.domain.Match
import com.flomaskine.matchessimulator.ui.adapter.MatchesAdapter
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var matchesAPI: MatchesAPI
    private var matchesAdapter: MatchesAdapter = MatchesAdapter(Collections.emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupHttpClient()
        setupFloatingActionButton()
        setupMatchesList()
        setupMatchesRefresh()

    }

    private fun setupFloatingActionButton() {
        binding.fabSimulate.setOnClickListener { view ->
            view.animate().rotationBy(360F).setDuration(500)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        for (i in 0 until matchesAdapter.itemCount) {
                            val match: Match = matchesAdapter.match!![i]
                            val homeStars = match.homeTeam.stars + 1
                            val awayStars = match.awayTeam.stars + 1
                            val randomHome = (0..homeStars).shuffled().last()
                            val randomAway = (0..awayStars).shuffled().last()
                            match.homeTeam.score = randomHome
                            match.awayTeam.score = randomAway
                            matchesAdapter.notifyItemChanged(i)
                        }
                    }
                })

        }
    }


    private fun setupHttpClient() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://flomaskine.github.io/matches-simulator_api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        matchesAPI = retrofit.create(MatchesAPI::class.java)
    }

    private fun setupMatchesList() {
        binding.rvMatches.setHasFixedSize(true)
        binding.rvMatches.layoutManager = LinearLayoutManager(this)
        binding.rvMatches.adapter = matchesAdapter
        findMatchesFromApi()

    }

    private fun setupMatchesRefresh() {
        binding.srlMatches.setOnRefreshListener(this::findMatchesFromApi)
    }

    private fun findMatchesFromApi() {
        binding.srlMatches.isRefreshing
        matchesAPI.getMatches().enqueue(object : Callback<List<Match>> {

            override fun onResponse(call: Call<List<Match>>, response: Response<List<Match>>) {
                if (response.isSuccessful) {
                    val matches: List<Match>? = response.body()
                    matchesAdapter = MatchesAdapter(matches)
                    binding.rvMatches.adapter = matchesAdapter
                } else {
                    Log.d("DEU TUDO ERRADO", "DEU TUDO ERRADO")
                    showErrorMessage()
                }
                binding.srlMatches.isRefreshing = false
            }

            override fun onFailure(call: Call<List<Match>>, t: Throwable) {
                showErrorMessage()
                binding.srlMatches.isRefreshing = false
            }
        })

    }

    fun showErrorMessage() {
        Snackbar.make(binding.fabSimulate, R.string.error_api, Snackbar.LENGTH_LONG).show()
    }

}


