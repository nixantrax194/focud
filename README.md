# Study Focus 2.0 — Android (Cloud APK Build)

A native Android upgrade of the original Study Focus web app.

## Included
- Existing Study Focus WebView UI and local study data.
- Native foreground focus timer with persistent notification and background countdown.
- Timer completion notification even when the app UI is not open.
- Native onboarding flow.
- Android Focus Mode: choose distracting launcher apps to shield during focus sessions.
- Usage Access + Display over other apps permission setup.
- Native app picker for blocked apps.
- Polished adaptive app icon.
- Dark Study Focus visual language retained.
- **GitHub Actions cloud build** — no Android Studio, Android SDK, or local Gradle installation required.

## Build APK without Android Studio

1. Create a GitHub repository (public or private).
2. Upload the contents of this project to the repository root. The `.github/workflows/build-apk.yml` file must be present.
3. Open the repository on GitHub and select **Actions**.
4. Select **Build Study Focus APK**.
5. Tap **Run workflow** and choose the `main` branch.
6. Wait for the green check mark.
7. Open the completed workflow run.
8. Scroll to **Artifacts** and download **StudyFocus-debug-apk**.
9. Extract the ZIP and install the `.apk` on your Android phone.

The workflow uses GitHub's hosted Linux runner, installs JDK 17 and Android SDK packages, installs Gradle 8.7, and runs `assembleDebug`.

## First launch
Onboarding asks for notification permission and provides buttons for Usage Access and Display over other apps. These are optional until you enable Focus Mode.

## Focus Mode
Android cannot silently grant Usage Access or overlay permissions. The user must enable them in Android Settings. The app only shields packages selected by the user, and only while a Study Focus session is active.

## Notes
The original HTML app remains inside `app/src/main/assets/index.html`. Native timer calls are bridged from the existing JavaScript so the current UI, subjects, schedules, countdowns, history and progress remain intact.
