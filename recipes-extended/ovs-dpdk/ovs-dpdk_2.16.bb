DESCRIPTION = "OVS DPDK"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ce5d23a6429dff345518758f13aaeab"

DEPENDS = "dpdk python3-six-native coreutils-native autoconf-native automake-native"
RDEPENDS:${PN} = "bash libcrypto libssl python3"

inherit python3native pkgconfig

SRC_URI = "git://bitbucket.sw.nxp.com/dqns/ovs-dpdk.git;protocol=ssh;nobranch=1"
SRCREV = "ad97b737bd978f8fe08f10f4abf38dc68d3134e0"

S = "${WORKDIR}/git"

do_configure() {
    export SYSROOT_DPDK=${PKG_CONFIG_SYSROOT_DIR}
    ${S}/boot.sh
    ${S}/configure --host aarch64-fsl-linux --with-dpdk=static --with-openssl=${SYSROOT_DPDK}/usr CFLAGS="-g -Wno-cast-align -Ofast" 
}

do_install:append() {
    install -d ${D}${bindir}/ovs-dpdk
    cp -rf  ${S}/ovsdb/ovsdb-tool ${D}${bindir}/ovs-dpdk
    cp -rf  ${S}/ovsdb/ovsdb-server ${D}${bindir}/ovs-dpdk
    cp -rf  ${S}/ovsdb/ovsdb-client ${D}${bindir}/ovs-dpdk
    cp -rf  ${S}/vswitchd/vswitch.ovsschema ${D}${bindir}/ovs-dpdk
    cp -rf  ${S}/vswitchd/ovs-vswitchd ${D}${bindir}/ovs-dpdk
    cp -rf  ${S}/utilities/ovs-vsctl ${D}${bindir}/ovs-dpdk
    cp -rf  ${S}/utilities/ovs-ofctl ${D}${bindir}/ovs-dpdk
    chmod 777 -R ${D}${bindir}/ovs-dpdk/*
}

ALLOW_EMPTY:${PN} = "1"
INHIBIT_PACKAGE_STRIP = "1"
PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(qoriq-arm64)"
