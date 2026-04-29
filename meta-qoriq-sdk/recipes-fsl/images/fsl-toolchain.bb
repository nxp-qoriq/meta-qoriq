require recipes-core/meta/meta-toolchain.bb
SDK_VERSION = "6.18-wrynose"

TOOLCHAIN_OUTPUTNAME = "${DISTRO}-${TCLIBC}-${SDKMACHINE}-${TUNE_PKGARCH}-toolchain-${SDK_VERSION}"

MULTILIBS:pn-${PN} = ""
TOOLCHAIN_NEED_CONFIGSITE_CACHE += "zlib"
TOOLCHAIN_TARGET_TASK += " \
"

TOOLCHAIN_HOST_TASK += " \
    nativesdk-dtc \
    nativesdk-u-boot-mkimage \
    nativesdk-qoriq-cst \
    nativesdk-perl-module-integer \
    nativesdk-openssl-dev \
    nativesdk-gnutls-dev \
"

TOOLCHAIN_HOST_TASK:append:e500v2 = " \
    nativesdk-boot-format \
    nativesdk-boot-format-config \
"

