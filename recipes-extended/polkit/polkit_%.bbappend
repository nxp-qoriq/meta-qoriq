SRC_URI = "git://gitlab.freedesktop.org/polkit/polkit.git;protocol=https;branch=master"

do_install:append() {
    rm -rf ${D}/${datadir}/polkit-1/rules.d
}

