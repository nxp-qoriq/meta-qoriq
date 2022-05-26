SUMMARY = "DP firmware"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://COPYING;md5=be5ff43682ed6c57dfcbeb97651c2829"

inherit deploy fsl-eula-unpack

SRC_URI = "${FSL_MIRROR}/firmware-imx-8.16.bin;fsl-eula=true"

SRC_URI[md5sum] = "174da3080bec0d6ae9a9eae72af0c9b7"
SRC_URI[sha256sum] = "b3abc9397b4f99d2547e460c65eeba1f7d3677ee8841881d29aa0897a7708614"

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

