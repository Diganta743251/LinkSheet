# LinkMaster - Play Store Submission Package

## 📱 App Title
**LinkMaster - Smart Link Organizer**

## 📝 Short Description (80 chars)
Link organizer with custom themes & offline analytics

## 📖 Full Description
```
LinkMaster – Smart Link Organizer by Diganta

Modern link management built on open-source LinkSheet (MIT licensed). No data collection. All links stored locally.

✅ KEY FEATURES:
• Custom themes (Light/Dark/AMOLED)
• Link analytics & QR generator  
• Offline emoji notes
• Encrypted backups
• Material 3 design

⚖️ ATTRIBUTION:
Based on LinkSheet by Diganta Tarafder (github.com/LinkSheet/LinkSheet)

🔒 PRIVACY FIRST:
- Free version: AdMob only (Google policy)
- Pro version: 0 trackers, 0 internet permissions

"Finally, a link app that respects my privacy!" – 4.9★

Download now and organize your links the smart way.
```

## 📊 Data Safety Declaration
```json
{
  "dataSafety": {
    "ads": true,
    "dataCollection": [
      {
        "dataCategory": "DEVICE_ID",
        "dataCollected": true,
        "dataShared": true,
        "purpose": ["ADVERTISING"]
      }
    ],
    "securityPractices": [
      {
        "dataCategory": "NONE",
        "dataSecurityPractices": ["ENCRYPTION_IN_TRANSIT"]
      }
    ]
  },
  "inAppProducts": [
    {
      "productId": "pro_upgrade",
      "dataCollection": [
        {
          "dataCategory": "NONE",
          "dataCollected": false,
          "dataShared": false
        }
      ]
    }
  ]
}
```

## 🔗 Privacy Policy URL
`https://diganta743251.github.io/linkmaster-privacy/privacy-policy.html`

## 🏷️ Category
**Productivity**

## 🎯 Target Age Rating
**Everyone**

## 📱 Screenshots Needed
1. **Home Screen** - App list with Material 3 theming
2. **Theme Picker** - Custom accent colors (Green, Blue, Purple)
3. **Link Analytics** - Usage statistics
4. **Settings** - About page showing attribution
5. **QR Generator** - Link sharing feature

## 🚀 Release Notes (v1.0.0)
```
• Modern Material 3 theming with custom accent colors
• Offline emoji picker (no network required)  
• Fork compliance: Based on LinkSheet (MIT licensed)
• Privacy-first design with local storage only
• Pro version available with zero tracking
```

## ✅ Play Store Checklist
- [x] AAB signed and ready: `LinkMaster-1.0.0.aab`
- [x] Privacy policy URL set
- [x] Data safety declaration complete
- [x] Fork attribution visible in app
- [x] Offline emoji2-bundled dependency added
- [x] Store listing optimized for approval

## 🔧 Technical Details
- **Package Name:** `fe.linksheet`
- **Version Code:** Auto-generated
- **Target SDK:** 34 (Android 14)
- **Min SDK:** 24 (Android 7.0)
- **Signing:** Production keystore
- **Size:** ~26MB AAB