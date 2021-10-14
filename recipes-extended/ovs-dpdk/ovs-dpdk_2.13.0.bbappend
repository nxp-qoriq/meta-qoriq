do_install:append() {
    cp -rf  ${S}/ovsdb/ovsdb-client ${D}${bindir}/ovs-dpdk
}

