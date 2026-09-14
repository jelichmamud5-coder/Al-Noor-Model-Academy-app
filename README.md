# AL-NOOR MODEL ACADEMY — Android School Management App v2.0

This is a native Android Java project for **AL-NOOR MODEL ACADEMY**, using the supplied school logo.

## Included
- Role-based login UI: Admin, Teacher, Student, Parent
- Firebase Authentication via REST API
- Firebase Realtime Database via REST API
- Notices, routine, homework, results, attendance, fees, study materials and contact screens
- Admin/Teacher notice publishing
- Offline/demo mode when Firebase is not configured
- Firebase database security rules template

## Important: Firebase setup is required for real online data
1. Create a Firebase project at https://console.firebase.google.com/
2. Enable **Authentication → Sign-in method → Email/Password**.
3. Create a **Realtime Database**.
4. Copy the Web API key and database URL into:
   `app/src/main/res/values/config.xml`
5. Deploy `database.rules.json` as your Realtime Database rules.
6. Create user accounts in Firebase Authentication. For each user, create:
   `/users/<UID>` with fields such as:
   `{ "name": "Teacher Name", "role": "teacher", "class": "III" }`
   Roles must be `admin`, `teacher`, `student`, or `parent`.
7. Open the project in Android Studio and build **Generate Signed Bundle / APK**.

## Suggested database structure
```
users/{uid}
notices/{noticeId}
routine/{class}/{day}
homework/{class}/{id}
attendance/{date}/{studentId}
results/{exam}/{studentId}
fees/{studentId}/{feeId}
materials/{class}/{id}
```

## Security note
The included rules are a starting point. Before production use, tighten read permissions so students/parents can only read records belonging to their own account/child. Do not put private service-account keys in the Android app.

## Push notifications
The current version uses online notices. True push notifications require Firebase Cloud Messaging plus a trusted server/cloud function. Add this after the Firebase project is connected.
