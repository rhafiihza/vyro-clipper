---
title: Privacy Policy
layout: default
---

# VYRO Privacy Policy

**Effective Date:** November 25, 2026
**Last Updated:** November 25, 2026

This Privacy Policy describes how VYRO ("the App", "we", "us") handles your information when you use the VYRO mobile application. By using the App, you agree to the practices described in this policy.

---

## 1. Information We Collect

### 1.1 Information You Provide

- **Google Account information:** When you sign in with Google to use the YouTube auto-publish feature, we receive your Google account email address and profile name. This is used solely to authenticate you and display your account status in the App.

### 1.2 Information Collected Automatically

- **Video files:** When you import or download a video for processing, the video file is stored locally on your device. We do not transmit, copy, or store your video files on any external server.
- **App usage data:** The App may collect anonymous crash logs and usage statistics to improve stability. This data does not identify you personally.

### 1.3 Information We Do NOT Collect

- We do **not** collect your name, phone number, location, contacts, or browsing history.
- We do **not** upload your video content to any server other than YouTube (when you explicitly choose to publish).
- We do **not** use cloud-based artificial intelligence, large language models, or remote inference.
- We do **not** sell, rent, or trade your data to third parties.

---

## 2. How We Use Your Information

- **Google Account email:** To authenticate your identity and display your signed-in status in Settings.
- **YouTube upload scope:** To upload video clips you explicitly choose to publish to your YouTube channel.
- **YouTube readonly scope:** To read your channel information for display in the App.
- **Local video files:** To analyze, score, trim, and export clips entirely on your device.
- **Crash logs:** To identify and fix bugs in future updates.

---

## 3. Google API Data Access

The App requests the following Google API scopes when you sign in for YouTube auto-publish:

| Scope | Purpose |
|-------|---------|
| `https://www.googleapis.com/auth/youtube.upload` | Upload video clips to your YouTube channel when you explicitly tap "Publish to YouTube". |
| `https://www.googleapis.com/auth/youtube.readonly` | Read your channel name and basic info for display in the App. |

**The App never uploads, modifies, or deletes videos without your explicit action.** Every upload requires you to tap the "Publish to YouTube" button and confirm.

---

## 4. Data Storage

### 4.1 On-Device Storage

All video processing runs **100% on your device**. Downloaded videos, analyzed clips, exported files, and settings are stored in your device's local storage under `Movies/VYRO/` and the App's private data directory.

### 4.2 No Cloud Storage

We do not operate any cloud servers for video processing or storage. Your video content never leaves your device except when you explicitly choose to upload to YouTube.

### 4.3 OAuth Token Storage

When you sign in with Google, your authentication token is stored locally on your device. The token is short-lived (approximately 1 hour) and is refreshed automatically when needed. You can revoke access at any time by signing out in Settings or via your Google Account security page.

---

## 5. Third-Party Services

### 5.1 Google Sign-In

The App uses Google Sign-In (play-services-auth) to authenticate your Google account. Google's privacy policy applies to the authentication process. See [Google Privacy Policy](https://policies.google.com/privacy).

### 5.2 YouTube Data API v3

When you choose to publish a clip, the App uses the YouTube Data API v3 to upload the video to your channel. YouTube's terms of service and privacy policy apply to uploaded content. See [YouTube Terms of Service](https://www.youtube.com/t/terms) and [YouTube Privacy Policy](https://policies.google.com/privacy).

### 5.3 No Other Third Parties

The App does not integrate with any analytics services, advertising networks, crash reporting SDKs, or other third-party data processors beyond Google Sign-In and YouTube Data API.

---

## 6. Data Sharing

We do **not** share your personal information or video content with any third party, except:

- When you explicitly choose to upload a video to YouTube (the video is sent directly from your device to YouTube's servers).
- When required by law, court order, or legal process.

---

## 7. Data Retention and Deletion

### 7.1 Video Files

Downloaded videos and exported clips remain on your device until you delete them. You can delete them at any time using the App's Library screen or your device's file manager.

### 7.2 Google Account Data

Your Google account email is stored only while you are signed in. Signing out in Settings removes the local token. You can also revoke the App's access to your Google account at [myaccount.google.com/permissions](https://myaccount.google.com/permissions).

### 7.3 Account and Data Deletion

To delete all data associated with the App:
1. Open VYRO Settings → tap "Sign Out" to remove your Google account.
2. Delete the App from your device. This removes all local files, tokens, and settings.
3. Revoke the App's Google access at [myaccount.google.com/permissions](https://myaccount.google.com/permissions).

---

## 8. Children's Privacy

The App is not directed to children under 13 years of age. We do not knowingly collect personal information from children. If you believe a child has provided personal information through the App, please contact us so we can delete it.

---

## 9. Your Rights

### 9.1 GDPR (European Economic Area and United Kingdom)

If you are in the EEA or UK, you have the right to:
- Access the personal data we hold about you
- Request deletion of your personal data
- Object to processing of your data
- Data portability

To exercise these rights, contact us at the email below. Since all data is stored on your device, deleting the App removes all your data immediately.

### 9.2 CCPA (California)

If you are a California resident, you have the right to:
- Know what personal information we collect
- Request deletion of your personal information
- Opt out of the sale of personal information (we do not sell any data)

---

## 10. Security

All video processing occurs on your device. Google OAuth tokens are stored using Android's secure storage mechanisms. We use HTTPS for all network communication with Google APIs. However, no method of electronic storage is 100% secure, and we cannot guarantee absolute security.

---

## 11. Changes to This Policy

We may update this Privacy Policy from time to time. When we do, we will update the "Last Updated" date at the top of this page. We encourage you to review this policy periodically.

---

## 12. Contact Us

If you have questions about this Privacy Policy, you can contact us at:

- **Email:** rhafiihza@gmail.com
- **GitHub:** [github.com/rhafiihza/vyro-clipper](https://github.com/rhafiihza/vyro-clipper)
- **Repository issues:** [github.com/rhafiihza/vyro-clipper/issues](https://github.com/rhafiihza/vyro-clipper/issues)

---

*This Privacy Policy is part of the VYRO application documentation.*