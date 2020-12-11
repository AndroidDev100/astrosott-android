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
-keep class com.kaltura.netkit.utils.** { *; }
-keep class com.kaltura.playkit.providers.** { *; }
-keep class com.kaltura.playkit.plugins.** { *; }
-keep class com.kaltura.playkit.plugins.ima.**{*;}
-keep class com.google.ads.**{*;}
-keep class com.kaltura.playkit.plugins.ads.ima.** { *; }
-keepclassmembers enum com.kaltura.playkit.ads.** { *; }
-keep class com.kaltura.playkit.* { *; } ## needed only for apps using MockMediaProvider
-dontwarn okio.**
-keep class com.google.android.exoplayer2.** {*;}
-keep class com.kaltura.client.** {*;}
-dontwarn  com.kaltura.**

#Retrofit Proguard
-keep class retrofit2.** {*;}
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

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

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
-keep class com.google.firebase.** {*;}

#Facebook Proguard with Audience
-keep class com.facebook.ads.** { *; }
-dontwarn com.facebook.ads.**
-keep class com.facebook.android.**{*;}


#Moengage Proguard
#-keep class com.moengage.** {*;}
#-dontwarn com.moengage.**

#IMA Proguard

-keep class com.google.obf.** { *; }
-keep interface com.google.obf.** { *; }

-keep class com.google.ads.interactivemedia.** { *; }
-keep interface com.google.ads.interactivemedia.** { *; }

#Aws Proguard
# Class names are needed in reflection
-keepnames class com.amazonaws.**
-keepnames class com.amazon.**
# Request handlers defined in request.handlers
-keep class com.amazonaws.services.**.*Handler
-dontwarn com.amazonaws.http.**
-dontwarn com.amazonaws.metrics.**

#Crashlatics Proguard
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
#Meaning crash report
-keepattributes SourceFile,LineNumberTable

#Common Enum Proguard
-keepclassmembers enum * { *; }

#///////////////////////////////////////////////
#Common Model Proguard
-keep public class * extends android.app.AndroidViewModel {*;}
-keep public class com.astro.sott.modelClasses.** {*;}
-keep public class com.astro.sott.baseModel.** {*;}
-keep public class com.astro.sott.callBacks.** {*;}
-keep public class com.astro.sott.activities.** {*;}
-keep public class com.astro.sott.networking.ksServices.** {*;}
-keep public class com.astro.sott.repositories.** {*;}

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
-keep class com.google.android.gms.** { *; }
-keep class org.apache.log4j.** { *; }
-keep class com.fasterxml.jackson.databind.** {*;}
-keep class org.apache.commons.logging.** {*;}
-keep class com.kaltura.** {*;}
-keep public class kotlin.reflect.jvm.internal.impl.builtins.* { public *; }
-keep class com.astro.sott.player.UI.** {*;}
-keep class  com.astro.sott.repositories.playerRepo.** {*;}
-keep interface android.support.** { *; }
-keep class android.support.** { *; }
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }
-keep public class com.astro.sott.ApplicationMain.** {*;}
-keep public class * extends android.app.AppCompatActivity {*;}






