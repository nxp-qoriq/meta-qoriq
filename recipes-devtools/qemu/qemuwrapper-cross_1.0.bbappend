do_install () {
	install -d ${D}${bindir_crossscripts}/

	qemu_binary=${@qemu_target_binary(d)}

        if [ ${PACKAGE_ARCH} = "ppce500v2" ]; then
            qemu_options='-r ${OLDEST_KERNEL} -cpu e500mc -E LD_LIBRARY_PATH=$D${libdir}:$D${base_libdir}'
        else
	    qemu_options='${QEMU_OPTIONS} -E LD_LIBRARY_PATH=$D${libdir}:$D${base_libdir}'
        fi
	cat >> ${D}${bindir_crossscripts}/${MLPREFIX}qemuwrapper << EOF
#!/bin/sh
set -x

if [ ${@bb.utils.contains('MACHINE_FEATURES', 'qemu-usermode', 'True', 'False', d)} = False ]; then
	echo "qemuwrapper: qemu usermode is not supported"
	exit 1
fi


$qemu_binary $qemu_options "\$@"
EOF

	chmod +x ${D}${bindir_crossscripts}/${MLPREFIX}qemuwrapper
}
