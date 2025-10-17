FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
ERROR_QA:remove = "buildpaths"
CFLAGS += "-Wno-error=implicit-function-declaration"
SRC_URI += "file://0001-util.c-Fix-cryptodev-build-failure.patch"
