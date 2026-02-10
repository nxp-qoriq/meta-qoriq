SUMMARY = "DPAA2 Management Complex Firmware"
LICENSE = "NXP-Binary-EULA"
LIC_FILES_CHKSUM = "file://LICENSE;md5=fd0dca2d99784178823b41340ab2d207"

inherit deploy

INHIBIT_DEFAULT_DEPS = "1"

SRC_URI = "git://github.com/nxp/qoriq-mc-binary;protocol=https;nobranch=1"
SRCREV = "258dd3a59457163fc21f6055f7b893ca94ce642f"

REGLEX:ls2088a = "ls2088a"
REGLEX:ls2080a = "ls2080a"
REGLEX:ls1088a = "ls1088a"
REGLEX:lx2160a = "lx216xa"
REGLEX:lx2162a = "lx216xa"

do_install () {
    install -d ${D}/boot
    install -m 755 ${S}/${REGLEX}/*.itb ${D}/boot
}

do_deploy () {
    install -d ${DEPLOYDIR}/mc_app
    install -m 755 ${S}/${REGLEX}/*.itb ${DEPLOYDIR}/mc_app
    # make a symlink to the latest binary
    ln -sf -r ${DEPLOYDIR}/mc_app/* ${DEPLOYDIR}/mc_app/mc.itb
}
addtask deploy before do_build after do_install

PACKAGES += "${PN}-image"
FILES:${PN}-image += "/boot"

INHIBIT_PACKAGE_STRIP = "1"

COMPATIBLE_MACHINE = "(qoriq-arm64)"
PACKAGE_ARCH = "${MACHINE_ARCH}"
