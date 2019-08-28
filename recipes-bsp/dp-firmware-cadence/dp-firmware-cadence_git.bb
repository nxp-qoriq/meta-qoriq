SUMMARY = "DP firmware"
LICENSE = "NXP-Binary-EULA"
LIC_FILES_CHKSUM = "file://EULA.txt;md5=86d76166990962fa552f840ff08e5798"

inherit deploy

SRC_URI = "git://bitbucket.sw.nxp.com/dash/firmware-cadence;protocol=ssh;nobranch=1"
SRCREV = "98d51b8a9edf74b09b2977f206107a742c32b116"

S = "${WORKDIR}/git"

do_install () {
    install -d ${D}/boot
    cp -fr ${S}/dp/*.bin ${D}/boot
}

do_deploy () {
    install -d ${DEPLOYDIR}/dp
    cp -fr ${S}/dp/*.bin ${DEPLOYDIR}/dp
}
addtask deploy before do_build after do_install

PACKAGES += "${PN}-image"
FILES_${PN}-image += "/boot"

COMPATIBLE_MACHINE = "(qoriq-arm64)"
PACKAGE_ARCH = "${MACHINE_ARCH}"

