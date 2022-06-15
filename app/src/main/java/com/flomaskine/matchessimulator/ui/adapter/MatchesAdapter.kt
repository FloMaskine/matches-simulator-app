package com.flomaskine.matchessimulator.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flomaskine.matchessimulator.databinding.MatchItemBinding
import com.flomaskine.matchessimulator.domain.Match
import com.flomaskine.matchessimulator.ui.DetailActivity

class MatchesAdapter(val match: List<Match>?) :
    RecyclerView.Adapter<MatchesAdapter.MatchViewholder>() {


    inner class MatchViewholder(val matchBinding: MatchItemBinding) :
        RecyclerView.ViewHolder(matchBinding.root) {
        fun bind(match: Match) {
            with(match) {
                matchBinding.nameHomeTeam.text = match.homeTeam.name
                matchBinding.nameAwayTeam.text = match.awayTeam.name
                matchBinding.scoreHomeTeam.text = match.homeTeam.score.toString()
                matchBinding.scoreAwayTeam.text = match.awayTeam.score.toString()
            }
        }

    }

    override fun onBindViewHolder(holder: MatchViewholder, position: Int) {
        holder.bind(match!![position])
        val context: Context = holder.itemView.context
        val matches: Match = match[position]

        // Adapta os dados da partida (recuperada da API) para o nosso layout.
        Glide.with(context).load(matches.homeTeam.image).circleCrop()
            .into(holder.matchBinding.ivHomeTeam)
        holder.matchBinding.nameHomeTeam.text = matches.homeTeam.name
        Glide.with(context).load(matches.awayTeam.image).circleCrop()
            .into(holder.matchBinding.ivAwayTeam)
        holder.matchBinding.nameAwayTeam.text = matches.awayTeam.name
        // Detail Activity
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.Extras.MATCH, matches)
            context.startActivity(intent)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewholder {
        val binding = MatchItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchViewholder(binding)
    }

    override fun getItemCount(): Int = match!!.size
}