do_compile () {
    export CROSS_COMPILE="${WRAP_TARGET_PREFIX}"
    cd ${S}/ppa
    if [ ${MACHINE} = ls1012afrdm ];then
        ./build  frdm-fit ${PPA_PATH}
        ./build  frdm-fit ${PPA_PATH}
    else
        ./build  rdb-fit ${PPA_PATH}
        ./build  rdb-fit ${PPA_PATH}
    fi
    cd ${S}
}

