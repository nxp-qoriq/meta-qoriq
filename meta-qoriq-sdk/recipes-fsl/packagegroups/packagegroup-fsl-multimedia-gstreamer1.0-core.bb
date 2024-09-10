# Copyright (C) 2015 Freescale Semiconductor
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Package group used by freescale to provide set of packages for GStreamer1.x Plugins"
SUMMARY = "Freescale Package group for multimedia Gstreamer1.x Plugins"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

MACHINE_GSTREAMER_1_0_EXTRA_INSTALL ?= ""
MACHINE_GSTREAMER_1_0_PLUGIN ?= ""

RDEPENDS:${PN} += " \
    gstreamer1.0-plugins-good-meta \
    gstreamer1.0-plugins-base-meta \
    ${MACHINE_GSTREAMER_1_0_EXTRA_INSTALL} \
    ${MACHINE_GSTREAMER_1_0_PLUGIN} \
"

# Open source plugins specific to i.MX
IMX_GSTREAMER_1_0_EXTRA_INSTALL = " \
    gstreamer1.0-plugins-bad-meta \
    gstreamer1.0-rtsp-server \
"

MACHINE_GSTREAMER_1_0_EXTRA_INSTALL:mx6 = "${IMX_GSTREAMER_1_0_EXTRA_INSTALL}"
MACHINE_GSTREAMER_1_0_EXTRA_INSTALL:mx7 = "${IMX_GSTREAMER_1_0_EXTRA_INSTALL}"
