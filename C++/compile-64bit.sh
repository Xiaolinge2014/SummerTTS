#!/bin/bash
#---------------------------颜色配置--------------------------
RED_COLOR='\E[1;31m'   #红
GREEN_COLOR='\E[1;32m' #绿
YELOW_COLOR='\E[1;33m' #黄
BLUE_COLOR='\E[1;34m'  #蓝
PINK='\E[1;35m'        #粉红
RES='\E[0m'
#---------------------------颜色配置--------------------------

rm -r build
ARCH_ABI=arm64-v8a
rm -r ${ARCH_ABI}
echo -e "${GREEN_COLOR}----------------------删除临时目录-------------------${RES}"
ANDROID_API=21
WORKER_COUNT=$(nproc)
mkdir $ARCH_ABI
mkdir build && cd build
make clean
PREFIX=/home/gyl/Desktop/crossCompile/SummerTTS/$ARCH_ABI
NDK_HOME=/mnt/SoftApp/Android/Sdk/ndk/25.2.9519653
echo -e "${GREEN_COLOR}----------------------开始编译${ARCH_ABI}-------------------${RES}"
cmake .. \
    -DCMAKE_TOOLCHAIN_FILE=$NDK_HOME/build/cmake/android.toolchain.cmake \
    -DCMAKE_SYSTEM_NAME=Android \
    -DCMAKE_SYSTEM-VERSION=$ANDROID_API \
    -DANDROID_PLATFORM=android-$ANDROID_API \
    -DANDROID_ABI=$ARCH_ABI \
    -DCMAKE_ANDROID_NDK=$NDK_HOME \
    -DCMAKE_BUILD_TYPE=Rlease \
    -DCMAKE_INSTALL_PREFIX=$PREFIX \
    -DCMAKE_ARCHIVE_OUTPUT_DIRECTORY=$PREFIX \
    -DCMAKE_LIBRARY_OUTPUT_DIRECTORY=$PREFIX

   # -DCMAKE_RUNTIME_OUTPUT_DIRECTORY=$PREFIX
    #-DCMAKE_ANDROID_ARCH_ABI=$ARCH_ABI \ (版本不同对abi的字段支持不一致)
    
make -j $WORKER_COUNT
make install || exit 1
echo -e "${GREEN_COLOR}----------------------完成编译${ARCH_ABI}-------------------${RES}"

