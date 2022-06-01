SUMMARY = "DP firmware"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://COPYING;md5=d0122773a9d62bd492c87ffaf42463b5"

inherit deploy fsl-eula-unpack

SRC_URI = "${FSL_MIRROR}/firmware-imx-8.16.bin;fsl-eula=true"

SRC_URI[md5sum] = "ba7ea200f5c9c877781c657477b163c7"
SRC_URI[sha256sum] = "b2925c4221147e4666136577eee545ace6cc159a59d8123ee2da68b36ae5280e"

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

