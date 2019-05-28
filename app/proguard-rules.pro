# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\mizutani-k\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Appcompact
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

# Support design
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

# Dagger
-dontwarn dagger.internal.codegen.**
-keepclassmembers,allowobfuscation class * {
    @javax.inject.* *;
    @dagger.* *;
    <init>();
}

-keep class dagger.* { *; }
-keep class javax.inject.* { *; }
-keep class * extends dagger.internal.Binding
-keep class * extends dagger.internal.ModuleAdapter
-keep class * extends dagger.internal.StaticInjection

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# Moshi
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keep class com.squareup.moshi.** { *; }
-keep interface com.squareup.moshi.** { *; }
-keep @com.squareup.moshi.JsonQualifier interface *
-dontwarn com.squareup.moshi.**

-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-keepclassmembers class * {
    @com.squareup.moshi.FromJson <methods>;
    @com.squareup.moshi.ToJson <methods>;
}

# Moshi Models [ Coupon ]
-keepnames @kotlin.Metadata class jp.co.pise.studyapp.data.repository.impl.CouponItemApiModel
-keep class jp.co.pise.studyapp.data.repository.impl.CouponItemApiModel { *; }
-keepclassmembers class jp.co.pise.studyapp.data.repository.impl.CouponItemApiModel { *; }

-keepnames @kotlin.Metadata class jp.co.pise.studyapp.data.repository.impl.GetCouponApiChallenge
-keep class jp.co.pise.studyapp.data.repository.impl.GetCouponApiChallenge { *; }
-keepclassmembers class jp.co.pise.studyapp.data.repository.impl.GetCouponApiChallenge { *; }

-keepnames @kotlin.Metadata class jp.co.pise.studyapp.data.repository.impl.GetCouponApiResult
-keep class jp.co.pise.studyapp.data.repository.impl.GetCouponApiResult { *; }
-keepclassmembers class jp.co.pise.studyapp.data.repository.impl.GetCouponApiResult { *; }

-keepnames @kotlin.Metadata class jp.co.pise.studyapp.data.repository.impl.GetCouponItemApiModel
-keep class jp.co.pise.studyapp.data.repository.impl.GetCouponItemApiModel { *; }
-keepclassmembers class jp.co.pise.studyapp.data.repository.impl.GetCouponItemApiModel { *; }

-keepnames @kotlin.Metadata class jp.co.pise.studyapp.data.repository.impl.UseCouponApiChallenge
-keep class jp.co.pise.studyapp.data.repository.impl.UseCouponApiChallenge { *; }
-keepclassmembers class jp.co.pise.studyapp.data.repository.impl.UseCouponApiChallenge { *; }

-keepnames @kotlin.Metadata class jp.co.pise.studyapp.data.repository.impl.UseCouponApiResult
-keep class jp.co.pise.studyapp.data.repository.impl.UseCouponApiResult { *; }
-keepclassmembers class jp.co.pise.studyapp.data.repository.impl.UseCouponApiResult { *; }

-keepnames @kotlin.Metadata class jp.co.pise.studyapp.data.repository.impl.GetUsedCouponApiChallenge
-keep class jp.co.pise.studyapp.data.repository.impl.GetUsedCouponApiChallenge { *; }
-keepclassmembers class jp.co.pise.studyapp.data.repository.impl.GetUsedCouponApiChallenge { *; }

-keepnames @kotlin.Metadata class jp.co.pise.studyapp.data.repository.impl.GetUsedCouponApiResult
-keep class jp.co.pise.studyapp.data.repository.impl.GetUsedCouponApiResult { *; }
-keepclassmembers class jp.co.pise.studyapp.data.repository.impl.GetUsedCouponApiResult { *; }

-keepnames @kotlin.Metadata class jp.co.pise.studyapp.data.repository.impl.GetUsedCouponItemApiModel
-keep class jp.co.pise.studyapp.data.repository.impl.GetUsedCouponItemApiModel { *; }
-keepclassmembers class jp.co.pise.studyapp.data.repository.impl.GetUsedCouponItemApiModel { *; }

-keepnames @kotlin.Metadata class jp.co.pise.studyapp.data.repository.impl.RefreshUsedCouponApiChallenge
-keep class jp.co.pise.studyapp.data.repository.impl.RefreshUsedCouponApiChallenge { *; }
-keepclassmembers class jp.co.pise.studyapp.data.repository.impl.RefreshUsedCouponApiChallenge { *; }

