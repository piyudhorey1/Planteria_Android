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
import com.example.planteria.responseModel.GetPlantsDataResponse
import com.example.planteria.responseModel.SpeciesData

class PlantsNameHorizontalAdapter(private var homeActivity: HomeActivity,
                                  private val plantsSpeciesList: GetPlantsDataResponse,
                                  private val itemClickListener: OnItemClickListener
): RecyclerView.Adapter<PlantsNameHorizontalAdapter.MyViewHolder>()
{


    interface OnItemClickListener {
        fun onItemClick(plaintId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_plant_name_horizontal_list, parent, false)
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
        var mPlantName = itemView.findViewById<TextView>(R.id.txtPlantName)
        fun bindItem(plantsData: SpeciesData) {
            val imageUrl = plantsData.defaultImage?.originalUrl
            mPlantName.setText(plantsData.commonName)
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
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val plantId = plantsSpeciesList.data[position].id
                    itemClickListener.onItemClick(plantId!!)
                }
            }
        }
    }

}