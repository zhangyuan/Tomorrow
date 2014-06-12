Tomorrow
========

The app is built with AVOS Cloud as backend server and Android Studio as IDE.

# How to build the app

1. Create an app on <https://cn.avoscloud.com/>.
2. Create `Post`, `Reply`, `ReplyLog` on AVOS control pannel. The schemas are as below:
	* Post: title(String), image(File), content(String)
	* Reply: content(String), post(Pointer, targets at Post)
	* ReplyLog: deviceId(String), line1Number(String), subscribedId(String), reply(Pointer, targets at Reply)
3. Make sure all classes have correct authorities.
4. Copy `tomorrow/src/main/assets/app.properties.example` to `tomorrow/src/main/assets/app.properties`, then copy the app_id and app_key from AVOS cloud website and paste into `tomorrow/src/main/assets/app.properties`.
5. Build the app in Android Studio.
