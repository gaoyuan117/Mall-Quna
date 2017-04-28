# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\AndroidStudio\sdk/tools/proguard/proguard-android.txt
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
##---------------Begin: proguard configuration common for all Android apps ----------
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes *Annotation*
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

-ignorewarnings

-keep public class com.hp.hpl.sparta.** { *; }
-keep public class net.sourceforge.pingyin4j.** { *; }

-keep public class com.slidingmenu.** { *; }

-keep public class com.baidu.** { *; }
-keep public class cn.thinkit.libtmfe.** { *; }
-keep public class vi.com.gdi.bgl.android.** { *; }
-keep public class android.support.** { *; }
-keep public class com.umeng.** { *; }
-keep public class com.tencent.** { *; }
-keep public class com.tencent.weibo.** { *; }
-keep public class com.baidu.location.** { *; }
-keep public class com.google.** { *; }
-keep public class com.google.gson.** { *; }

-keep class com.tencent.mm.sdk.** {*;}
-keep class com.tencent.wxop.** {*;}
-keep class com.tencent.a.** {*;}

-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.* { *; }
-keep class com.google.gson.examples.android.model.* { *; }
-keep class com.google.gson.* { *;}
-keep public class org.apache.commons.httpclient.** { *; }
-keep public class com.sina.sso.** { *; }
-keep public class com.weibo.sdk.android.** { *; }
-keep public class com.crittercism.** { *; }
-keep public class crittercism.android.** { *; }
-keep public class android.support.v4.app.** { *; }
-keep public class com.android.aizachi.activity { *; }
-keep public class com.android.zcomponent.http.api.** { *; }
-keep public class com.android.zcomponent.util.MyLayoutAdapter { *; }
-keep public class com.android.zcomponent.http.HttpRequest { *; }

-keep public class com.server.api.** { *; }
-keep public class com.android.zcomponent.communication.http.** { *; }


#银联支付
-keep class com.unionpay.** { *; }
-keep class cn.gov.pbc.tsm.client.mobile.android.bank.service.** { *; }
-keep class com.UCMobile.PayPlugin.** { *; }
-keep class com.unionpay.client3.tsm.** { *; }
-keep class com.unionpay.mobile.** { *; }
-keep class com.unionpay.uppay.** { *; }

-keep  public class java.util.HashMap {
	public <methods>;
}
-keep  public class java.lang.String {
	public <methods>;
}
-keep  public class java.util.List {
	public <methods>;
}

#环信
-keep class com.hyphenate.** {*;}
-keep class org.apache.** {*;}
-keep class com.parse.** {*;}
-dontwarn  com.hyphenate.**


# 以下两个命令配合让类的路径给删除了
-allowaccessmodification
-repackageclasses ''

# 记录生成的日志数据，在proguard目录下
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt

# 异常都可以忽略就打开
#-dontwarn
-dontwarn android.support.v4.**
-dontwarn org.springframework.**

-keep public class * extends android.app.Fragment
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-dontnote com.android.vending.licensing.ILicensingService


-keepnames class * implements java.io.Serializable

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Preserve all native method names and the names of their classes.
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Preserve static fields of inner classes of R classes that might be accessed
# through introspection.
-keepclassmembers class **.R$* {
  public static <fields>;
}

# Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#P++
-dontwarn com.pingplusplus.**
-keep class com.pingplusplus.** {*;}

-dontwarn  com.alipay.**
-keep class com.alipay.** {*;}

-dontwarn  com.ta.utdid2.**
-keep class com.ta.utdid2.** {*;}

-keep class com.ut.device.** {*;}

-dontwarn  com.tencent.**
-keep class com.tencent.** {*;}

-dontwarn  com.unionpay.**
-keep class com.unionpay.** {*;}

-dontwarn com.baidu.**
-keep class com.baidu.** {*;}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;}

# 支付宝
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}

# 如果你的工程是对外提供方法调用就打开
#-keep public class * {
#    public protected *;
#}

##---------------End: proguard configuration common for all Android apps ----------