# 同步到 GitHub 的步骤

## 方法1：使用 GitHub 镜像功能（最简单）

1. 登录 https://github.com/new
2. 创建新仓库，命名为 `android-h5-wrapper`
3. 创建后，进入仓库设置 → 点击左侧"Import repository"或手动添加远程仓库：
   ```bash
   cd android-h5-wrapper
   git remote add github https://github.com/<你的用户名>/android-h5-wrapper.git
   git push github main
   ```

## 方法2：使用 Gitee 官方同步

1. 进入 Gitee 仓库：https://gitee.com/wangyf2015/android-h5-wrapper
2. 点击右上角"同步/镜像"
3. 选择"强制同步"，填写 GitHub 仓库地址
4. GitHub 会自动创建仓库并同步代码

## 同步后

代码同步到 GitHub 后，GitHub Actions 会自动开始构建 APK。
构建完成后，在 GitHub 仓库页面点击 "Actions" 标签即可下载 APK。

**预计构建时间：5-10分钟**
