FILESEXTRAPATHS:prepend:qoriq := "${THISDIR}/${BPN}:"

SRC_URI:append:qoriq = " file://console.sh"

do_install:append:qoriq () {
	install -m 0755 ${WORKDIR}/console.sh ${D}${sysconfdir}/init.d
	update-rc.d -r ${D} console.sh start 80 2 3 4 5 .
}

