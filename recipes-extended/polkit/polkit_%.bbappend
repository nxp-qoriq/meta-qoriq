SRC_URI = "git://gitlab.freedesktop.org/polkit/polkit.git;protocol=https;branch=master"

do_install:append() {
	rmdir --ignore-fail-on-non-empty ${D}/${datadir}/polkit-1/rules.d
}

