LICENSE = "NXP-Binary-EULA"
LIC_FILES_CHKSUM = "file://NXP-Binary-EULA.txt;md5=92723670f432558b9e2494ed177d2a85"

SRC_URI = "git://github.com/nxp/qoriq-engine-pfe-bin.git;protocol=https;nobranch=1"
SRCREV = "848002fe4e0e3efb8d13a813e5c123bbc87aa95b"
do_install () {
    install -d ${D}/lib/firmware
    install -d ${D}/boot/engine-pfe-bin
    install -m 644 ${S}/NXP-Binary-EULA.txt ${D}/lib/firmware
    install -m 755 ${S}/ls1012a/slow_path/*.elf ${D}/lib/firmware
    install -m 755 ${S}/ls1012a/u-boot/* ${D}/boot/engine-pfe-bin
}
