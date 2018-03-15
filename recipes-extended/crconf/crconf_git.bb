SUMMARY = "crconf -Linux crypto layer configuraton tool"
SECTION = "base"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://Makefile;beginline=1;endline=5;md5=0f77fc44eb5911007ae4ac9f6736e111"

SRC_URI = "git://git.code.sf.net/p/crconf/code \
    file://0001-Modify-the-Makefile-for-cross-compile.patch \
"

EXTRA_OEMAKE = "'CC=${CC}' 'HOSTCC=${CC}'"

SRCREV = "8bd996400d087028ba56b724abc1f5b378eaa77"

PV .= "+git${SRCPV}"

S = "${WORKDIR}/git"

do_install () {
       oe_runmake install  DESTDIR=${D}
}

FILES_${PN} += "/share/man/*  ${libdir}/* ${sbindir}/*"
