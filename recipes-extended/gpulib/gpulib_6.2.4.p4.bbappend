DEPENDS_append = " kernel-module-qoriq-gpu"

PROVIDES = " \
    gpulib \
    virtual/egl \
    virtual/libgbm \
    virtual/libgles2 \
"

do_install() {
    install -d ${D}/opt ${D}${libdir} ${D}/usr/include
    cd  ls1028a/linux
    cp -a gpu-demos/opt/viv_samples/* ${D}/opt
    cp -a gpu-core/usr/include/* ${D}/usr/include
    rm -rf   gpu-core/usr/lib/libwayland-egl.so*
    cp -a gpu-core/usr/lib/* ${D}/usr/lib
    rm -rf gpu-core/${includedir}/vulkan/
}
