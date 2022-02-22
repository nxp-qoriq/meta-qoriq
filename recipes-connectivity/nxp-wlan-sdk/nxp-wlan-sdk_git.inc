SUMMARY = "NXP Wi-Fi SDK for 88w8997 and 88w8987"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://gpl-2.0.txt;md5=ab04ac0f249af12befccb94447c08b77"

SRCBRANCH = "master"
MRVL_SRC ?= "git://bitbucket.sw.nxp.com/imx/mwifiex.git;protocol=ssh"
SRC_URI = "${MRVL_SRC};branch=${SRCBRANCH}"
SRCREV = "35da8282828814d3e606c82af24c536968d340e5"

S = "${WORKDIR}/git/mxm_wifiex/wlan_src"

EXTRA_OEMAKE = "KERNELDIR=${STAGING_KERNEL_BUILDDIR}"

RRECOMMENDS:${PN} = "wireless-tools"
