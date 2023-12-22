package com.example.planteria.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.planteria.PlanteriaApplication
import com.example.planteria.R
import com.example.planteria.databinding.ActivitySignInBinding
import com.example.planteria.utils.FirebaseUtils.FirebaseUtils.auth
import com.example.planteria.utils.FirebaseUtils.FirebaseUtils.user
import com.example.planteria.utils.LoadingDialogFragment
import com.example.planteria.utils.PrefHelper
import com.google.gson.Gson

class SignInActivity : AppCompatActivity() {

    lateinit var signInEmail: String
    lateinit var signInPassword: String
    lateinit var signInInputsArray: Array<EditText>
    lateinit var binding: ActivitySignInBinding
    var mLoadingFragments = LoadingDialogFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        signInInputsArray = arrayOf(binding.edtEmail, binding.edtPassword)

        binding.txtSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSignIn.setOnClickListener {
            signInWithEmailAndPassword()
        }
    }

    private fun notEmpty() : Boolean = signInEmail.isNotEmpty() && signInPassword.isNotEmpty()

    private fun signInWithEmailAndPassword() {

        signInEmail = binding.edtEmail.text.toString().trim()
        signInPassword = binding.edtPassword.text.toString().trim()

        if (notEmpty()) {
            mLoadingFragments.show(supportFragmentManager, "")

            auth.signInWithEmailAndPassword(signInEmail, signInPassword)
                .addOnCompleteListener {signIn ->
                    if (signIn.isSuccessful) {
                        mLoadingFragments.dismissAllowingStateLoss()
                        Toast.makeText(this, "Signed in successfully", Toast.LENGTH_SHORT).show()

//                        var userDetails = auth.currentUser!!.displayName
//
//                        userDetails = if (userDetails == null) ({
//                            auth.currentUser!!.email
//                        }).toString() else
//                            auth.currentUser!!.displayName

                        val userDetails = auth.currentUser?.displayName ?: run {
                            auth.currentUser?.email?.substringBefore('@')
                        }

                        auth.currentUser?.getIdToken(false)
                            ?.addOnSuccessListener {result ->

                                val idToken = result.token
                                saveUserDetails(idToken, userDetails)
                            }
                    } else {
                        mLoadingFragments.dismissAllowingStateLoss()
                        Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            signInInputsArray.forEach { input ->
                if (input.text.toString().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        }
    }

    private fun saveUserDetails(token: String?, userName: String?){

        PlanteriaApplication.prefHelper.putString(
            PrefHelper.TOKEN,
            "Bearer $token"
        )

        val gson = Gson()
        val userDetails = gson.toJson(userName)
        PlanteriaApplication.prefHelper.putString(
            PrefHelper.USER_DETAIL,
            userDetails
        )

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}