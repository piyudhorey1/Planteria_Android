package com.example.planteria.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planteria.R
import com.example.planteria.activity.HomeActivity
import com.example.planteria.adapter.AddedPlantsAdapter
import com.example.planteria.databinding.FragmentAddedPlantsBinding
import com.example.planteria.request.PlantData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddedPlantsFragment : Fragment() {

    lateinit var binding: FragmentAddedPlantsBinding
    lateinit var homeActivity: HomeActivity
    lateinit var dbRef : DatabaseReference
    lateinit var plantsRecyclerView: RecyclerView
    lateinit var plantsArrayList: ArrayList<PlantData>
    val userId = FirebaseAuth.getInstance().currentUser!!.uid
//    val plantsAdapter = AddedPlantsAdapter(plantsArrayList, homeActivity)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddedPlantsBinding.inflate(layoutInflater, container, false)
        if (context is HomeActivity) {
            homeActivity = context as HomeActivity
        }

        binding.btnAddPlantData.setOnClickListener {
            homeActivity.addNewFragment(AddPlantFragment.newInstance(), "Add plant", Bundle())
        }

        plantsRecyclerView = binding.rcyAddedPlants
        plantsRecyclerView.layoutManager = LinearLayoutManager(homeActivity)
        plantsRecyclerView.setHasFixedSize(true)

        plantsArrayList = arrayListOf<PlantData>()
        getAddedPlantData()

        return binding.root
    }

    private fun getAddedPlantData() {

        dbRef = FirebaseDatabase.getInstance().getReference("User's Plants/$userId")

        dbRef.addValueEventListener(object : ValueEventListener{


            override fun onDataChange(snapshot: DataSnapshot) {
                plantsArrayList.clear()

                for (plantSnapshot in snapshot.children) {
                    val plant = plantSnapshot.getValue(PlantData::class.java)
                    plant?.let { plantsArrayList.add(it) }
                }

                if (plantsArrayList != null) {
                    plantsRecyclerView.adapter = AddedPlantsAdapter(plantsArrayList, homeActivity)
                    binding.txtNoPlantsAdded.visibility = View.GONE
                } else {
                    binding.txtNoPlantsAdded.visibility = View.VISIBLE
                }

//                plantsAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching data", error.toException())
            }

        })
    }

    companion object {
        fun newInstance() =
            AddedPlantsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}