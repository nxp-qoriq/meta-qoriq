include dpdk.inc

SRC_URI += " \
            file://0001-meson.build-march-and-mcpu-already-passed-by-Yocto.patch \
            file://0001-ifpga-meson-Fix-finding-librt-using-find_library.patch \
            file://0001-drivers-net-enetfec-enet_uio.c-fix-multiple-definiti.patch \
"

MESON_BUILDTYPE = "release"

# kernel module is provide by dpdk-module recipe, so disable here
EXTRA_OEMESON = " -Denable_kmods=false \
                -Dexamples=all \
		-Doptimization=3 \
"

PACKAGECONFIG ??= "openssl"
PACKAGECONFIG[afxdp] = ",,libbpf"
PACKAGECONFIG[libvirt] = ",,libvirt"
PACKAGECONFIG[openssl] = ",,openssl"

RDEPENDS_${PN} += "bash pciutils python3-core"
RDEPENDS_${PN}-examples += "bash"
DEPENDS = "numactl"

inherit meson

INSTALL_PATH = "${prefix}/share/dpdk"

do_configure_prepend() {
    sed -i "/implementor_/d" ${WORKDIR}/meson.cross
    sed -i "/\[properties]/aimplementor_id = 'dpaa'" ${WORKDIR}/meson.cross
    sed -i "/\[properties]/aimplementor_pn = 'default'" ${WORKDIR}/meson.cross
    sed -i "s/cpu =.*/cpu = 'armv8-a'/" ${WORKDIR}/meson.cross
}

do_install_append(){
    # remove source files
    rm -rf ${D}/${INSTALL_PATH}/examples/*

    # Install examples
    install -m 0755 -d ${D}/${INSTALL_PATH}/examples/
    for dirname in ${B}/examples/dpdk-*
    do
        if [ ! -d ${dirname} ] && [ -x ${dirname} ]; then
            install -m 0755 ${dirname} ${D}/${INSTALL_PATH}/examples/
        fi
    done
    cp -rf ${S}/nxp/* ${D}/${INSTALL_PATH}/
}

PACKAGES =+ "${PN}-tools ${PN}-examples ${PN}-misc"

FILES_${PN}-tools = " \
    ${bindir}/dpdk-testpmd \
    ${INSTALL_PATH}/examples/dpdk-l2fwd \
    ${INSTALL_PATH}/examples/dpdk-l2fwd-crypto \
    ${INSTALL_PATH}/examples/dpdk-l3fwd \
    ${INSTALL_PATH}/examples/dpdk-ipsec-secgw \
"

FILES_${PN}-examples = " \
    ${bindir}/dpdk-proc-info \
    ${bindir}/dpdk-test \
    ${bindir}/dpdk-test-crypto-perf \
    ${bindir}/dpdk-*.py \
    ${INSTALL_PATH}/examples/dpdk-cmdif \
    ${INSTALL_PATH}/examples/dpdk-cmdline \
    ${INSTALL_PATH}/examples/dpdk-ethtool \
    ${INSTALL_PATH}/examples/dpdk-ip_fragmentation \
    ${INSTALL_PATH}/examples/dpdk-ip_reassembly \
    ${INSTALL_PATH}/examples/dpdk-kni \
    ${INSTALL_PATH}/examples/dpdk-l2fwd-keepalive \
    ${INSTALL_PATH}/examples/dpdk-l2fwd-qdma \
    ${INSTALL_PATH}/examples/dpdk-l3fwd-acl \
    ${INSTALL_PATH}/examples/dpdk-link_status_interrupt \
    ${INSTALL_PATH}/examples/dpdk-mp_client \
    ${INSTALL_PATH}/examples/dpdk-mp_server \
    ${INSTALL_PATH}/examples/dpdk-qdma_demo \
    ${INSTALL_PATH}/examples/dpdk-simple_mp \
    ${INSTALL_PATH}/examples/dpdk-symmetric_mp \
    ${INSTALL_PATH}/examples/dpdk-symmetric_mp_qdma \
    ${INSTALL_PATH}/examples/dpdk-timer \
"

FILES_${PN}-misc = " \
    ${bindir}/dpdk-pdump \
    ${bindir}/dpdk-test-* \
    ${bindir}/dpdk-*.py \
    ${INSTALL_PATH}/examples/* \
"

INSANE_SKIP_${PN} = "dev-so"
