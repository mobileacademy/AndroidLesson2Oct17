# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-dontwarn android.support.v4.**
-dontwarn android.support.v7.**

# conflicts with proguard optimiation
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.util.** { *; }

-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*

-keepattributes *Annotation*,EnclosingMethod,Signature

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}


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

-keep class com.squareup.okhttp3.** {
*;
}

-keepattributes Signature
-keepattributes Annotation
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**
