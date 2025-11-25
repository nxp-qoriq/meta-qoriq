# Copyright 2022-2025 NXP
SUMMARY = "OP-TEE Trusted OS TA devkit"
DESCRIPTION = "OP-TEE TA devkit for build TAs"
HOMEPAGE = "https://www.op-tee.org/"


require recipes-security/optee-qoriq/optee-os.nxp.inc
DEPENDS += "python3-pycryptodome-native"

OPTEE_OS_BRANCH = "lf-6.12.49_2.2.0"
SRCREV = "771a7ca0494110b5eee1229cc175c755ab8c7e00"
PV:append = "+git"

SRC_URI:remove = " file://0007-allow-setting-sysroot-for-clang.patch"

do_install() {
    #install TA devkit
    install -d ${D}${includedir}/optee/export-user_ta/
    for f in ${B}/export-ta_${OPTEE_ARCH}/* ; do
        cp -aR $f ${D}${includedir}/optee/export-user_ta/
    done
}

do_deploy() {
	echo "Do not inherit do_deploy from optee-os."
}

FILES:${PN} = "${includedir}/optee/"

