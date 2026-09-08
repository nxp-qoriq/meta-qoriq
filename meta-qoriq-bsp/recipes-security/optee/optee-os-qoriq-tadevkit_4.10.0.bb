# Copyright 2022-2026 NXP
SUMMARY = "OP-TEE Trusted OS TA devkit"
DESCRIPTION = "OP-TEE TA devkit for build TAs"
HOMEPAGE = "https://www.op-tee.org/"


require recipes-security/optee-qoriq/optee-os.nxp.inc
DEPENDS += "python3-pycryptodome-native"

OPTEE_OS_BRANCH = "lf-6.18.37_2.1.0"
SRCREV = "99f40897dd1fc28b4c792773b167e3db629db2ff"
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

