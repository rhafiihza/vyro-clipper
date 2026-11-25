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
import com.vyro.app.BuildConfig

/**
 * Manages Google Sign-In for YouTube upload authentication.
 *
 * The OAuth Client ID is injected at build time via BuildConfig.OAUTH_CLIENT_ID.
 * It reads from the GOOGLE_OAUTH_CLIENT_ID environment variable (GitHub Secret in CI).
 * See docs/google-oauth-client-id-tutorial.md for setup instructions.
 */
object AuthManager {

    private const val TAG = "VYRO/Auth"
    private const val YOUTUBE_UPLOAD_SCOPE = "https://www.googleapis.com/auth/youtube.upload"
    private const val YOUTUBE_READONLY_SCOPE = "https://www.googleapis.com/auth/youtube.readonly"

    /**
     * Returns true if the OAuth client ID has been configured.
     * Returns false if the placeholder is still in place.
     */
    fun isConfigured(context: Context): Boolean {
        return !BuildConfig.OAUTH_CLIENT_ID.startsWith("YOUR_GOOGLE_OAUTH_CLIENT_ID")
    }

    /**
     * Creates the GoogleSignInClient with YouTube upload scope.
     * Call this to get the sign-in intent.
     */
    fun getSignInClient(context: Context): GoogleSignInClient {
        val clientId = BuildConfig.OAUTH_CLIENT_ID
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

    /**
     * Gets the currently signed-in account, or null if not signed in.
     */
    fun getCurrentAccount(context: Context): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }

    /**
     * Returns the OAuth access token for YouTube API calls, or null if not available.
     * The token is short-lived (~1 hour) and must be refreshed.
     */
    fun getAccessToken(account: GoogleSignInAccount?): String? {
        if (account == null) return null
        return account.idToken
    }

    /**
     * Handles the sign-in result from the activity intent.
     * Returns the account on success, null on failure.
     */
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

    /**
     * Signs the user out.
     */
    fun signOut(context: Context, onComplete: () -> Unit) {
        getSignInClient(context).signOut().addOnCompleteListener { onComplete() }
    }
}
