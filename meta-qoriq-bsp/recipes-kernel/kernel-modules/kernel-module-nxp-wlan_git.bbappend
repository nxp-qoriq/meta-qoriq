# Use latest NXP Wi-Fi kernel module
SRC_URI = "${MRVL_SRC};branch=${SRCBRANCH}"
SRCBRANCH = "lf-6.18.20_2.0.0"
SRCREV = "2e481212d262758cbd4d0fc7ea95a2ad5f704bc3"
ERROR_QA:remove = "buildpaths"
COMPATIBLE_MACHINE = "(imx-nxp-bsp|qoriq)"
