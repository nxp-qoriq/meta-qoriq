require recipes-core/meta/meta-toolchain.bb

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

CORE_SPECIFIC_ls102xa = ""
CORE_SPECIFIC_qoriq-arm64 = ""
CORE_SPECIFIC_e500v2 = ""

TOOLCHAIN_HOST_TASK += " \
    nativesdk-dtc \
    nativesdk-u-boot-mkimage \
    nativesdk-cst \
    ${CORE_SPECIFIC} \
"

TOOLCHAIN_HOST_TASK_append_e500v2 = " \
    nativesdk-boot-format \
    nativesdk-boot-format-config \
"

