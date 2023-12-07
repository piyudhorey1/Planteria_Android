package com.example.planteria.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.planteria.PlanteriaApplication
import com.example.planteria.R
import java.lang.IllegalStateException

class LoadingDialogFragment: DialogFragment() {

    private val mLoadingDrawable: AnimationDrawable? = PlanteriaApplication.mLoadingAnimDrawable
    var mStateSaved = false
    override fun onResume() {
        super.onResume()
        mStateSaved = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mStateSaved = true
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.layout_loading_dialog, null)
        val imgLoading = view.findViewById<ImageView>(R.id.imgLoading)


//        Glide.with(this)
//            .load(R.drawable.ic_loader)
//            .into(imgLoading)

        imgLoading.setImageDrawable(mLoadingDrawable)

        mLoadingDrawable!!.run()

        val dialog: Dialog = object : Dialog(requireActivity()) {
            override fun onBackPressed() {}
        }
        dialog.setCancelable(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(view)
        dialog.setCanceledOnTouchOutside(false)
        val dialogWindow = dialog.window
        dialogWindow?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val fragmentView = getView()
        fragmentView?.setOnClickListener { }
        return dialog
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (mStateSaved) return
        try {
            super.show(manager, tag)
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
        }
    }
}