SUMMARY = "DP firmware"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://COPYING;md5=be5ff43682ed6c57dfcbeb97651c2829"

inherit deploy fsl-eula-unpack

SRC_URI = "${FSL_MIRROR}/firmware-imx-8.16.bin;fsl-eula=true"

SRC_URI[md5sum] = "dc715c2d8e3acf2c19a3f271308b044a"
SRC_URI[sha256sum] = "b2e16576462658b7e5f6cf4e1d18b300486a2b6ab27d86ff469cc06ba4caa8aa"

S = "${WORKDIR}/firmware-imx-8.16"

do_install () {
    install -d ${D}/boot
    install -m 0644 ${S}/firmware/hdmi/cadence/dp_ls1028a.bin ${D}/boot/ls1028a-dp-fw.bin
}

do_deploy () {
    install -d ${DEPLOYDIR}/dp
    install -m 0644 ${S}/firmware/hdmi/cadence/dp_ls1028a.bin ${DEPLOYDIR}/dp/ls1028a-dp-fw.bin
}
addtask deploy before do_build after do_install

PACKAGES += "${PN}-image"
FILES:${PN}-image += "/boot"

COMPATIBLE_MACHINE = "(qoriq-arm64)"
PACKAGE_ARCH = "${MACHINE_ARCH}"

