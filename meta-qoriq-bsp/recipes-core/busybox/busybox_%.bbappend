FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

BUSYBOX_SPLIT_SUID:qoriq = "0"
ALTERNATIVE_PRIORITY[init] = "40"

SRC_URI += " file://busybox-dd.cfg"

do_install:append:qoriq () {
    rm -f ${D}${sysconfdir}/init.d/rcS
    rm -f ${D}${sysconfdir}/init.d/rcK
    rm -f ${D}${sysconfdir}/inittab
}

