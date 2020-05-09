SRCREV= "8672a5f5abcd3a354dcab07e03f2a8a69b2e962d"

MC_CFG_lx2162a = "lx2160a"

do_install_lx2162a () {
	oe_runmake -C config

	install -d ${D}/boot/mc-utils
	cp -r ${S}/config/${MC_CFG}/QDS/*.dtb ${D}/boot/mc-utils
        if [ -d ${S}/config/${MC_CFG}/QDS/custom/ ]; then
            install -d ${D}/boot/mc-utils/custom
            cp -r ${S}/config/${MC_CFG}/QDS/custom/*.dtb ${D}/boot/mc-utils/custom
        fi
}

do_deploy_lx2162a () {
	install -d ${DEPLOYDIR}/mc-utils
	cp -r ${S}/config/${MC_CFG}/QDS/*.dtb ${DEPLOYDIR}/mc-utils
        if [ -d ${S}/config/${MC_CFG}/QDS/custom/ ]; then
            install -d ${DEPLOYDIR}/mc-utils/custom
            cp -r ${S}/config/${MC_CFG}/QDS/custom/*.dtb ${DEPLOYDIR}/mc-utils/custom
        fi
}
