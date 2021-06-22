package com.ivan.animals.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivan.animals.R
import com.ivan.animals.model.Animal
import kotlinx.android.synthetic.main.animal_item.view.*

class AnimalAdapter(val animals: ArrayList<Animal>): RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AnimalViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.animal_item, parent, false))

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.onBind(animals[position])
    }

    override fun getItemCount() = animals.count()

    fun onRefreshAnimals(refreshAnimals: List<Animal>){
        animals.clear()
        animals.addAll(refreshAnimals)
        notifyDataSetChanged()
    }

    inner class AnimalViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val image: ImageView = itemView.iv_animal
        val animalName: TextView = itemView.tv_animal

        fun onBind(animal: Animal){
            animalName.text = animal.name
        }
    }
}