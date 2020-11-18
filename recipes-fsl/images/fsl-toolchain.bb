require recipes-core/meta/meta-toolchain.bb

TOOLCHAIN_OUTPUTNAME = "${DISTRO}-${TCLIBC}-${SDKMACHINE}-${TUNE_PKGARCH}-toolchain-${SDK_VERSION}"

MULTILIBS_pn-${PN} = ""
TOOLCHAIN_NEED_CONFIGSITE_CACHE += "zlib"
TOOLCHAIN_TARGET_TASK += " \
    dtc-staticdev \
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
    nativesdk-cst \
"

TOOLCHAIN_HOST_TASK_append_e500v2 = " \
    nativesdk-boot-format \
    nativesdk-boot-format-config \
"

