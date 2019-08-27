SUMMARY = "Configure TSN funtionalitie"
DESCRIPTION = "A tool to configure TSN funtionalities in user space"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ef58f855337069acd375717db0dbbb6d"

DEPENDS = "cjson libnl readline"

inherit pkgconfig

SRC_URI = "git://bitbucket.sw.nxp.com/dnind/tsntool.git;protocol=ssh;nobranch=1"
SRCREV = "3b2f9025cb1ef51c62583a1ae2318f0b1d424236"

S = "${WORKDIR}/git"

do_install() {
    install -d ${D}${bindir} ${D}${libdir} 
    install -m 0755 ${S}/tsntool ${D}${bindir}
    install -m 0755 ${S}/tools/event ${D}${bindir}/
    install -m 0755 ${S}/tools/timestamping ${D}${bindir}/
    install -m 0755 ${S}/libtsn.so ${D}${libdir}
    ln -sf libtsn.so.1.0 ${D}${libdir}/libtsn.so
}
 
INSANE_SKIP_${PN} += "file-rdeps rpaths"
COMPATIBLE_MACHINE = "(qoriq)"
