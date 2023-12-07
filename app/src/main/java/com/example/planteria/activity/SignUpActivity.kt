package com.example.planteria.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.planteria.R
import com.example.planteria.databinding.ActivitySignUpBinding
import com.example.planteria.utils.FirebaseUtils.FirebaseUtils.user
import com.example.planteria.utils.LoadingDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : SocialLoginActivity() {

    lateinit var binding: ActivitySignUpBinding
    lateinit var userEmail: String
    lateinit var userPassword: String
    lateinit var createAccountInputsArray: Array<EditText>
    var mLoadingFragments = LoadingDialogFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createAccountInputsArray = arrayOf(binding.edtEmail, binding.edtPassword, binding.edtRePassword)


        binding.txtSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.imgGoogle.setOnClickListener {
            signInWithGoogle()
        }

        binding.btnSignUp.setOnClickListener {
            signUpWithEmailAndPassword()
        }
    }

    private fun notEmpty(): Boolean = binding.edtEmail.text.toString().trim().isNotEmpty() &&
            binding.edtPassword.text.toString().trim().isNotEmpty() &&
            binding.edtRePassword.text.toString().trim().isNotEmpty()


    private fun identicalPassword(): Boolean {
        var identical = false
        if (notEmpty() && binding.edtPassword.text.toString().trim() == binding.edtRePassword.text.toString().trim())
        {
            identical = true
        }
        else if (!notEmpty()) {
            createAccountInputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        } else {
            Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
        }
        return identical
    }

    private fun signUpWithEmailAndPassword() {
        if (identicalPassword()) {
            mLoadingFragments.show(supportFragmentManager, "")

            userEmail = binding.edtEmail.text.toString()
            userPassword = binding.edtPassword.text.toString()

            auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener {task ->
                    if (task.isSuccessful) {
                        mLoadingFragments.dismissAllowingStateLoss()
                        Toast.makeText(this, "User account created successfully", Toast.LENGTH_SHORT).show()
                        sendEmailVerification()
                        startActivity(Intent(this, SignInActivity::class.java))
                        finish()
                    }
                    else {
                        mLoadingFragments.dismissAllowingStateLoss()
                        Toast.makeText(this, "User account creation failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun sendEmailVerification() {
        user.let {
            it?.sendEmailVerification()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Email verification send to $userEmail", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}