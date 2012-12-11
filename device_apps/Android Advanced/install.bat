call ant clean debug
call adb install -r bin/AndroidTest-debug.apk
call adb shell am start -n my.android_test/my.android_test.MainActivity