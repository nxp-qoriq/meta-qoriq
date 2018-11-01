FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

BUSYBOX_SPLIT_SUID_qoriq = "0"
ALTERNATIVE_PRIORITY[init] = "40"

SRC_URI_append_qoriq = " file://defconfig-fsl"

do_configure_prepend_qoriq () {
    cp ${WORKDIR}/defconfig-fsl ${WORKDIR}/defconfig
}

do_install_append_qoriq () {
    rm -f ${D}${sysconfdir}/init.d/rcS
    rm -f ${D}${sysconfdir}/init.d/rcK
    rm -f ${D}${sysconfdir}/inittab
}

