# Copyright 2022-2026 NXP

require recipes-security/optee-qoriq/optee-os.nxp.inc

OPTEE_OS_BRANCH = "lf-6.18.20_2.0.0"
SRCREV = "${AUTOREV}"
PV:append = "+git"

SRC_URI:remove = " file://0007-allow-setting-sysroot-for-clang.patch"

do_install:append () {
	install -d ${D}${nonarch_base_libdir}/firmware/
	install -m 644 ${B}/core/*.bin ${D}${nonarch_base_libdir}/firmware/
	install -m 644 ${B}/core/tee-raw.bin ${D}${nonarch_base_libdir}/firmware/tee_${MACHINE}.bin

	# Install embedded TAs
	install -d ${D}${base_libdir}/optee_armtz/
	install -m 444 ${B}/ta/*/*.ta ${D}${base_libdir}/optee_armtz/
}

do_deploy:append () {
	install -d ${DEPLOYDIR}/optee
	install -m 644 ${D}${nonarch_base_libdir}/firmware/* ${DEPLOYDIR}/optee/
}

FILES:${PN} = "${nonarch_base_libdir}/optee_armtz/ ${nonarch_base_libdir}/firmware/"
