do_install () {
        oe_runmake O=${RTE_OUTPUT} T= install-runtime DESTDIR=${D}
        oe_runmake O=${RTE_OUTPUT} T= install-kmod DESTDIR=${D} kerneldir=${MODULE_DIR}
        oe_runmake O=${RTE_OUTPUT} T= install-sdk DESTDIR=${D}

        # Install examples
        install -d 0644 ${D}/${datadir}/dpdk/cmdif/include
        install -d 0644 ${D}/${datadir}/dpdk/cmdif/lib
        install -d 0644 ${D}/${datadir}/dpdk/examples
        cp examples/cmdif/lib/client/fsl_cmdif_client.h examples/cmdif/lib/server/fsl_cmdif_server.h \
            examples/cmdif/lib/shbp/fsl_shbp.h      ${D}/${datadir}/dpdk/cmdif/include
        cp examples/cmdif/lib/${RTE_TARGET}/librte_cmdif.a ${D}/${datadir}/dpdk/cmdif/lib
        install -d 0644 ${D}/${datadir}/dpdk/examples/ipsec_secgw
        cp -r ${S}/examples/ipsec-secgw/*.cfg  ${D}/${datadir}/dpdk/examples/ipsec_secgw
        cp -rf ${S}/nxp/* ${D}/${datadir}/dpdk
        cp -r ${S}/examples/cmdif/cmdif_demo/${DPDK_RTE_TARGET}/cmdif_demo ${D}/${datadir}/dpdk/examples
        cp -r ${S}/examples/vhost/vhost-switch ${D}/${datadir}/dpdk/examples
        for APP in l2fwd l3fwd l2fwd-qdma l2fwd-crypto ipsec-secgw  kni ip_fragmentation ip_reassembly; do
            cp -r ${S}/examples/${APP}/${APP}  ${D}/${datadir}/dpdk/examples
        done
}

