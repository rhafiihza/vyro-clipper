# YouTube Auto-Publish Setup Guide

This guide walks you through everything needed to enable YouTube auto-publish in VYRO. Follow each step in order — some steps take weeks (Google verification), so start early.

---

## Overview

When configured, VYRO can upload exported clips directly to YouTube from the app. The flow is:

1. User taps **Publish to YouTube** on the Export screen
2. VYRO authenticates with Google (OAuth 2.0)
3. VYRO uploads the video via YouTube Data API v3
4. Video appears on the user's YouTube channel

**What you need:**
- A Google Cloud project (free)
- YouTube Data API v3 enabled (free)
- OAuth 2.0 credentials (free)
- A hosted Privacy Policy URL (free with GitHub Pages)
- Google OAuth verification (free, takes 2–6 weeks)

---

## Step 1: Create a Google Cloud Project

1. Go to [console.cloud.google.com](https://console.cloud.google.com)
2. Click the project dropdown (top bar) → **New Project**
3. Project name: `vyro-clipper`
4. Organization: leave as default (or "No organization")
5. Click **Create**
6. Wait for the project to be created, then select it from the dropdown

---

## Step 2: Enable YouTube Data API v3

1. In the Cloud Console, go to **APIs & Services → Library**
2. Search for `YouTube Data API v3`
3. Click on it → **Enable**
4. Wait for it to enable (a few seconds)

---

## Step 3: Configure the OAuth Consent Screen

This screen defines what users see when VYRO asks for permission to upload to their YouTube.

1. Go to **APIs & Services → OAuth consent screen**
2. User type: **External** (unless you have a Google Workspace)
3. Click **Create**
4. Fill in the form:
   - **App name:** `VYRO`
   - **User support email:** your email
   - **App logo:** upload the VYRO icon (optional but recommended)
   - **Application home page:** `https://github.com/rhafiihza/vyro-clipper`
   - **Application privacy policy URL:** see Step 4 below
   - **Application terms of service URL:** see Step 4 below
   - **Authorized domains:** add any domain you control (e.g. your GitHub Pages domain)
   - **Developer contact information:** your email
5. Click **Save and Continue**
6. **Scopes page:**
   - Click **Add or Remove Scopes**
   - Add these scopes:
     - `https://www.googleapis.com/auth/youtube.upload` (Upload videos)
     - `https://www.googleapis.com/auth/youtube.readonly` (View your YouTube account)
   - Click **Save and Continue**
7. **Test users page:**
   - Add your own Google account email as a test user
   - Click **Save and Continue**
8. Review the summary → **Back to Dashboard**

---

## Step 4: Host Privacy Policy and Terms of Service

Google requires hosted privacy policy and terms of service URLs before they will verify your app.

### Option A: GitHub Pages (free, recommended)

1. In the `vyro-clipper` repo, create a `docs/privacy-policy.md` file
2. Create a `docs/terms-of-service.md` file
3. Go to **Settings → Pages** in the GitHub repo
4. Source: **Deploy from a branch**, Branch: `main`, Folder: `/docs`
5. Click **Save**
6. Your pages will be live at:
   - `https://rhafiihza.github.io/vyro-clipper/privacy-policy`
   - `https://rhafiihza.github.io/vyro-clipper/terms-of-service`

### Option B: Any static hosting

Upload the two HTML files to any web host. The URLs must be HTTPS.

---

## Step 5: Create OAuth 2.0 Credentials

1. Go to **APIs & Services → Credentials**
2. Click **Create Credentials → OAuth client ID**
3. Application type: **Web application** (not Android — Web type works better for this flow)
4. Name: `VYRO Android Client`
5. **Authorized JavaScript origins:**
   - Add: `https://rhafiihza.github.io` (or your domain)
6. **Authorized redirect URIs:**
   - Add: `https://rhafiihza.github.io/vyro-clipper/auth/callback`
7. Click **Create**
8. A dialog appears with your **Client ID** — copy it
   - It looks like: `123456789-abcdefg.apps.googleusercontent.com`
9. Click **OK**

---

## Step 6: Add the Client ID to the App

1. Open the project in Android Studio (or edit on GitHub)
2. Open `app/src/main/res/values/strings.xml`
3. Replace the placeholder:

```xml
<!-- Before -->
<string name="google_oauth_client_id">YOUR_GOOGLE_OAUTH_CLIENT_ID.apps.googleusercontent.com</string>

<!-- After -->
<string name="google_oauth_client_id">123456789-abcdefg.apps.googleusercontent.com</string>
```

4. Also update `default_web_client_id` with the same value
5. Commit and push the change

---

## Step 7: Get Your SHA-1 Fingerprint (if using Android client type)

If you chose **Android** application type in Step 5 (instead of Web), you need to provide your app's SHA-1 fingerprint.

### Debug fingerprint (for development)

```bash
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

Look for the `SHA1:` line and copy the value.

### Release fingerprint (for production)

```bash
keytool -list -v -keystore your-release.jks -alias your-alias
```

Add both fingerprints to the OAuth credential in Google Cloud Console.

---

## Step 8: Test Before Verification

While your app is in **Testing** status, only test users you added in Step 3 can sign in.

1. Install the VYRO APK on your phone
2. Go to **Settings → YouTube Auto-Publish**
3. It should now show "Not signed in" instead of "Not configured"
4. The **Publish to YouTube** button on the Export screen will be enabled
5. Sign in with your Google account (the one you added as a test user)
6. Try uploading a clip — it will upload as **Private** visibility

### Default quota

YouTube Data API gives you **10,000 units per day** by default. Each upload costs approximately **1,600 units**, so you can upload about **6 videos per day** during testing.

---

## Step 9: Submit for Google Verification (for public release)

To let any user (not just test users) sign in, you must submit your app for Google's OAuth verification.

1. Go to **APIs & Services → OAuth consent screen**
2. Click **Publish App** (change status from Testing to In Production)
3. Google will show a verification checklist
4. Prepare these items:
   - Hosted privacy policy URL
   - Hosted terms of service URL
   - App screenshots (3–5 screenshots of the app)
   - A demo video showing the OAuth flow
   - Written justification for each scope:
     - `youtube.upload`: "VYRO uploads user-created video clips to the user's YouTube channel."
     - `youtube.readonly`: "VYRO reads the user's channel name to display it in the app."
   - A link to your app's home page
5. Submit for verification
6. Google will review — this takes **2–6 weeks**
7. You may receive follow-up emails asking for clarification

### Until verification is complete
- Only test users can sign in (up to 100 test users)
- Videos upload as **Private** visibility only
- Public uploads are blocked until verification passes

---

## Step 10: Request Quota Increase (optional)

If 6 uploads per day is not enough:

1. Go to **APIs & Services → YouTube Data API v3 → Quotas**
2. Click **Apply for higher quota**
3. Justify your need (e.g. "VYRO users create an average of 10 clips per session")
4. Google reviews and may approve

---

## Troubleshooting

### "Not configured" in Settings
Your `google_oauth_client_id` in `strings.xml` still has the placeholder. Replace it with your real Client ID from Step 5.

### Sign-in fails with error code 10
This is `DEVELOPER_ERROR` — usually means the Client ID is wrong or the SHA-1 fingerprint is missing. Double-check Step 5 and Step 7.

### Sign-in fails with error code 12500
This is `SIGN_IN_CANCELLED` or a configuration issue. Make sure the OAuth consent screen is published (or you are a test user).

### Upload fails with 403
Your app hasn't been verified yet, or the user isn't in the test users list. Add the user in the OAuth consent screen.

### Upload fails with 401
The auth token has expired. Sign out and sign in again.

### Upload fails with quota error
You've hit the 10,000 units/day limit. Wait 24 hours or request a quota increase (Step 10).

---

## File Locations in the Codebase

| File | Purpose |
|------|---------|
| `data/AuthManager.kt` | Google Sign-In client, token management |
| `data/YouTubeUploader.kt` | Resumable video upload via YouTube Data API |
| `ui/screens/Screens.kt` | Publish screen UI with upload button |
| `res/values/strings.xml` | OAuth Client ID placeholder |
| `app/build.gradle.kts` | play-services-auth and okhttp dependencies |

---

## Checklist

- [ ] Google Cloud project created
- [ ] YouTube Data API v3 enabled
- [ ] OAuth consent screen configured
- [ ] Privacy Policy hosted (GitHub Pages)
- [ ] Terms of Service hosted (GitHub Pages)
- [ ] OAuth 2.0 credentials created (Client ID copied)
- [ ] Client ID added to `strings.xml`
- [ ] SHA-1 fingerprint added (if using Android client type)
- [ ] Test user added to OAuth consent screen
- [ ] Sign-in works in debug build
- [ ] Upload works (video appears as Private on YouTube)
- [ ] Submitted for Google verification
- [ ] Verification approved (2–6 weeks)
- [ ] App published to Play Store (optional)

---

*This guide is part of the VYRO Master Development Plan. See PLAN.md for the full project plan.*