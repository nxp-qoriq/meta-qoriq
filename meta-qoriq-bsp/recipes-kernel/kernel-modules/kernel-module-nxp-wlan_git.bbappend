# Use latest NXP Wi-Fi kernel module
SRC_URI = "${MRVL_SRC};branch=${SRCBRANCH}"
SRCBRANCH = "lf-6.12.49_2.2.0"
SRCREV = "${AUTOREV}"
ERROR_QA:remove = "buildpaths"
COMPATIBLE_MACHINE = "(imx-nxp-bsp|qoriq)"
