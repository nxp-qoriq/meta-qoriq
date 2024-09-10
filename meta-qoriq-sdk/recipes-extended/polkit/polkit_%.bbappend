do_install:append() {
    rm -rf ${D}/${datadir}/polkit-1/rules.d
}

