SUMMARY = "Configure TSN funtionalitie"
DESCRIPTION = "A tool to configure TSN funtionalities in user space"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ef58f855337069acd375717db0dbbb6d"

DEPENDS = "cjson libnl readline"

inherit pkgconfig

SRC_URI = "git://bitbucket.sw.nxp.com/dnind/tsntool.git;protocol=ssh;nobranch=1"
SRCREV = "cb654e4bb46387e5ed970dfff929d2b2efa1da33"

S = "${WORKDIR}/git"

do_compile_prepend() {
        mkdir -p ${S}/include/linux
        cp -r ${STAGING_KERNEL_DIR}/include/uapi/linux/tsn.h ${S}/include/linux
}     
do_install() {
    install -d ${D}${bindir} ${D}${libdir} 
    install -m 0755 ${S}/tsntool ${D}${bindir}
    install -m 0755 ${S}/tools/event ${D}${bindir}/
    install -m 0755 ${S}/tools/timestamping ${D}${bindir}/
    install -m 0755 ${S}/libtsn.so ${D}${libdir}
}

PACKAGES = "${PN}-dbg ${PN}"
FILES_${PN} = "${libdir}/libtsn.so ${bindir}/*"
INSANE_SKIP_${PN} += "file-rdeps rpaths dev-so"
COMPATIBLE_MACHINE = "(qoriq)"
PARALLEL_MAKE = ""
