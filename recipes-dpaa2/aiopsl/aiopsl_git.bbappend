do_install () {
    install -d ${D}/usr/aiop/bin
    install -d ${D}/usr/aiop/
    cp -rf ${S}/demos/images/*  ${D}/usr/aiop/bin
    cp -rf ${S}/misc/setup/scripts ${D}/usr/aiop/
    cp -rf ${S}/misc/setup/traffic_files/ ${D}/usr/aiop/
}
