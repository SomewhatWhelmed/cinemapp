package com.example.cinemapp.ui.main.movie_details

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemapp.R
import com.example.cinemapp.databinding.DialogCrewItemBinding
import com.example.cinemapp.ui.main.model.CrewMember
import com.example.cinemapp.util.mapDpToPixel

class CrewAdapter(val context: Context) : RecyclerView.Adapter<CrewAdapter.CrewViewHolder>() {

    private var crew: List<CrewMember> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setCrew(crew: List<CrewMember>) {
        this.crew = crew
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return crew.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrewViewHolder {
        val binding = DialogCrewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CrewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CrewViewHolder, position: Int) {
        holder.bind(crew[position])
    }

    inner class CrewViewHolder(private val binding: DialogCrewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(crewMember: CrewMember) {
            with(binding) {
                tvName.text = crewMember.name
                val hPadding = mapDpToPixel(20f, context)
                val vPadding = mapDpToPixel(5f, context)
                if (crewMember.name == crewMember.job) {
                    tvName.setTypeface(Typeface.DEFAULT_BOLD)
                    binding.divider.isVisible = true
                    tvName.setTextColor(ContextCompat.getColor(context, R.color.md_theme_tertiary))
                    tvName.setPadding(
                        hPadding,
                        2 * vPadding,
                        hPadding,
                        vPadding
                    )
                } else {
                    tvName.setTypeface(Typeface.DEFAULT)
                    binding.divider.isVisible = false
                    tvName.setTextColor(ContextCompat.getColor(context, R.color.md_theme_secondary))
                    tvName.setPadding(
                        hPadding,
                        vPadding / 2,
                        hPadding,
                        0
                    )
                }
            }
        }
    }
}
