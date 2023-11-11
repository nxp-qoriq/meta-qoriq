
DEPENDS:append:ls1028a = " patchelf-native"

RPROVIDES:${PN}:ls1028a += "imx-gpu-viv"
EXTRA_PROVIDES:append:ls1028a = " \
    virtual/libgles1 \
    virtual/libgles2 \
"
EXTRA_PROVIDES:append:ls1028a = " \
    virtual/libgbm \
"
PROVIDES_OPENVX:ls1028a   = ""
PROVIDES_OPENGLES3:ls1028a = ""
PROVIDES_REMOVE_OPENCL:ls1028a = "${PROVIDES_OPENCL}"

PACKAGES_VULKAN:ls1028a = "libvulkan-imx libvulkan-imx-dev"

# Keep IMX_PACKAGES_GBM to back-compatiable to Q3 release
IMX_PACKAGES_GBM:ls1028a = "libgbm-imx libgbm-imx-dev"

PACKAGES_GBM:ls1028a          = "libgbm-imx libgbm-imx-dev"
PACKAGES_VULKAN:ls1028a       = "libvulkan-imx libvulkan-imx-dev"
HAS_GBM:ls1028a = "true"
IS_MX8:ls1028a = "1"

FILES:libgbm-imx:ls1028a = "${libdir}/libgbm${REALSOLIBS} ${libdir}/libgbm${SOLIBSDEV} ${libdir}/libgbm_viv${SOLIBSDEV}"
FILES:libgbm-imx-dev:ls1028a = "${libdir}/pkgconfig/gbm.pc ${includedir}/gbm.h"
RDEPENDS:libgbm-imx:append:ls1028a = " libdrm"

RDEPENDS:libegl-imx:append:ls1028a = " libdrm"

RDEPENDS:libgal-imx:remove:ls1028a = "kernel-module-imx-gpu-viv"
COMPATIBLE_MACHINE = "(imxfbdev|imxgpu|qoriq)"
