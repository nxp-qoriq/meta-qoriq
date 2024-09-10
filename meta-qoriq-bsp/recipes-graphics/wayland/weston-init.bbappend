FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

do_install:append:ls1028a() {
    if [ "${VIRTUAL-RUNTIME_init_manager}" != "systemd" ]; then
        # Install weston-socket.sh for sysvinit as well
        install -D -p -m0644 ${WORKDIR}/weston-socket.sh ${D}${sysconfdir}/profile.d/weston-socket.sh

        # FIXME: weston should be run as weston, not as root
        sed -i 's,WESTON_USER=weston,WESTON_USER=root,g' ${D}/${sysconfdir}/init.d/weston
    fi
}
