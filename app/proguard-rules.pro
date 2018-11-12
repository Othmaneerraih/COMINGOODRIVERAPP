# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn okio.**
-dontwarn okhttp3.**
-dontwarn com.squareup.okhttp.**
-dontwarn com.google.appengine.**
-dontwarn javax.servlet.**
# Support classes for compatibility with older API versions
-dontwarn android.support.**
-dontnote android.support.**
 -dontwarn com.squareup.picasso.**
 -dontwarn com.squareup.okhttp.internal.**
 -dontwarn org.apache.commons.logging.**
 -dontwarn org.joda.convert.**
 -dontwarn com.firebase.**
 -keep class com.firebase.** { *; }
 -keep interface com.firebase.** { *; }

 -dontwarn javax.management.**
 -dontwarn javax.xml.**
 -dontwarn org.apache.**
 -dontwarn org.slf4j.**

     -ignorewarnings
-keep class * {
public private protected *;
}

