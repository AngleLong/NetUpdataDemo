cmake_minimum_required(VERSION 3.4.1)

# 查找文件系统中指定模式的路径，如/* 匹配根目录的文件（注意路径）
file(GLOB bzip_source ${CMAKE_SOURCE_DIR}/bzip/*.c)

# 设置本地动态库 编译生成动态库
add_library(
        #模块名
        native-lib
        # 动态库/分享可以
        SHARED

        #源文件
        native-lib.cpp

        #配置相应的文件引用
        bspatch.c
        #这个相当与别名引用，上面设置了一个别名
        ${bzip_source}
)

#系统库，日志输出log
find_library(
        log-lib

        log)

#需要链接或者编译的库
target_link_libraries(
        native-lib

        ${log-lib})