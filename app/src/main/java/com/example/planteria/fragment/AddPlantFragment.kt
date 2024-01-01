package com.example.planteria.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.planteria.R
import com.example.planteria.activity.ArMeasurement
import com.example.planteria.activity.HomeActivity
import com.example.planteria.databinding.FragmentAddPlantBinding
import com.example.planteria.request.PlantData
import com.example.planteria.utils.LoadingDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.Calendar
import java.util.Locale

class AddPlantFragment : Fragment() {

    lateinit var binding: FragmentAddPlantBinding
    lateinit var homeActivity: HomeActivity
    private var imageUri : Uri? = null
    var mLoadingFragments = LoadingDialogFragment()
    private val calendar = Calendar.getInstance()

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it

        binding.imgProfile.setImageURI(imageUri)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddPlantBinding.inflate(layoutInflater, container, false)
        if (context is HomeActivity) {
            homeActivity = context as HomeActivity
        }

        binding.changePlantPicture.setOnClickListener {
            selectImage.launch("image/*")
        }

        binding.btnSavePlant.setOnClickListener {
            validateData()
        }

        binding.edtAddDate.setOnClickListener {
            showDatePicker()
        }
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnManually.setOnClickListener {
            if (binding.edtHeightPlant.visibility == View.VISIBLE) {
                binding.edtHeightPlant.visibility = View.GONE
            } else {
                binding.edtHeightPlant.visibility = View.VISIBLE
            }
        }

        binding.btnUsingAr.setOnClickListener {
            val intent = Intent(homeActivity, ArMeasurement::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun validateData() {
        if (binding.edtNamePlant.text.toString().isEmpty()
            || binding.edtHeightPlant.text.toString().isEmpty()
            || binding.edtAddDate.text.toString().isEmpty()
            || imageUri == null) {
            Toast.makeText(homeActivity, "Enter all fields", Toast.LENGTH_SHORT).show()
        } else {
            uploadImage()
        }
    }

    private fun uploadImage() {
        mLoadingFragments.show(childFragmentManager, "")

        val storageRef = FirebaseStorage.getInstance().getReference("plant_data/")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("plants")

        Log.e("UID", FirebaseAuth.getInstance().currentUser!!.uid)

        if (imageUri != null) {
            storageRef.putFile(imageUri!!)
                .addOnSuccessListener {
                    mLoadingFragments.dismissAllowingStateLoss()
                    storageRef.downloadUrl.addOnSuccessListener {
                        storeData(it)
                    }.addOnFailureListener {
                        Toast.makeText(homeActivity, "Failed to download image Url", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    mLoadingFragments.dismissAllowingStateLoss()
                    Toast.makeText(homeActivity, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }
        }
        else {
            Toast.makeText(homeActivity, "The image Uri in null", Toast.LENGTH_SHORT).show()
        }
    }

//    private fun storeData(imageUrl : Uri?) {
//
//        val plantData = PlantData(
//            plantImage = imageUrl.toString(),
//            plantName = binding.edtNamePlant.text.toString(),
//            plantHeight = binding.edtHeightPlant.text.toString(),
//            plantAddedDate = binding.edtAddDate.text.toString()
//        )
//
//        FirebaseDatabase.getInstance().getReference("plant_data/")
//            .child(FirebaseAuth.getInstance().currentUser!!.uid)
//            .setValue(plantData).addOnCompleteListener {
//                mLoadingFragments.dismissAllowingStateLoss()
//
//                if (it.isSuccessful) {
//                    Toast.makeText(homeActivity, "Plant data added successfully", Toast.LENGTH_SHORT).show()
//                }else {
//                    Toast.makeText(homeActivity, it.exception!!.message, Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
    private fun storeData(imageUrl: Uri?) {
        val plantData = PlantData(
            plantImage = imageUrl.toString(),
            plantName = binding.edtNamePlant.text.toString(),
            plantHeight = binding.edtHeightPlant.text.toString(),
            plantAddedDate = binding.edtAddDate.text.toString()
        )

        val userPlantsRef = FirebaseDatabase.getInstance().getReference("User's Plants")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        val plantKey = userPlantsRef.push().key

        if (plantKey != null) {

            userPlantsRef.child(plantKey).setValue(plantData)
                .addOnCompleteListener { task ->
                    mLoadingFragments.dismissAllowingStateLoss()

                    if (task.isSuccessful) {
                        Toast.makeText(homeActivity, "Plant data added successfully", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()
                    } else {
                        Toast.makeText(homeActivity, "Failed to store data: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(homeActivity, "Failed to generate unique key", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showDatePicker() {


        val datePickerDialog = DatePickerDialog(
            homeActivity, R.style.MyDatePickerStyle, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->

                val selectedDate = Calendar.getInstance()

                selectedDate.set(year, monthOfYear, dayOfMonth)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                val formattedDate = dateFormat.format(selectedDate.time)

                binding.edtAddDate.setText(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    companion object {
        fun newInstance() =
            AddPlantFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}