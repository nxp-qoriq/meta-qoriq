FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SECURE_PATCHES = " file://0001-Correct-the-address-for-secure-boot-headers.patch \
"

SRC_URI_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'secure', '${SECURE_PATCHES}', '', d)}"

do_install_append_ls102xa () {
    unset i j
    for config in ${UBOOT_MACHINE}; do
        i=$(expr $i + 1);
        for type in ${UBOOT_CONFIG}; do
            j=$(expr $j + 1);
            if [ $j -eq $i ] && [ "${type}" = "sdcard-ifc-secure-boot" ]; then
                cp ${B}/${config}/u-boot-dtb.bin ${D}/boot/u-boot-dtb-${type}-${PV}-${PR}.${UBOOT_SUFFIX}
                cp ${B}/${config}/spl/u-boot-spl.bin ${D}/boot/u-boot-spl-${type}-${PV}-${PR}.${UBOOT_SUFFIX}
                ln -sf u-boot-dtb-${type}-${PV}-${PR}.${UBOOT_SUFFIX} ${D}/boot/u-boot-dtb.bin-${type}
                ln -sf u-boot-spl-${type}-${PV}-${PR}.${UBOOT_SUFFIX} ${D}/boot/u-boot-spl.bin-${type}
            fi
        done
        unset  j
    done
    unset i
}

do_deploy_append_ls102xa () {
    unset i j
    for config in ${UBOOT_MACHINE}; do
        i=$(expr $i + 1);
        for type in ${UBOOT_CONFIG}; do
            j=$(expr $j + 1);
            if [ $j -eq $i ] && [ "${type}" = "sdcard-ifc-secure-boot" ]; then
                cp ${B}/${config}/u-boot-dtb.bin ${DEPLOYDIR}/u-boot-dtb-${type}-${PV}-${PR}.${UBOOT_SUFFIX}
                cp ${B}/${config}/spl/u-boot-spl.bin ${DEPLOYDIR}/u-boot-spl-${type}-${PV}-${PR}.${UBOOT_SUFFIX}
                ln -sf u-boot-dtb-${type}-${PV}-${PR}.${UBOOT_SUFFIX} ${DEPLOYDIR}/u-boot-dtb.bin-${type}
                ln -sf u-boot-spl-${type}-${PV}-${PR}.${UBOOT_SUFFIX} ${DEPLOYDIR}/u-boot-spl.bin-${type}
            fi
        done
        unset  j
    done
    unset i
}
