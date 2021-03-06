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

#Kaltura Proguard
-keep class com.kaltura.playkit.* { *; } ## needed only for apps using MockMediaProvider
-dontwarn okio.**
-dontwarn  com.kaltura.**

#Retrofit Proguard
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions

#Branch Proguard
#need to test

#appsflyer
#need to test

#Glide Proguard
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#Google play services


-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

#FCM Proguard
-dontwarn com.google.firebase.**

#Facebook Proguard with Audience
-dontwarn com.facebook.ads.**


#Moengage Proguard
#-keep class com.moengage.** {*;}
#-dontwarn com.moengage.**

#IMA Proguard



#Aws Proguard
# Class names are needed in reflection
# Request handlers defined in request.handlers
-keep class com.amazonaws.services.**.*Handler
-dontwarn com.amazonaws.http.**
-dontwarn com.amazonaws.metrics.**

#Crashlatics Proguard
-dontwarn com.crashlytics.**
#Meaning crash report
-keepattributes SourceFile,LineNumberTable

#Common Enum Proguard
-keepclassmembers enum * { *; }

#///////////////////////////////////////////////
#Common Model Proguard

-dontwarn com.dialog.dialoggo.repositories.**
-dontwarn com.dialog.dialoggo.networking.ksServices.**
-dontwarn com.dialog.dialoggo.baseModel.**
-dontwarn com.dialog.dialoggo.activities.**
-dontwarn com.dialog.dialoggo.callBacks.**

#Common null warnings
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault


#Common warnings
-dontwarn com.google.android.gms.**
-dontwarn  org.apache.log4j.**
-dontwarn  com.fasterxml.jackson.databind.**
-dontwarn org.apache.commons.logging.**
-dontwarn retrofit2.**
-dontwarn com.dialog.dialoggo.player.UI.**
-dontwarn  com.dialog.dialoggo.repositories.playerRepo.**
# The following are referenced but aren't required to run
-dontwarn com.fasterxml.jackson.**
-dontwarn org.apache.commons.logging.**
# Android 6.0 release removes support for the Apache HTTP client
-dontwarn org.apache.http.**
# The SDK has several references of Apache HTTP client
-keep public class kotlin.reflect.jvm.internal.impl.builtins.* { public *; }
-dontwarn android.support.design.**
-keep public class android.support.design.R$* { *; }






