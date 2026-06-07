# Haola Android App

一个将 Haola H5 网站包装成 Android 应用的项目。

## 功能

- 基于 WebView 的 H5 haola应用
- 支持 JavaScript 和 DOM Storage
- 支持缩放和手势操作
- 支持返回键导航
- 自适应布局

## 技术栈

- Kotlin
- Android SDK 34
- WebView

## 构建说明

### 环境要求

- Android Studio 或 Android SDK
- JDK 8 或更高版本
- Gradle 8.0

### 构建 APK

```bash
# 构建发布版 APK
./gradlew assembleRelease

# 构建调试版 APK
./gradlew assembleDebug
```

### 输出位置

- Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
- Release APK: `app/build/outputs/apk/release/app-release.apk`

## 网站地址

- H5 网站: https://haola.ru

## 版本信息

- 版本号: 1.0.0
- 版本代码: 1
- 最低 SDK: 24 (Android 7.0)
- 目标 SDK: 34 (Android 14)

## 许可证

MIT License
