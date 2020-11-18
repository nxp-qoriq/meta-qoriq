# Copyright (C) 2020 NXP

require recipes-security/optee/optee-os.nxp.inc

DEPENDS_append = " dtc-native"
SRCBRANCH = "imx_3.10.y"
SRCREV = "b609848fdbe443af4c272b29068dbcc424939d50"

PLATFORM = "ls"
PLATFORM_FLAVOR                 ?= "${MACHINE}"
PLATFORM_FLAVOR_ls1088ardb-pb   = "ls1088ardb"
PLATFORM_FLAVOR_ls1046afrwy 	= "ls1046ardb"
PLATFORM_FLAVOR_lx2162aqds 	= "lx2160aqds"

EXTRA_OEMAKE_append = " CFG_ARM64_core=y"

do_compile_append() {  
    if [ ${MACHINE} = ls1012afrwy ]; then
        mv ${B}/core/tee-raw.bin  ${B}/core/tee_512mb.bin
        oe_runmake CFG_DRAM0_SIZE=0x40000000 all
    fi
    mv ${B}/core/tee-raw.bin  ${B}/core/tee.bin
}


do_install () {
    install -d ${D}${nonarch_base_libdir}/firmware/
    install -m 644 ${B}/core/*.bin ${D}${nonarch_base_libdir}/firmware/

    if [ ${MACHINE} = ls1012afrwy ]; then
        install -m 644 ${B}/core/tee_512mb.bin ${D}/lib/firmware/tee_${MACHINE}_512mb.bin
    fi
    install -m 644 ${B}/core/tee.bin ${D}/lib/firmware/tee_${MACHINE}.bin
    
    #install TA devkit
    install -d ${D}/usr/include/optee/export-user_ta/

    for f in  ${B}/export-ta_${OPTEE_ARCH}/* ; do
        cp -aR  $f  ${D}/usr/include/optee/export-user_ta/
    done
}

do_deploy() {
        install -d ${DEPLOYDIR}/optee
	install -m 644 ${B}/core/*.bin ${DEPLOYDIR}/optee
}

addtask deploy after do_compile before do_install

COMPATIBLE_MACHINE = "(qoriq-arm64)"
