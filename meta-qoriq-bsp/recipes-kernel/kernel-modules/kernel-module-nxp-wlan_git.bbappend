# Use latest NXP Wi-Fi kernel module
SRC_URI = "${MRVL_SRC};branch=${SRCBRANCH}"
SRCBRANCH = "master"
SRCREV = "${AUTOREV}"
ERROR_QA:remove = "buildpaths"
COMPATIBLE_MACHINE = "(imx-nxp-bsp|qoriq)"
