
# Android Hidden Camera

使用 [android-hidden-camera](https://github.com/kevalpatel2106/android-hidden-camera) 这个库写的一个简单的 Android 隐藏相机 App (Package: me.sagan.magic)
。

[下载 APK](https://github.com/sagan/android-hidden-camera/releases) (API Level 25, Android 7.1.1)

## Usage

1. 打开程序 (显示的名称为 "Magic")。
2. 点击 "Toggle Service" 启动服务。第一次启动服务时，会打开手机系统设置里的 "Draw over other app" 管理页面，请在这里授权本程序该权限。服务运行时，会在 notifications 区域显示一个 "Magic service is running" 的通知。
3. 在任意画面（包括锁屏时），按音量+/-键、或触摸屏幕右下角区域(128x128像素范围)拍照（使用后置摄像头、最大像素），拍照完成后手机会震动一下。照片会自动保存到外置存储里的 DCIM/magic 文件夹里。
4. 再次点击程序里的 "Toggle Service" 停止服务。

注：部分 Android 12 设备可能无法使用音量+/-键拍照，这是一个[系统 bug](https://issuetracker.google.com/issues/201546605)。

## Permissions

程序所需权限：

* CAMERA：拍照
* WRITE_EXTERNAL_STORAGE：将照片保存到 DCIM 相机文件夹
* VIBRATE：拍照完成时震动
* SYSTEM_ALERT_WINDOW：即 "Draw over other app" 权限，需要单独在系统“设置”里手动授权（启动程序服务时，如果没有该权限，会自动打开“设置”里的对应页面以引导用户授权该权限）。
