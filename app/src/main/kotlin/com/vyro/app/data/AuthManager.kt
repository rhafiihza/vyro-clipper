package com.vyro.app.data

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task

/**
 * Manages Google Sign-In for YouTube upload authentication.
 *
 * Setup required before this works:
 * 1. Create a Google Cloud project and enable YouTube Data API v3
 * 2. Create OAuth 2.0 Web Application credentials
 * 3. Put the Client ID in strings.xml (google_oauth_client_id)
 * 4. Configure the OAuth consent screen
 * 5. Submit for Google verification (for public use)
 *
 * See docs/youtube-setup-guide.md for step-by-step instructions.
 */
object AuthManager {

    private const val TAG = "VYRO/Auth"
    private const val YOUTUBE_UPLOAD_SCOPE = "https://www.googleapis.com/auth/youtube.upload"
    private const val YOUTUBE_READONLY_SCOPE = "https://www.googleapis.com/auth/youtube.readonly"

    fun isConfigured(context: Context): Boolean {
        val clientId = context.getString(com.vyro.app.R.string.google_oauth_client_id)
        return !clientId.startsWith("YOUR_GOOGLE_OAUTH_CLIENT_ID")
    }

    fun getSignInClient(context: Context): GoogleSignInClient {
        val clientId = context.getString(com.vyro.app.R.string.google_oauth_client_id)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientId)
            .requestScopes(
                Scope(YOUTUBE_UPLOAD_SCOPE),
                Scope(YOUTUBE_READONLY_SCOPE)
            )
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    fun getCurrentAccount(context: Context): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }

    fun getAccessToken(account: GoogleSignInAccount?): String? {
        if (account == null) return null
        return account.idToken
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>): GoogleSignInAccount? {
        return try {
            val account = completedTask.getResult(ApiException::class.java)
            Log.i(TAG, "Google Sign-In successful: ${account.email}")
            account
        } catch (e: ApiException) {
            Log.e(TAG, "Google Sign-In failed: code=${e.statusCode}, msg=${e.message}")
            null
        }
    }

    fun signOut(context: Context, onComplete: () -> Unit) {
        getSignInClient(context).signOut().addOnCompleteListener { onComplete() }
    }
}