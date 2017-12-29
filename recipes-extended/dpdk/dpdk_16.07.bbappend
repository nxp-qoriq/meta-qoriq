SRCREV = "076aa8e2f9a4ad7e0a020f5c574371d92afe4a60"

do_install_append () {
    install -d ${D}/${bindir}/dpdk-example/extras
    cp -rf  ${S}/nxp/* ${D}/${bindir}/dpdk-example/extras/
}
