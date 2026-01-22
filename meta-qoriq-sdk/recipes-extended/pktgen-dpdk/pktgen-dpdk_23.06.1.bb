DESCRIPTION = "PKTGEN DPDK"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a7da455bc8eefb5be137b63fce4c9a46"

DEPENDS += "libpcap dpdk lua lua-native numactl"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = "git://github.com/pktgen/Pktgen-DPDK.git;protocol=https;nobranch=1 \
           file://0001-pktgen-dpdk-fix-build-issue-in-cross-compilation-env.patch"

SRCREV = "1e93fa88916b8f2c27b612d761a03cbf03d046de"

S = "${UNPACKDIR}/git"

DPAA_VER ?= "dpaa"
export RTE_TARGET = "arm64-${DPAA_VER}-linuxapp-gcc"
export RTE_SDK = "${RECIPE_SYSROOT}/usr/share/dpdk"

inherit meson pkgconfig

MESON_BUILDTYPE = "release"

EXTRA_OEMESON += '-Dc_args="-DRTE_FORCE_INTRINSICS"'
EXTRA_OEMESON += " -Dwerror=false"

do_configure:prepend() {
    sed -i "/^add_project_arguments('-march=native'/s/^/#&/" ${S}/meson.build
}

do_install() {
    install -d ${D}${bindir}/
    install -m 0755 app/pktgen ${D}${bindir}/
    install -m 0644 ${S}/Pktgen.lua ${D}${bindir}/
}

INSANE_SKIP:${PN} = "ldflags"
INHIBIT_PACKAGE_STRIP = "1"
PACKAGE_ARCH = "${MACHINE_ARCH}"
PARALLEL_MAKE = ""
COMPATIBLE_MACHINE = "(qoriq-arm64)"

CFLAGS:remove = "-march=native"
