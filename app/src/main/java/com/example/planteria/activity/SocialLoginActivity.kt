package com.example.planteria.activity

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.planteria.PlanteriaApplication
import com.example.planteria.R
import com.example.planteria.fragment.HomeFragment
import com.example.planteria.utils.PrefHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson

open class SocialLoginActivity : BaseActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var homeActivity: HomeActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuthInit()

    }

    private fun firebaseAuthInit() {
        auth = FirebaseAuth.getInstance()
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }


    fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }


    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleGoogleResults(task)
        }
    }


    private fun handleGoogleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            Log.d(ContentValues.TAG, "signInWithCredential:success")
            val account: GoogleSignInAccount? = task.result
            if (account != null) {

                val idToken = account.idToken
                val displayName = account.displayName
                val email = account.email

                firebaseAuthWithGoogle(account)

                Log.d(ContentValues.TAG, "ID Token: $idToken, Display Name: $displayName, Email: $email")

            }
        }else{
            Log.d(task.exception.toString(),"signInWithCredential:failed")
        }
    }


    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    val email = account?.email
                    val name = account?.displayName

                    user?.getIdToken(true)?.addOnCompleteListener { idTokenTask ->
                        if (idTokenTask.isSuccessful) {
                            var idToken = idTokenTask.result?.token
                            println("Social Google: id token: $idToken email: $email")

                            saveUserDetails(idToken, name, email)
                        }
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)

                }
            }
        }


    private fun saveUserDetails(token: String?, name: String?, email: String?) {

        PlanteriaApplication.prefHelper.putString(
            PrefHelper.TOKEN,
            "Bearer $token"
        )

        val gson = Gson()
        val userDetails = name
        PlanteriaApplication.prefHelper.putString(
            PrefHelper.USER_DETAIL,
            userDetails
        )

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()

    }
}