# Use latest NXP Wi-Fi kernel module
SRC_URI = "${MRVL_SRC};branch=${SRCBRANCH}"
SRCBRANCH = "lf-6.12.20_2.0.0"
SRCREV = "${AUTOREV}"
ERROR_QA:remove = "buildpaths"
COMPATIBLE_MACHINE = "(imx-nxp-bsp|qoriq)"
