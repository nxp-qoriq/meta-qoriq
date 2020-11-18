# Copyright (C) 2020 NXP

require recipes-security/optee/optee-test.nxp.inc

DEPENDS_append = " optee-os-qoriq optee-client-qoriq" 
SRCBRANCH = "imx_3.10.y"
SRCREV = "0c998f42a3fb87b9f2929955cf4b0116cc515091"

TA_DEV_KIT_DIR_aarch64 = "${STAGING_INCDIR}/optee/export-user_ta"

do_compile_prepend () {
	export CFLAGS="${CFLAGS} --sysroot=${STAGING_DIR_HOST}"
}

COMPATIBLE_MACHINE = "(qoriq-arm64)"
