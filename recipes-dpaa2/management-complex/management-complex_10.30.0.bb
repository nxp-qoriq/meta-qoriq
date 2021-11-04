SUMMARY = "DPAA2 Management Complex Firmware"
LICENSE = "NXP-Binary-EULA"
LIC_FILES_CHKSUM = "file://NXP-Binary-EULA.txt;md5=2cb2fee5d3558ee2a81331ed121647ad"

inherit deploy

INHIBIT_DEFAULT_DEPS = "1"

SRC_URI = "git://github.com/nxp/qoriq-mc-binary;nobranch=1"
SRCREV = "00dce4b2456789bac0fb660f00e7d6796d047405"

S = "${WORKDIR}/git"

REGLEX_ls2088a = "ls2088a"
REGLEX_ls2080a = "ls2080a"
REGLEX_ls1088a = "ls1088a"
REGLEX_lx2160a = "lx2160a"
REGLEX_lx2162a = "lx2160a"

do_install () {
    install -d ${D}/boot
    install -m 755 ${S}/${REGLEX}/*.itb ${D}/boot
}

do_deploy () {
    install -d ${DEPLOYDIR}/mc_app
    install -m 755 ${S}/${REGLEX}/*.itb ${DEPLOYDIR}/mc_app
    # make a symlink to the latest binary
    for mc_binary in `find ${DEPLOYDIR}/mc_app -type f -printf "%f\n" |sort`;do
        ln -sfT ${mc_binary} ${DEPLOYDIR}/mc_app/mc.itb
    done
}
addtask deploy before do_build after do_install

PACKAGES += "${PN}-image"
FILES_${PN}-image += "/boot"

INHIBIT_PACKAGE_STRIP = "1"

COMPATIBLE_MACHINE = "(qoriq-arm64)"
PACKAGE_ARCH = "${MACHINE_ARCH}"
