do_install() {
    #install core on boot directory
    install -d ${D}/lib/firmware/

    ${OBJCOPY} -v -O binary ${B}/out/arm-plat-ls/core/tee.elf   ${B}/out/arm-plat-ls/core/tee.bin
    install -m 644 ${B}/out/arm-plat-ls/core/tee.bin ${D}/lib/firmware/tee_${MACHINE}.bin
    #install TA devkit
    install -d ${D}/usr/include/optee/export-user_ta/

    for f in  ${B}/out/arm-plat-ls/export-ta_${OPTEE_ARCH}/* ; do
        cp -aR  $f  ${D}/usr/include/optee/export-user_ta/
    done
}

