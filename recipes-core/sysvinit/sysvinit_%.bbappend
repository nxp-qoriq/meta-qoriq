do_install:append:qoriq () {
	echo 'BOOTLOGD_ENABLE=no' >>${D}${sysconfdir}/default/bootlogd
}

