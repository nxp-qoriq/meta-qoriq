# Use latest NXP Wi-Fi kernel module
SRC_URI = "${MRVL_SRC};branch=${SRCBRANCH}"
SRCBRANCH = "lf-6.18.37_2.1.0"
SRCREV = "${AUTOREV}"
ERROR_QA:remove = "buildpaths"
COMPATIBLE_MACHINE = "(imx-nxp-bsp|qoriq)"
