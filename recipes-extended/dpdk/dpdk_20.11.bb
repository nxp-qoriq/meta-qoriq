include dpdk.inc

SRC_URI += " \
            file://0001-meson.build-march-and-mcpu-already-passed-by-Yocto.patch \
            file://0001-ifpga-meson-Fix-finding-librt-using-find_library.patch \
"

MESON_BUILDTYPE = "release"

# kernel module is provide by dpdk-module recipe, so disable here
EXTRA_OEMESON = " -Denable_kmods=false \
                -Dexamples="l2fwd,l3fwd,l2fwd-qdma,ip_fragmentation,ip_reassembly,qdma_demo,ethtool,link_status_interrupt,multi_process/symmetric_mp,multi_process/simple_mp,multi_process/symmetric_mp_qdma,kni,ipsec-secgw,qos_sched,multi_process/client_server_mp/mp_server,multi_process/client_server_mp/mp_client,l3fwd-power,l2fwd-event,l2fwd-crypto,bond" \
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

do_configure() {
    sed -i "/implementor_/d" ${WORKDIR}/meson.cross
    sed -i "/\[properties]/aimplementor_id = 'dpaa'" ${WORKDIR}/meson.cross
    sed -i "/\[properties]/aimplementor_pn = 'default'" ${WORKDIR}/meson.cross
    sed -i "s/cpu =.*/cpu = 'armv8-a'/" ${WORKDIR}/meson.cross
    
    # Meson requires this to be 'bfd, 'lld' or 'gold' from 0.53 onwards
    # https://github.com/mesonbuild/meson/commit/ef9aeb188ea2bc7353e59916c18901cde90fa2b3
    unset LD

    # Work around "Meson fails if /tmp is mounted with noexec #2972"
    mkdir -p "${B}/meson-private/tmp"
    export TMPDIR="${B}/meson-private/tmp"
    bbnote Executing meson ${MESONOPTS} ${MESON_CROSS_FILE} ${EXTRA_OEMESON} "${B}" "${S}"
    if ! meson ${MESONOPTS} ${MESON_CROSS_FILE} ${EXTRA_OEMESON} "${B}" "${S}"; then
        bbfatal_log meson failed
    fi
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

PACKAGES =+ "${PN}-examples ${PN}-tools"

FILES_${PN}-examples = " \
    ${INSTALL_PATH}/examples/* \
	"

FILES_${PN}-tools = " \
    ${bindir}/dpdk-pdump \
    ${bindir}/dpdk-test \
    ${bindir}/dpdk-test-* \
    ${bindir}/dpdk-*.py \
    "

INSANE_SKIP_${PN} = "dev-so"
