# Overrides for Qoriq
SRC_URI:append:qoriq = " ${NXP_PATCHES}"
# override the effect of "inherit allarch"
python allarch_package_arch_handler:prepend:qoriq () {
    return
}

PACKAGE_ARCH:qoriq = "${MACHINE_SOCARCH}"
