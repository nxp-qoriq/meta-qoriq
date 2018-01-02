LICENSE = "Freescale-Binary-EULA"
LIC_FILES_CHKSUM = "file://NXP-Binary-EULA.txt;md5=92723670f432558b9e2494ed177d2a85"

SRC_URI = "git://github.com/nxp/qoriq-engine-pfe-bin.git;protocol=https;nobranch=1"
SRCREV = "848002fe4e0e3efb8d13a813e5c123bbc87aa95b"
do_install_prepend () {
    cp ${S}/NXP-Binary-EULA.txt ${S}/Freescale-Binary-EULA
}

