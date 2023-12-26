package com.example.planteria.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.planteria.R
import com.example.planteria.activity.HomeActivity
import com.example.planteria.request.PlantData

class AddedPlantsAdapter(private val plantsList : ArrayList<PlantData>,
                         private var homeActivity: HomeActivity
) : RecyclerView.Adapter<AddedPlantsAdapter.AddedPlantsHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddedPlantsHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_plant_added_vertical_list, parent, false)

        return AddedPlantsHolder(itemView)
    }

    override fun getItemCount(): Int {
        return plantsList.size
    }

    override fun onBindViewHolder(holder: AddedPlantsHolder, position: Int) {
        val currentItem = plantsList[position]

        holder.plantName.text = currentItem.plantName
        Glide
            .with(homeActivity.applicationContext)
            .load(currentItem.plantImage)
            .centerCrop()
            .into(holder.plantImage)
    }

    inner class AddedPlantsHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val plantName : TextView = itemView.findViewById(R.id.txtAddedPlantName)
        val plantImage : ImageView = itemView.findViewById(R.id.imgAddedRestPicture)

    }

}