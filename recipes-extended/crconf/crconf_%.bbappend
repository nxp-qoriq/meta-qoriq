FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI = "git://git.code.sf.net/p/crconf/code;protocol=https;nobranch=1 \
    file://0001-Update-dependencies-Linux-kernel-headers-and-libnetl.patch \
    file://0002-fix-Wstringop-truncation-warning.patch \
"

SRCREV = "8bd996400d087028ba56b724abc1f5b378eaa77f"
