do_compile() {
    export CROSS_COMPILE="${WRAP_TARGET_PREFIX}"
    cp ${RECIPE_SYSROOT}/lib/firmware/tee_${MACHINE}.bin ${S}/ppa/soc-${PPA_PATH}/tee.bin
    cd ${S}/ppa
    ./build rdb-fit ${PPA_PATH}
    ./build rdb-fit spd=on ${PPA_PATH}
    cd ${S}
}
