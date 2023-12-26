DESCRIPTION = "mTCP on DPDK"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=189d0d4d51a8c303a7188b0932b735a9"

inherit autotools pkgconfig

PV = "21.11"

DEPENDS += "gmp numactl dpdk bc-native"

SRC_URI = "gitsm://github.com/nxpmicro/mtcp;protocol=https;branch=mtcp-dpdk"
SRCREV = "82cf300c4de96045f8b5b9ae3cd2fcabfe7c2aef"

S = "${WORKDIR}/git"
B = "${WORKDIR}/git"

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
}

COMPATIBLE_MACHINE = "(qoriq-arm64)"
