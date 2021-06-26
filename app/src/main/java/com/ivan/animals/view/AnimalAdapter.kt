package com.ivan.animals.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ivan.animals.R
import com.ivan.animals.databinding.AnimalItemBinding
import com.ivan.animals.model.Animal

class AnimalAdapter(val animals: ArrayList<Animal>) :
    RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>(), AnimalClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AnimalViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.animal_item,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.animalItemBinding.animal = animals[position]
        holder.animalItemBinding.listener = this
    }

    override fun getItemCount() = animals.count()

    fun onRefreshAnimals(refreshAnimals: List<Animal>) {
        animals.clear()
        animals.addAll(refreshAnimals)
        notifyDataSetChanged()
    }

    override fun onAnimalClicked(view: View) {
        for(animal in animals){
            if (animal.name == view.tag) {
                val action = ListFragmentDirections.actionGoToDetail(animal)
                Navigation.findNavController(view).navigate(action)
            }
        }
    }

    inner class AnimalViewHolder(val animalItemBinding: AnimalItemBinding) :
        RecyclerView.ViewHolder(animalItemBinding.root)
}

