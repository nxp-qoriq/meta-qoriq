# Copyright 2020-2022 NXP

SUMMARY = "OPTEE Client libs"
HOMEPAGE = "http://www.optee.org/"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=69663ab153298557a59c67a60a743e5b"

inherit update-rc.d python3native systemd pkgconfig

OPTEE_CLIENT_SRC ?= "git://github.com/nxp-qoriq/optee_client.git;protocol=https"
SRC_URI = "${OPTEE_CLIENT_SRC};branch=${OPTEE_CLIENT_BRANCH}"
SRCREV = "9d8f1903bbea3a1e631c8d26ee51c37020569312"

FILESEXTRAPATHS:prepend := "${THISDIR}/optee-client:"
SRC_URI += "file://tee-supplicant.service"
SRC_URI += "file://tee-supplicant.init"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

DEPENDS = "util-linux-libuuid"

OPTEE_ARCH:arm = "arm32"
OPTEE_ARCH:aarch64 = "arm64"

EXTRA_OEMAKE = " \
	-C ${S} O=${B} \
"

do_install () {
	oe_runmake install

	install -d ${D}${libdir}/
	install -D -p -m0644 ${B}/export/usr/lib/libteec.so.1.0.0 ${D}${libdir}/libteec.so.1.0.0
	ln -sf libteec.so.1.0.0 ${D}${libdir}/libteec.so.1
	ln -sf libteec.so.1.0.0 ${D}${libdir}/libteec.so

	install -D -p -m0644 ${B}/export/usr/lib/libckteec.so.0.1.0 ${D}${libdir}/libckteec.so.0.1.0
	ln -sf libckteec.so.0.1.0 ${D}${libdir}/libckteec.so.0
	ln -sf libckteec.so.0.1.0 ${D}${libdir}/libckteec.so

	install -D -p -m0755 ${B}/export/usr/sbin/tee-supplicant ${D}${bindir}/tee-supplicant

	cp -a ${B}/export/usr/include ${D}${includedir}

	if [ "${VIRTUAL-RUNTIME_init_manager}" == "systemd" ]; then
		install -d ${D}${systemd_system_unitdir}/
		install -m0644 ${WORKDIR}/tee-supplicant.service ${D}${systemd_system_unitdir}/
		sed -i -e s:/etc:${sysconfdir}:g -e s:/usr/bin:${bindir}:g ${D}${systemd_system_unitdir}/tee-supplicant.service
	else
		install -d ${D}${sysconfdir}/init.d/
		install -m 0755 ${WORKDIR}/tee-supplicant.init ${D}${sysconfdir}/init.d/
	fi
}

SYSTEMD_SERVICE:${PN} = "tee-supplicant.service"
INITSCRIPT_NAME = "tee-supplicant.init"
INITSCRIPT_PARAMS = "start 80 2 3 4 5 ."

FILES:${PN} += "${libdir}/* ${includedir}/*"

INSANE_SKIP:${PN} = "ldflags dev-elf"
INSANE_SKIP:${PN}-dev = "ldflags dev-elf"
