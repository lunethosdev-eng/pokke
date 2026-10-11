# Pokke ProGuard rules
-keep class com.poke.music.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-dontwarn io.ktor.**
