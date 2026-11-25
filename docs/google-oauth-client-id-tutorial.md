# Google OAuth Client ID — Complete Setup Tutorial

This tutorial walks you through obtaining a `GOOGLE_OAUTH_CLIENT_ID` so VYRO can upload clips to YouTube. Since your repo is private and for internal use, you can keep the app in **Testing mode** — no Google verification required, and up to 100 test users can sign in.

---

## Prerequisites

- A Google account (any Gmail or Google Workspace account)
- A browser
- 15 minutes

---

## Step 1: Create a Google Cloud Project

1. Open [console.cloud.google.com](https://console.cloud.google.com)
2. Sign in with your Google account
3. Click the **project dropdown** at the top of the page (next to the Google Cloud logo)
4. Click **New Project** (top right of the dropdown)
5. Fill in:
   - **Project name:** `VYRO`
   - **Organization:** leave as default (or "No organization")
   - **Location:** leave as default
6. Click **Create**
7. Wait 5–10 seconds for the project to be created
8. Click the project dropdown again and select **VYRO** to make sure you're in the right project

> You can verify you're in the right project by checking the project name in the top bar.

---

## Step 2: Enable YouTube Data API v3

1. In the left sidebar, click **APIs & Services** → **Library**
   - Or go directly to: [console.cloud.google.com/apis/library](https://console.cloud.google.com/apis/library)
2. In the search box, type: `YouTube Data API v3`
3. Click on **YouTube Data API v3** in the results
4. Click the blue **Enable** button
5. Wait for it to enable (2–3 seconds, you'll see a green checkmark)

> If you see "API enabled" with usage stats, you're done with this step.

---

## Step 3: Configure the OAuth Consent Screen

This screen defines what users see when VYRO asks for permission to upload to their YouTube.

1. Go to **APIs & Services** → **OAuth consent screen**
   - Or: [console.cloud.google.com/apis/consent](https://console.cloud.google.com/apis/consent)
2. Under **User type**, select **External**
   - (Even though this is internal use, select External. "Internal" is only for Google Workspace organizations.)
3. Click **Create**
4. Fill in the **App information** form:
   - **App name:** `VYRO`
   - **User support email:** select your email from the dropdown
   - **App logo:** (optional) upload the VYRO icon
   - **Application home page:** `https://vyro-clipper.vercel.app` (your Vercel URL)
   - **Application privacy policy URL:** `https://vyro-clipper.vercel.app/privacy-policy` (replace with your actual Vercel URL)
   - **Application terms of service URL:** `https://vyro-clipper.vercel.app/terms-of-service` (replace with your actual Vercel URL)
   - **Authorized domains:** type your Vercel domain without `https://` (e.g. `vyro-clipper.vercel.app`) and press Enter
5. Under **Developer contact information:**
   - **Email addresses:** your email (should be auto-filled)
6. Click **Save and Continue**

### Scopes page

7. Click **Add or Remove Scopes**
8. In the filter box, type: `youtube`
9. Check these two scopes:
   - `https://www.googleapis.com/auth/youtube.upload` — **Upload videos to YouTube**
   - `https://www.googleapis.com/auth/youtube.readonly` — **View your YouTube account**
10. Click **Update** at the bottom
11. Click **Save and Continue**

### Test users page

12. Click **Add Users**
13. Type your own Google email address (the one you'll use to sign in on your phone)
14. Click **Add**
15. Click **Save and Continue**

### Summary page

16. Review the summary
17. Click **Back to Dashboard** at the bottom

> Your app is now in **Testing** mode. Up to 100 test users can sign in. No Google verification needed.

---

## Step 4: Create OAuth 2.0 Credentials

Now you'll create the actual Client ID that goes into the app.

1. Go to **APIs & Services** → **Credentials**
   - Or: [console.cloud.google.com/apis/credentials](https://console.cloud.google.com/apis/credentials)
2. Click **Create Credentials** (top of the page) → **OAuth client ID**
3. Under **Application type**, select **Web application**
   - (Choose Web, not Android. Web type is simpler and works with Google Sign-In on Android.)
4. **Name:** `VYRO Android Client`
5. Under **Authorized JavaScript origins:**
   - Click **Add URI**
   - Type: `https://vyro-clipper.vercel.app` (replace with your Vercel URL)
6. Under **Authorized redirect URIs:**
   - Click **Add URI**
   - Type: `https://vyro-clipper.vercel.app/auth/callback`
7. Click **Create**

---

## Step 5: Copy Your Client ID

After clicking Create, a dialog appears with your credentials:

- **Your Client ID:** `123456789-abcdefghij.apps.googleusercontent.com`
- **Your Client Secret:** (you don't need this for the app — ignore it)

1. **Copy the Client ID** (the long string ending in `.apps.googleusercontent.com`)
2. Click **OK** to close the dialog

> You can always find this Client ID again later at APIs & Services → Credentials → click the client name.

---

## Step 6: Add the Client ID to the App

You have two options. Since your repo is private, **Option A** is fine for now. Use **Option B** when you want to share the repo or make it public.

### Option A: Direct (simple, fine for private repo)

1. Open `app/src/main/res/values/strings.xml` in your repo
2. Replace the placeholder:

```xml
<!-- Before -->
<string name="google_oauth_client_id">YOUR_GOOGLE_OAUTH_CLIENT_ID.apps.googleusercontent.com</string>

<!-- After (example) -->
<string name="google_oauth_client_id">123456789-abcdefghij.apps.googleusercontent.com</string>
```

3. Also update `default_web_client_id` with the same value:

```xml
<string name="default_web_client_id">123456789-abcdefghij.apps.googleusercontent.com</string>
```

4. Commit and push the change
5. Rebuild the APK

### Option B: GitHub Secret (best practice, future-proof)

This keeps the Client ID out of the source code entirely.

#### Step 6b.1: Store the Client ID in GitHub

1. Go to [github.com/rhafiihza/vyro-clipper/settings/secrets/actions](https://github.com/rhafiihza/vyro-clipper/settings/secrets/actions)
2. Click **New repository secret**
3. **Name:** `GOOGLE_OAUTH_CLIENT_ID`
4. **Secret:** paste your Client ID (`123456789-abcdefghij.apps.googleusercontent.com`)
5. Click **Add secret**

#### Step 6b.2: Update build.gradle.kts to read from environment

In `app/build.gradle.kts`, add this inside `defaultConfig`:

```kotlin
defaultConfig {
    applicationId = "com.vyro.app"
    minSdk = 26
    targetSdk = 34
    versionCode = 2
    versionName = "2.1"

    // Inject OAuth Client ID from environment (GitHub Secret in CI, local for dev)
    buildConfigField("String", "OAUTH_CLIENT_ID",
        "\"${System.getenv("GOOGLE_OAUTH_CLIENT_ID") ?: "YOUR_GOOGLE_OAUTH_CLIENT_ID.apps.googleusercontent.com"}\"")
}
```

Also add `buildFeatures { buildConfig = true }` to the android block.

#### Step 6b.3: Update AuthManager.kt to use BuildConfig

In `AuthManager.kt`, replace the strings.xml reference:

```kotlin
// Before
val clientId = context.getString(com.vyro.app.R.string.google_oauth_client_id)

// After
val clientId = com.vyro.app.BuildConfig.OAUTH_CLIENT_ID
```

#### Step 6b.4: Create a GitHub Actions workflow to build the APK

Create `.github/workflows/build-apk.yml`:

```yaml
name: Build APK

on:
  push:
    branches: [main]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Setup Android SDK
        uses: android-actions/setup-android@v3
      - name: Build APK
        env:
          GOOGLE_OAUTH_CLIENT_ID: ${{ secrets.GOOGLE_OAUTH_CLIENT_ID }}
        run: ./gradlew assembleDebug
      - name: Upload APK
        uses: actions/upload-artifact@v4
        with:
          name: vyro-debug-apk
          path: app/build/outputs/apk/debug/app-debug.apk
```

This builds the APK on every push, with the real Client ID injected from the GitHub Secret. The APK is available as a downloadable artifact in the Actions tab.

---

## Step 7: Test the Sign-In

1. Install the VYRO APK on your phone
2. Open the app → **Settings** tab
3. Under **YouTube Auto-Publish**, you should see:
   - Before: "Not configured — see setup guide"
   - After: "Not signed in"
4. This confirms the Client ID is being read correctly
5. (Full sign-in flow requires the Google Sign-In intent to be wired up — see PLAN.md Phase 11)

---

## Troubleshooting

### "Not configured" still shows after adding the Client ID
- Double-check `strings.xml` has the real ID, not the placeholder
- Make sure there are no typos (the ID ends in `.apps.googleusercontent.com`)
- Rebuild the APK after changing `strings.xml`

### Google Cloud Console says "YouTube Data API v3 is not enabled"
- Go back to Step 2 and enable it. It's free.

### OAuth consent screen says "Publishing in progress"
- You don't need to publish. Keep it in **Testing** mode for internal use.
- In Testing mode, only users you added in Step 3 can sign in.

### Error 10 (DEVELOPER_ERROR) when signing in
- The Client ID is wrong or the app's package name doesn't match
- VYRO's package name is `com.vyro.app` — verify this matches
- If using Android client type (not Web), add your SHA-1 fingerprint

### Error 12500 when signing in
- The OAuth consent screen is not configured properly
- Make sure you added yourself as a test user
- Make sure the scopes are correctly set

### Can't find the Credentials page
- Direct link: [console.cloud.google.com/apis/credentials](https://console.cloud.google.com/apis/credentials)
- Make sure you're in the VYRO project (check the dropdown at the top)

---

## Quick Reference

| Item | Value |
|------|-------|
| Google Cloud Console | [console.cloud.google.com](https://console.cloud.google.com) |
| YouTube Data API v3 | [console.cloud.google.com/apis/library](https://console.cloud.google.com/apis/library) |
| OAuth consent screen | [console.cloud.google.com/apis/consent](https://console.cloud.google.com/apis/consent) |
| Credentials page | [console.cloud.google.com/apis/credentials](https://console.cloud.google.com/apis/credentials) |
| Google permissions | [myaccount.google.com/permissions](https://myaccount.google.com/permissions) |
| VYRO package name | `com.vyro.app` |
| Required scopes | `youtube.upload`, `youtube.readonly` |
| App status (internal use) | Testing (no verification needed) |
| Max test users | 100 |
| Daily upload quota | ~6 videos (10,000 units / 1,600 per upload) |

---

## Checklist

- [ ] Google Cloud project created (name: VYRO)
- [ ] YouTube Data API v3 enabled
- [ ] OAuth consent screen configured (Testing mode)
- [ ] Privacy Policy URL added (Vercel URL)
- [ ] Terms of Service URL added (Vercel URL)
- [ ] Test user added (your Google email)
- [ ] OAuth 2.0 credentials created (Web application type)
- [ ] Client ID copied
- [ ] Client ID added to app (strings.xml or GitHub Secret)
- [ ] APK rebuilt with the Client ID
- [ ] Settings screen shows "Not signed in" (not "Not configured")

---

*This tutorial is part of the VYRO documentation. See also: [YouTube Setup Guide](youtube-setup-guide.md) for the full auto-publish setup.*