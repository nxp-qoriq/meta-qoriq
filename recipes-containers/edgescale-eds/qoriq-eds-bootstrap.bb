SUMMARY = "eds-bootstrap application binary"
HOMEPAGE = "https://github.com/nxp/qoriq-eds-bootstrap.git"
LICENSE = "NXP-Binary-EULA"
LIC_FILES_CHKSUM = "file://NXP-Binary-EULA.txt;md5=685768ff8092cc783d95e3480cb9bdb1"

SRC_URI = "git://github.com/NXP/qoriq-eds-bootstrap.git;nobranch=1"
SRCREV = "72abfd9b61cc8a6d10e4808a87ff4ce1e2b99020"

ARCH_qoriq-arm = "arm"
ARCH_qoriq-arm64 = "arm64"
ARCH_mx8 = "arm64"

S = "${WORKDIR}/git"

do_compile[noexec] = "1"

do_install () {
    install -d ${D}/usr/local/bin
    cp -r  ${S}/${ARCH}/* ${D}/usr/local/bin
    chown -R root:root ${D}
}

INSANE_SKIP_${PN} += "already-stripped"
FILES_${PN} += "/usr/local/bin/*"
