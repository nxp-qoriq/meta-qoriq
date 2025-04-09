# SUMMARY = "utility for security boot"

CST_QORIQ_SRC ?= "git://github.com/nxp-qoriq/cst;protocol=https"
SRC_URI = "${CST_QORIQ_SRC};nobranch=1"

SRCREV = "892d2ed3207d78a3cb5533eeb91bcc73967e3e36"

INSANE_SKIP:nativesdk-qoriq-cst-dbg += " buildpaths"
INSANE_SKIP:${PN}-dbg += " buildpaths"
