do_install_append() {
	rmdir --ignore-fail-on-non-empty ${D}/${datadir}/polkit-1/rules.d
}

