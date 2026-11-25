package com.vyro.app.data

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Uploads video clips to YouTube via the YouTube Data API v3.
 * Uses the resumable upload protocol.
 * Requires a valid GoogleSignInAccount with youtube.upload scope.
 * See docs/youtube-setup-guide.md for setup instructions.
 */
object YouTubeUploader {

    private const val TAG = "VYRO/YouTube"
    private const val YOUTUBE_UPLOAD_INIT_URL = "https://www.googleapis.com/upload/youtube/v3/videos?uploadType=resumable&part=snippet,status"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .build()

    data class UploadResult(val success: Boolean, val videoId: String? = null, val videoUrl: String? = null, val error: String? = null)
    data class VideoMetadata(val title: String, val description: String, val tags: List<String> = emptyList(), val categoryId: String = "22", val privacyStatus: String = "private")

    fun uploadVideo(context: Context, account: GoogleSignInAccount?, videoFile: File, metadata: VideoMetadata): UploadResult {
        if (!AuthManager.isConfigured(context)) {
            return UploadResult(success = false, error = "Google OAuth not configured. See docs/youtube-setup-guide.md")
        }
        val token = AuthManager.getAccessToken(account)
            ?: return UploadResult(success = false, error = "Not signed in.")

        try {
            val metadataJson = buildMetadataJson(metadata)
            Log.i(TAG, "Initiating upload: ${metadata.title}")

            val initRequest = Request.Builder()
                .url(YOUTUBE_UPLOAD_INIT_URL)
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .header("X-Upload-Content-Type", "video/*")
                .header("X-Upload-Content-Length", videoFile.length().toString())
                .post(metadataJson.toRequestBody("application/json".toMediaType()))
                .build()

            client.newCall(initRequest).execute().use { response ->
                if (!response.isSuccessful) return UploadResult(success = false, error = "YouTube API error: ${response.code}")
                val uploadUrl = response.header("Location") ?: return UploadResult(success = false, error = "No upload URL")

                val videoBytes = videoFile.readBytes()
                val uploadRequest = Request.Builder()
                    .url(uploadUrl)
                    .header("Content-Type", "video/*")
                    .put(videoBytes.toRequestBody("video/*".toMediaType()))
                    .build()

                client.newCall(uploadRequest).execute().use { uploadResponse ->
                    val body = uploadResponse.body?.string() ?: ""
                    if (uploadResponse.isSuccessful) {
                        val videoId = JSONObject(body).optString("id", "")
                        return UploadResult(success = true, videoId = videoId, videoUrl = "https://youtube.com/watch?v=$videoId")
                    }
                    return UploadResult(success = false, error = "Upload failed: ${uploadResponse.code}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Upload exception: ${e.message}", e)
            return UploadResult(success = false, error = e.message ?: "Unknown error")
        }
    }

    private fun buildMetadataJson(meta: VideoMetadata): String {
        val snippet = JSONObject().put("title", meta.title).put("description", meta.description).put("categoryId", meta.categoryId)
        if (meta.tags.isNotEmpty()) snippet.put("tags", org.json.JSONArray(meta.tags))
        val status = JSONObject().put("privacyStatus", meta.privacyStatus).put("selfDeclaredMadeForKids", false)
        return JSONObject().put("snippet", snippet).put("status", status).toString()
    }
}