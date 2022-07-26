require recipes-core/meta/meta-toolchain.bb

TOOLCHAIN_OUTPUTNAME = "${DISTRO}-${TCLIBC}-${SDKMACHINE}-${TUNE_PKGARCH}-toolchain-${SDK_VERSION}"

MULTILIBS:pn-${PN} = ""
TOOLCHAIN_NEED_CONFIGSITE_CACHE += "zlib"
TOOLCHAIN_TARGET_TASK += " \
    glib-2.0 \
    glib-2.0-dev \
    libgomp \
    libgomp-dev \
    libgomp-staticdev \
    libstdc++-staticdev \
    ${TCLIBC}-staticdev \
"

TOOLCHAIN_HOST_TASK += " \
    nativesdk-dtc \
    nativesdk-u-boot-mkimage \
    nativesdk-qoriq-cst \
    nativesdk-perl-module-integer \
"

TOOLCHAIN_HOST_TASK:append:e500v2 = " \
    nativesdk-boot-format \
    nativesdk-boot-format-config \
"

