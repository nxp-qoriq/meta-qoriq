DEPENDS:append:ls1028a = " virtual/egl"

do_install:append:ls1028a() {
    # imx-gpu-viv and mali-imx both provide /usr/include/KHR, so drop the mesa-gl one
    rm -rf ${D}${includedir}/KHR
}

COMPATIBLE_MACHINE:ls1028a = "(ls1028a)"
