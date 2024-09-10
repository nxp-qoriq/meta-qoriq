# Copyright (C) 2015 Freescale Semiconductor
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Freescale Package group for audio tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

PACKAGES = "${PN}"

PULSEAUDIO_X11_INSTALL = "${@bb.utils.contains('DISTRO_FEATURES','x11', \
    'pulseaudio-module-x11-xsmp \
     pulseaudio-module-x11-publish \
     pulseaudio-module-x11-cork-request \
     pulseaudio-module-x11-bell \
     consolekit', \
    '', d)}"

PULSEAUDIO_INSTALL = "${@bb.utils.contains('DISTRO_FEATURES', 'pulseaudio',  \
    'pulseaudio-server \
     pulseaudio-module-cli \
     pulseaudio-misc \
     pulseaudio-module-device-manager \
     ${PULSEAUDIO_X11_INSTALL}', \
    '', d)}"

ALSA_INSTALL = "${@bb.utils.contains('DISTRO_FEATURES', 'alsa',  \
    'alsa-utils \
     alsa-tools', \
    '', d)}"

RDEPENDS:${PN} = " \
    ${ALSA_INSTALL} \
    ${PULSEAUDIO_INSTALL} \
"
