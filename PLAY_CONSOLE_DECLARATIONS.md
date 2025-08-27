# Play Console Declarations for LinkMaster

## Foreground Service Declaration

**Service Name:** InterconnectService
**Purpose:** Binder Transactions for inter-process communication
**Declaration:** "Uses FOREGROUND_SERVICE for inter-process communication (Binder Transactions). Required for LinkMaster's core functionality of handling link intents from other apps."

## Permission Declarations

### INTERNET Permission
**Declaration:** "Used only for AdMob ads in the free version. Pro version operates completely offline with 0 internet usage."

### ACCESS_NETWORK_STATE Permission
**Declaration:** "Used to check network connectivity before showing AdMob ads. Ensures ads are only displayed when internet is available."

### FOREGROUND_SERVICE Permission
**Declaration:** "Required for InterconnectService to handle link intents from other applications in the background."

### POST_NOTIFICATIONS Permission
**Declaration:** "Used for app notifications and user feedback. No marketing or promotional content."

### INTERACT_ACROSS_PROFILES Permission
**Declaration:** "Required for LinkMaster to handle links across different user profiles on multi-user devices."

### ACCESS_DOWNLOAD_MANAGER Permission
**Declaration:** "Used for downloading files when users choose to download content instead of opening links."

### PACKAGE_USAGE_STATS Permission
**Declaration:** "Used to sort apps by usage frequency in the link handler list. Improves user experience by showing most-used apps first."

### Shizuku Permissions
**Declaration:** "Used for advanced system integration features. Shizuku is a trusted system integration framework."

## Data Collection Declaration

**Declaration:** "LinkMaster does not collect, transmit, or store any user data. All link history is stored locally on the device. No analytics, tracking, or personal information is collected."

## AdMob Declaration

**Declaration:** "AdMob is used only in the free version for monetization. Pro version is completely ad-free and operates offline. AdMob usage follows Google's privacy policy."

## Fork Attribution Declaration

**Declaration:** "LinkMaster is based on LinkSheet by Diganta Tarafder (github.com/link-sheet/link-sheet) and is licensed under MIT. Proper attribution is provided in the app's About section."

## Storage Access Declaration

**Declaration:** "LinkMaster uses Android's Storage Access Framework for data export/import. No dangerous storage permissions are required. All file operations are user-initiated and transparent."

## Target SDK Declaration

**Declaration:** "LinkMaster targets Android 14 (API 34) and uses modern Android APIs including Storage Access Framework, Foreground Service Types, and Material 3 design components."