-keepnames @kotlin.Metadata class jp.co.pise.studyapp.data.repository.impl.RefreshUsedCouponApiResult
-keep class jp.co.pise.studyapp.data.repository.impl.RefreshUsedCouponApiResult { *; }
-keepclassmembers class jp.co.pise.studyapp.data.repository.impl.RefreshUsedCouponApiResult { *; }

# Moshi Models [ JwtAuth ]
-keepnames @kotlin.Metadata class jp.co.pise.studyapp.data.repository.impl.RefreshTokenApiChallenge
-keep class jp.co.pise.studyapp.data.repository.impl.RefreshTokenApiChallenge { *; }
-keepclassmembers class jp.co.pise.studyapp.data.repository.impl.RefreshTokenApiChallenge { *; }

-keepnames @kotlin.Metadata class jp.co.pise.studyapp.data.repository.impl.RefreshTokenApiResult
-keep class jp.co.pise.studyapp.data.repository.impl.RefreshTokenApiResult { *; }
-keepclassmembers class jp.co.pise.studyapp.data.repository.impl.RefreshTokenApiResult { *; }

# Moshi Models [ News ]
-keepnames @kotlin.Metadata class jp.co.pise.studyapp.data.repository.impl.GetNewsApiResult
-keep class jp.co.pise.studyapp.data.repository.impl.GetNewsApiResult { *; }
-keepclassmembers class jp.co.pise.studyapp.data.repository.impl.GetNewsApiResult { *; }

-keepnames @kotlin.Metadata class jp.co.pise.studyapp.data.repository.impl.GetNewsItemApiModel
-keep class jp.co.pise.studyapp.data.repository.impl.GetNewsItemApiModel { *; }
-keepclassmembers class jp.co.pise.studyapp.data.repository.impl.GetNewsItemApiModel { *; }

# Moshi Models [ Product ]
-keepnames @kotlin.Metadata class jp.co.pise.studyapp.data.repository.impl.GetProductApiResult
-keep class jp.co.pise.studyapp.data.repository.impl.GetProductApiResult { *; }
-keepclassmembers class jp.co.pise.studyapp.data.repository.impl.GetProductApiResult { *; }

-keepnames @kotlin.Metadata class jp.co.pise.studyapp.data.repository.impl.ProductItemApiModel
-keep class jp.co.pise.studyapp.data.repository.impl.ProductItemApiModel { *; }
-keepclassmembers class jp.co.pise.studyapp.data.repository.impl.ProductItemApiModel { *; }

# Moshi Models [ User ]
-keepnames @kotlin.Metadata class jp.co.pise.studyapp.data.repository.impl.LoginApiChallenge
-keep class jp.co.pise.studyapp.data.repository.impl.LoginApiChallenge { *; }
-keepclassmembers class jp.co.pise.studyapp.data.repository.impl.LoginApiChallenge { *; }

-keepnames @kotlin.Metadata class jp.co.pise.studyapp.data.repository.impl.LoginApiChallenge
-keep class jp.co.pise.studyapp.data.repository.impl.LoginApiChallenge { *; }
-keepclassmembers class jp.co.pise.studyapp.data.repository.impl.LoginApiChallenge { *; }

-keepnames @kotlin.Metadata class jp.co.pise.studyapp.data.repository.impl.LoginApiResult
-keep class jp.co.pise.studyapp.data.repository.impl.LoginApiResult { *; }
-keepclassmembers class jp.co.pise.studyapp.data.repository.impl.LoginApiResult { *; }

-keepnames @kotlin.Metadata class jp.co.pise.studyapp.data.repository.impl.GetUserApiChallenge
-keep class jp.co.pise.studyapp.data.repository.impl.GetUserApiChallenge { *; }
-keepclassmembers class jp.co.pise.studyapp.data.repository.impl.GetUserApiChallenge { *; }

-keepnames @kotlin.Metadata class jp.co.pise.studyapp.data.repository.impl.GetUserApiResult
-keep class jp.co.pise.studyapp.data.repository.impl.GetUserApiResult { *; }
-keepclassmembers class jp.co.pise.studyapp.data.repository.impl.GetUserApiResult { *; }

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# Okio
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**

# Other
-dontwarn org.jetbrains.annotations.**
-keep class kotlin.Metadata { *; }

# Log
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}