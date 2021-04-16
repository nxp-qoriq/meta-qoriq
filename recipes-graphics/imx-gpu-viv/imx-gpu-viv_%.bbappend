FSL_MIRROR = "http://sun.ap.freescale.net/downloads"

DEPENDS_append_ls1028a = " patchelf-native"

RPROVIDES_${PN}_ls1028a += "imx-gpu-viv"
EXTRA_PROVIDES_append_ls1028a = " \
    virtual/libgl \
    virtual/libgles1 \
    virtual/libgles2 \
"
EXTRA_PROVIDES_append_ls1028a = " \
    virtual/libgbm \
"
PROVIDES_OPENVX_ls1028a   = "virtual/libopenvx"

IMX_PACKAGES_GBM_ls1028a = "libgbm-imx libgbm-imx-dev"
HAS_GBM_ls1028a = "true"
IS_MX8_ls1028a = "1"

GLES3_HEADER_REMOVALS_ls1028a   = "gl32.h"

FILES_libgbm-imx_ls1028a = "${libdir}/libgbm${REALSOLIBS} ${libdir}/libgbm${SOLIBSDEV} ${libdir}/libgbm_viv${SOLIBSDEV}"
FILES_libgbm-imx-dev_ls1028a = "${libdir}/pkgconfig/gbm.pc ${includedir}/gbm.h"
RDEPENDS_libgbm-imx_append_ls1028a = " libdrm"

RDEPENDS_libgal-imx_remove_ls1028a = "kernel-module-imx-gpu-viv"
COMPATIBLE_MACHINE = "(imxfbdev|imxgpu|qoriq)"
