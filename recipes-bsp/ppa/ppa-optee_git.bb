require ppa.inc

DEPENDS += "optee-os"

PPA_PATH_ls1046a = "ppa/soc-ls1046/platform-rdb"
PPA_PATH_ls1043a = "ppa/soc-ls2088/platform-rdb"
PPA_PATH_ls1012ardb = "ppa/soc-ls1012/platform-rdb"

do_compile() {
    export ARMV8_TOOLS_DIR="${STAGING_BINDIR_TOOLCHAIN}"
    export ARMV8_TOOLS_PREFIX="${TARGET_PREFIX}"
    export CROSS_COMPILE="${WRAP_TARGET_PREFIX}"
    cp ${DEPLOY_DIR_IMAGE}/optee/tee_${MACHINE}.bin ${S}/${PPA_PATH}/tee.bin
    cd ${S}/${PPA_PATH}
    oe_runmake rdb-fit spd=on
    cd ${S}
}

COMPATIBLE_MACHINE = "(ls1043a|ls1046a|ls1012ardb)"
