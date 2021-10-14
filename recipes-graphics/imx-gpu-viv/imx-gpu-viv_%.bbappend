FSL_MIRROR = "http://sun.ap.freescale.net/downloads"

DEPENDS:append:ls1028a = " patchelf-native"

RPROVIDES:${PN}:ls1028a += "imx-gpu-viv"
EXTRA_PROVIDES:append:ls1028a = " \
    virtual/libgl \
    virtual/libgles1 \
    virtual/libgles2 \
"
EXTRA_PROVIDES:append:ls1028a = " \
    virtual/libgbm \
"
PROVIDES_OPENVX:ls1028a   = "virtual/libopenvx"

IMX_PACKAGES_GBM:ls1028a = "libgbm-imx libgbm-imx-dev"
HAS_GBM:ls1028a = "true"
IS_MX8:ls1028a = "1"

FILES:libgbm-imx:ls1028a = "${libdir}/libgbm${REALSOLIBS} ${libdir}/libgbm${SOLIBSDEV} ${libdir}/libgbm_viv${SOLIBSDEV}"
FILES:libgbm-imx-dev:ls1028a = "${libdir}/pkgconfig/gbm.pc ${includedir}/gbm.h"
RDEPENDS:libgbm-imx:append:ls1028a = " libdrm"

RDEPENDS:libgal-imx:remove:ls1028a = "kernel-module-imx-gpu-viv"
COMPATIBLE_MACHINE = "(imxfbdev|imxgpu|qoriq)"
