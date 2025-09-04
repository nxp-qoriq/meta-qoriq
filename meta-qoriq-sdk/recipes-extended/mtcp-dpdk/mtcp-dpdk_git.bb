DESCRIPTION = "mTCP on DPDK"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6a36820ca4ad07a1d62df52c596df642"

inherit autotools pkgconfig

PV = "22.11"

DEPENDS += "gmp numactl dpdk bc-native zlib"

SRC_URI = "gitsm://github.com/nxpmicro/mtcp;protocol=https;branch=mtcp-dpdk"
SRCREV = "a62034212be0a41e9eeb83efa75d77226aa7cd05"

S = "${UNPACKDIR}/git"
B = "${UNPACKDIR}/git"

export RTE_SDK = "${RECIPE_SYSROOT}/usr"
EXTRA_OECONF += "--with-dpdk-lib=$RTE_SDK"

do_configure() {
    export SYSROOT_DPDK=${PKG_CONFIG_SYSROOT_DIR}
    ${S}/configure --host aarch64-fsl-linux --with-dpdk-lib=${SYSROOT_DPDK}/usr
}

do_compile() {
    make setup-dpdk
    make
    make -C apps/perf
}

do_install() {
    install -d ${D}${bindir}/mtcp
    install -m 0755 ${S}/apps/perf/*.sh  ${D}${bindir}/mtcp
    install -m 0755 ${S}/apps/perf/*.py  ${D}${bindir}/mtcp
    install -m 0755 ${S}/apps/perf/client  ${D}${bindir}/mtcp
    install -m 0644 ${S}/apps/perf/client.conf  ${D}${bindir}/mtcp
    install -m 0644 ${S}/apps/perf/README.md    ${D}${bindir}/mtcp
    install -m 0755 ${S}/apps/example/epserver ${D}${bindir}/mtcp
    install -m 0755 ${S}/apps/example/epwget   ${D}${bindir}/mtcp
    install -m 0644 ${S}/config/sample_route.conf   ${D}${bindir}/mtcp
    install -m 0644 ${S}/config/sample_arp.conf     ${D}${bindir}/mtcp
    install -m 0644 ${S}/apps/example/epserver.conf ${D}${bindir}/mtcp
    install -m 0644 ${S}/apps/example/epwget.conf   ${D}${bindir}/mtcp
}

COMPATIBLE_MACHINE = "(qoriq-arm64)"
