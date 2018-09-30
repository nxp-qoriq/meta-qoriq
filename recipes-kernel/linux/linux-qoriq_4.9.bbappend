FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI = "git://source.codeaurora.org/external/qoriq/qoriq-components/linux;nobranch=1 \
    file://0001-Makefile-fix-gcc-8-build-error.patch \
"

SRCREV = "e8b01fb24fb8eb1adee9667eba2cae702b5892e9"
