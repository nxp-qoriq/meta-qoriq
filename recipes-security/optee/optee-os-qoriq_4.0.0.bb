# Copyright 2022-2023 NXP
require optee-os-qoriq.inc

OPTEE_OS_BRANCH = "lf-6.1.55_2.2.0"
SRCREV = "a303fc80f7c4bd713315687a1fa1d6ed136e78ee"

PV:append = "+git${SRCPV}"

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
