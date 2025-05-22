#!/bin/bash

# ========== 用户自定义部分 ==========

# 设置项目根目录
ROOT_DIR="/Users/ghj/work/bootnava/SmartEngine"

# 设置包名映射
OLD_PACKAGE="com.alibaba"
NEW_PACKAGE="com.bootnova"

# 转换为路径格式
OLD_PATH=${OLD_PACKAGE//./\/}
NEW_PATH=${NEW_PACKAGE//./\/}

# ========== 脚本逻辑部分 ==========

echo "🚀 开始批量替换包名 $OLD_PACKAGE → $NEW_PACKAGE..."

# 1. 处理 Java 文件中的包名和导入语句
echo "📄 处理 Java 文件中的包名和导入语句..."
find "$ROOT_DIR" -name "*.java" -type f -print0 | xargs -0 perl -pi -e "s/package\s+$OLD_PACKAGE/package $NEW_PACKAGE/g"
find "$ROOT_DIR" -name "*.java" -type f -print0 | xargs -0 perl -pi -e "s/import\s+$OLD_PACKAGE/import $NEW_PACKAGE/g"
find "$ROOT_DIR" -name "*.java" -type f -print0 | xargs -0 perl -pi -e "s/\b$OLD_PACKAGE\b/$NEW_PACKAGE/g"

# 2. 处理 XML 文件中的包名引用
echo "📄 处理 XML 文件中的包名引用..."
find "$ROOT_DIR" -name "*.xml" -type f -print0 | xargs -0 perl -pi -e "s/\b$OLD_PACKAGE\b/$NEW_PACKAGE/g"

# 3. 处理 properties 文件中的包名引用
echo "📄 处理 properties 文件中的包名引用..."
find "$ROOT_DIR" -name "*.properties" -type f -print0 | xargs -0 perl -pi -e "s/\b$OLD_PACKAGE\b/$NEW_PACKAGE/g"

# 4. 处理 pom.xml 文件中的 groupId
echo "📄 处理 pom.xml 文件中的 groupId..."
find "$ROOT_DIR" -name "pom.xml" -type f -print0 | xargs -0 perl -pi -e "s/<groupId>$OLD_PACKAGE\.smart\.framework<\/groupId>/<groupId>$NEW_PACKAGE\.smart\.framework<\/groupId>/g"

# 5. 移动 Java 文件到新的包路径
echo "📂 移动 Java 文件到新的包路径..."

# 查找所有包含旧路径的目录
find "$ROOT_DIR" -type d -path "*/$OLD_PATH*" | sort -r | while read -r dir; do
  # 计算新目录路径
  new_dir="${dir/$OLD_PATH/$NEW_PATH}"
  
  # 创建新目录
  if [ ! -d "$new_dir" ]; then
    mkdir -p "$new_dir"
    echo "  创建目录: $new_dir"
  fi
  
  # 移动文件
  if [ -d "$dir" ]; then
    for file in "$dir"/*; do
      if [ -f "$file" ]; then
        new_file="${file/$OLD_PATH/$NEW_PATH}"
        mv "$file" "$new_file"
        echo "  移动文件: $file → $new_file"
      fi
    done
  fi
done

# 6. 清理空目录
echo "🧹 清理空目录..."
find "$ROOT_DIR" -type d -empty -delete

echo "✅ 包名替换完成！从 $OLD_PACKAGE 到 $NEW_PACKAGE"
echo "⚠️ 请在 IDE 中验证更改并运行测试以确保一切正常工作"
