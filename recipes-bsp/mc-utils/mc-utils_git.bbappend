SRCREV= "8e0b863693fc2ccbc62a62c79b4e3db6da88c16e"

MC_CFG_lx2162a = "lx2162aqds"

do_install_lx2162a () {
	oe_runmake -C config

	install -d ${D}/boot/mc-utils
	cp -r ${S}/config/${MC_CFG}/*.dtb ${D}/boot/mc-utils
        if [ -d ${S}/config/${MC_CFG}/QDS/custom/ ]; then
            install -d ${D}/boot/mc-utils/custom
            cp -r ${S}/config/${MC_CFG}/QDS/custom/*.dtb ${D}/boot/mc-utils/custom
        fi
}

do_deploy_lx2162a () {
	install -d ${DEPLOYDIR}/mc-utils
	cp -r ${S}/config/${MC_CFG}/*.dtb ${DEPLOYDIR}/mc-utils
        if [ -d ${S}/config/${MC_CFG}/QDS/custom/ ]; then
            install -d ${DEPLOYDIR}/mc-utils/custom
            cp -r ${S}/config/${MC_CFG}/QDS/custom/*.dtb ${DEPLOYDIR}/mc-utils/custom
        fi
}
