do_install_append() {
    cp -rf  ${S}/ovsdb/ovsdb-client ${D}${bindir}/ovs-dpdk
}

