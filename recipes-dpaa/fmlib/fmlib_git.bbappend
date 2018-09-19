SRC_URI = "git://bitbucket.sw.nxp.com/scm/dash/fmlib.git;protocol=https;nobranch=1 \
"

SRCREV = "82d89adae917397324c5d74108305f6c9bae8d00"

TARGET_ARCH_FMLIB = "${DEFAULTTUNE}"
TARGET_ARCH_FMLIB_qoriq_arm = "arm"
TARGET_ARCH_FMLIB_qoriq_arm64 = "arm"
TARGET_ARCH_FMLIB_e5500 = "ppc32e5500"
TARGET_ARCH_FMLIB_e6500 = "ppc32e6500"
TARGET_ARCH_FMLIB_e500mc = "ppce500mc"
TARGET_ARCH_FMLIB_e500v2 = "ppce500v2"

FMLIB_TARGET = "libfm-${TARGET_ARCH_FMLIB}"
FMLIB_TARGET_t1 = "libfm-${TARGET_ARCH_FMLIB}-fmv3l"
