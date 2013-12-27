LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
include   ../../workspace/OpenCV-2.4.6-android-sdk/sdk/native/jni/OpenCV.mk
LOCAL_MODULE    := drawNative
LOCAL_SRC_FILES := drawNative.cpp
LOCAL_LDLIBS +=  -llog -ldl
include $(BUILD_SHARED_LIBRARY)
