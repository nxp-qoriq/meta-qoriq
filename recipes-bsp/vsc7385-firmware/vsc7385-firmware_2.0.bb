DESCRIPTION = "Firmware image for the VSC7385"
LICENSE = "Vitesse"
LIC_FILES_CHKSUM = "file://Readme_EULA.txt;md5=53894647eb55a1aa9e91fd6d93236b53"

inherit deploy

SRC_URI = "git://git.am.freescale.net/gitolite/sdk/vsc7385-firmware.git"
SRCREV = "a1a6456165046cd9c9a20be77f9160d68da6368a"

S = "${WORKDIR}/git"

do_deploy() {
    install -d ${DEPLOYDIR}/boot
    install ${S}/vsc2bin ${DEPLOYDIR}/boot/vsc-switch-firmware.bin
}
addtask deploy before do_build after do_install

COMPATIBLE_MACHINE = "(p1020rdb|p1021rdb|p2020rdb)"
