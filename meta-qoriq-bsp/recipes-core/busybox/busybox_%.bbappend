FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

BUSYBOX_SPLIT_SUID:qoriq = "0"
ALTERNATIVE_PRIORITY[init] = "40"

SRC_URI:append:qoriq = " file://defconfig-fsl"

do_configure:prepend:qoriq () {
    cp ${WORKDIR}/defconfig-fsl ${WORKDIR}/defconfig
}

do_install:append:qoriq () {
    rm -f ${D}${sysconfdir}/init.d/rcS
    rm -f ${D}${sysconfdir}/init.d/rcK
    rm -f ${D}${sysconfdir}/inittab
}

