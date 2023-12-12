package com.example.planteria.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.planteria.R
import com.example.planteria.activity.HomeActivity
import com.example.planteria.responseModel.GetSpeciesDataResponse
import com.example.planteria.responseModel.SpeciesData
import java.net.URL

class PlantsImageHorizontalAdapter(private var homeActivity: HomeActivity,
                                   private val plantsSpeciesList: GetSpeciesDataResponse
): RecyclerView.Adapter<PlantsImageHorizontalAdapter.MyViewHolder>()
{



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_plant_image_horizontal_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return plantsSpeciesList.data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val plantsData = plantsSpeciesList.data.get(position)
        holder.bindItem(plantsData)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mPlantImage = itemView.findViewById<ImageView>(R.id.imgRestPicture)
        fun bindItem(plantsData: SpeciesData) {
            val imageUrl = plantsData.defaultImage?.originalUrl
            if (imageUrl != null) {
                Glide
                    .with(homeActivity.applicationContext)
                    .load(imageUrl)
                    .centerCrop()
                    .into(mPlantImage)
            } else {
                // Log or handle null URL
            }
        }

    }

}