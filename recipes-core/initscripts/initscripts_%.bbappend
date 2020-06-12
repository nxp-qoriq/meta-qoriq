FILESEXTRAPATHS_prepend_qoriq := "${THISDIR}/${BPN}:"

SRC_URI_append_qoriq = " file://console.sh"

do_install_append_qoriq () {
	install -m 0755 ${WORKDIR}/console.sh ${D}${sysconfdir}/init.d
	update-rc.d -r ${D} console.sh start 80 2 3 4 5 .
}

