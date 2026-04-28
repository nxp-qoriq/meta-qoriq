FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

GBM_FORMAT_VALUE = "argb8888"

do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        # Add weston.log back, used by NXP for testing
        update_file "ExecStart=/usr/bin/weston " "ExecStart=/usr/bin/weston --log=\$\{XDG_RUNTIME_DIR\}/weston.log " ${D}${systemd_system_unitdir}/weston.service

        # Run weston as root
        # FIXME: weston should be run as weston, not as root
        update_file "User=weston" "User=root" ${D}${systemd_system_unitdir}/weston.service
        update_file "Group=weston" "Group=root" ${D}${systemd_system_unitdir}/weston.service
    else
        # Run weston as root
        # FIXME: weston should be run as weston, not as root
        update_file "WESTON_USER=weston" "WESTON_USER=root" ${D}/${sysconfdir}/init.d/weston
    fi
}
