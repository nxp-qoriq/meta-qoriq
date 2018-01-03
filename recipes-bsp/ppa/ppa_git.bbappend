SRCREV = "e5641434f00d75634a285341d810df4261daf5de"

PPA_PATH_ls1012a = "ppa/soc-ls1012/platform-frdm"

do_compile () {
    export ARMV8_TOOLS_DIR="${STAGING_BINDIR_TOOLCHAIN}"
    export ARMV8_TOOLS_PREFIX="${TARGET_PREFIX}"
    export CROSS_COMPILE="${WRAP_TARGET_PREFIX}"
    cd ${S}/${PPA_PATH}
    if [ ${MACHINE} = ls1012afrdm ];then
        oe_runmake frdm-fit
    else 
        oe_runmake rdb-fit
    fi
    cd ${S}
}

