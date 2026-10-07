# lestExec

A button-triggered prank app. Nothing happens until the "DO NOT PRESS" button is tapped —
then the screen flashes, a loud tone plays, and the wallpaper changes to a random pattern.

## How to build

1. Push this whole folder structure (as-is) to a GitHub repo on the `main` or `master` branch.
2. GitHub Actions will automatically run `.github/workflows/build-apk.yml`.
3. Once the workflow finishes, download the `lestExec-APK` artifact from the Actions run —
   it's a flat zip containing just the `.apk` file, no nested folders.
4. Install the APK on the target device (enable "install from unknown sources" if prompted).

## Notes

- Minimum Android version: 7.0 (API 24).
- Requests `SET_WALLPAPER` permission (normal permission, granted automatically on install).
- All effects (screen flash, tone, wallpaper change) run only inside the button's click handler —
  the app does nothing on its own when opened.
