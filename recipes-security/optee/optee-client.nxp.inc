# Copyright 2020-2021 NXP

SUMMARY = "OPTEE Client libs"
HOMEPAGE = "http://www.optee.org/"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=69663ab153298557a59c67a60a743e5b"

inherit python3native systemd

OPTEE_CLIENT_SRC ?= "git://source.codeaurora.org/external/qoriq/qoriq-components/optee_client"
SRC_URI = "${OPTEE_CLIENT_SRC};nobranch=1"
SRCREV = "2a77cf88d956c34cb4a1c191bea6113e327f5fe0"

FILESEXTRAPATHS_prepend := "${THISDIR}/optee-client:"
SRC_URI += "file://tee-supplicant.service"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

OPTEE_ARCH ?= "arm32"
OPTEE_ARCH_armv7a = "arm32"
OPTEE_ARCH_aarch64 = "arm64"

EXTRA_OEMAKE = "ARCH=${OPTEE_ARCH} O=${B}"

do_install () {
	oe_runmake -C ${S} install

	install -d ${D}${libdir}/
	install -p -m0644 ${B}/export${libdir}/libteec.so.1.0.0 ${D}${libdir}/
	ln -sf libteec.so.1.0.0 ${D}${libdir}/libteec.so.1.0
	ln -sf libteec.so.1.0.0 ${D}${libdir}/libteec.so.1
	ln -sf libteec.so.1 ${D}${libdir}/libteec.so

	install -d ${D}${bindir}/
	install -m0755 ${B}/export/usr/sbin/tee-supplicant ${D}${bindir}/

	cp -a ${B}/export/usr/include ${D}/usr/

	install -d ${D}${systemd_system_unitdir}/
	install -m0644 ${WORKDIR}/tee-supplicant.service ${D}${systemd_system_unitdir}/
	sed -i -e s:/etc:${sysconfdir}:g -e s:/usr/bin:${bindir}:g ${D}${systemd_system_unitdir}/tee-supplicant.service
}

SYSTEMD_SERVICE_${PN} = "tee-supplicant.service"

FILES_${PN} += "${libdir}/* ${includedir}/*"

INSANE_SKIP_${PN} = "ldflags dev-elf"
INSANE_SKIP_${PN}-dev = "ldflags dev-elf"
