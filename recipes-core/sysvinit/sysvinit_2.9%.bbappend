do_install_append_qoriq () {
	echo 'BOOTLOGD_ENABLE=no' >>${D}${sysconfdir}/default/bootlogd
}

