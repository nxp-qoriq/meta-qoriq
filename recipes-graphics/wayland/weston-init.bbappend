FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:ls1028a = "\
    file://profile \
"

do_install:append:ls1028a() {
    install -Dm0755 ${WORKDIR}/profile ${D}${sysconfdir}/profile.d/weston.sh

    #FIXME: set user root instead of user weston to start weston
    if [ "${VIRTUAL-RUNTIME_init_manager}" != "systemd" ]; then
        sed -i 's,WESTON_USER=weston,WESTON_USER=root,g' ${D}/${sysconfdir}/init.d/weston
    fi
}
