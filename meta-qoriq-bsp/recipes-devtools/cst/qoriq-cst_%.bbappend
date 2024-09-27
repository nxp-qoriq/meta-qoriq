# SUMMARY = "utility for security boot"

CST_QORIQ_SRC ?= "git://github.com/nxp-qoriq/cst;protocol=https;nobranch=1"
SRC_URI = "${CST_QORIQ_SRC} \
           file://0001-tools-Mark-struct-input_field-file_field-extern.patch \
"
SRCREV = "e96dead3c339f6addb1600249be67e1884cdbcc5"
