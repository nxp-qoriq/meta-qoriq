FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
            file://0001-net-multicast-Remove-mc_gethost.patch \
            file://0001-Temp-fix-for-conflicting-types-for-getdents64.patch \
"
