pkg_postinst_eudev-hwdb () {
}
pkg_postinst_ontarget_eudev-hwdb () {
    if test -n "$D"; then
        if ${@bb.utils.contains('MACHINE_FEATURES', 'qemu-usermode', 'true','false', d)}; then
            ${@qemu_run_binary(d, '$D', '${bindir}/udevadm')} hwdb --update --root $D
            chown root:root $D${sysconfdir}/udev/hwdb.bin
        else
            $INTERCEPT_DIR/postinst_intercept delay_to_first_boot ${PKG} mlprefix=${MLPREFIX}
        fi
    else
        udevadm hwdb --update
    fi
}
