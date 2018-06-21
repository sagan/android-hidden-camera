
# Android Hidden Camera

使用 [android-hidden-camera](https://github.com/kevalpatel2106/android-hidden-camera) 这个库写的一个简单的 Android 隐藏相机 App。

## Usage

1. 打开程序 (显示的名称为 "Magic")
2. 点击 "Toggle Service" 启动服务。服务运行时，会在 notifications 区域显示一个 "Magic service is running" 的通知。
3. 在任意画面（包括锁屏时）按音量+/-键拍照（使用后置摄像头、最大像素），拍照完成后手机会震动一下。照片会自动保存到外置存储里的 DCIM 相机文件夹里。
4. 再次点击程序里的 "Toggle Service" 停止服务。
