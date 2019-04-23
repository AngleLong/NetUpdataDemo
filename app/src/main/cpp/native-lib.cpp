#include <jni.h>
#include <string>

//这个主要是为了导入其中的方法
extern "C" {
extern int p_main(int argc, char *argv[]);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_angle_netupdatademo_MainActivity_bsPath(JNIEnv *env, jobject instance, jstring oldApk_,
                                                 jstring patch_, jstring output_) {

    //将java字符串转换成char指针
    const char *oldApk = env->GetStringUTFChars(oldApk_, 0);
    const char *patch = env->GetStringUTFChars(patch_, 0);
    const char *output = env->GetStringUTFChars(output_, 0);

    //bspatch ,oldfile ,newfile ,patchfile
    char *argv[] = {"", const_cast<char *>(oldApk), const_cast<char *>(output),
                    const_cast<char *>(patch)};
    p_main(4, argv);

    // 释放相应的指针gc
    env->ReleaseStringUTFChars(oldApk_, oldApk);
    env->ReleaseStringUTFChars(patch_, patch);
    env->ReleaseStringUTFChars(output_, output);
}