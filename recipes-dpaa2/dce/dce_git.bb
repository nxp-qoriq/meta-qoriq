DESCRIPTION = "Decompression Compression Engine Userspace Utils"
SECTION = "dpaa2"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3bb80dec5c1b94d99a1422cbfd96192c"

SRC_URI = "git://source.codeaurora.org/external/qoriq/qoriq-components/dce;nobranch=1 \
      git://source.codeaurora.org/external/qoriq/qoriq-components/qbman_userspace;nobranch=1;name=qbman;destsuffix=git/lib/qbman_userspace \
"
SRCREV = "8d6dd3286d93f2289e2467401b900378ade24a0f"
SRCREV_qbman = "75ff61a7ca6acdbdbb780161b053cbcbc990f1be"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = 'CROSS_COMPILE=${TARGET_PREFIX} CC="${TARGET_PREFIX}gcc ${TOOLCHAIN_OPTIONS}"'

do_install () {
    oe_runmake install DESTDIR=${D}
}

INSANE_SKIP_${PN} = "ldflags"
COMPATIBLE_MACHINE = "(qoriq-arm64)"
