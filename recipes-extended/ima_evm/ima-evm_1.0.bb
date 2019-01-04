DESCRIPTION = "Linux Integrity Subsystem signed tool"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

RDEPENDS_${PN} = "bash keyutils e2fsprogs"
SRC_URI = "file://merge"
S = "${WORKDIR}"

do_install () {
    install -d ${D}/${bindir}
    install -m 0755 ${WORKDIR}/merge/sec_imaevm ${D}/${bindir}/
}
do_unpack[nostamp] = "1"
do_install[nostamp] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

ALLOW_EMPTY_${PN} = "1"
