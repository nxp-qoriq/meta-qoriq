do_install_append() {
        mkdir -p ${D}/etc/vpp
        cp ${S}/src/vpp/conf/startup.conf ${D}/etc/vpp/startup.conf
}
