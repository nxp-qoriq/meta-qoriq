FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI = "\
    file://71-fsl-dpaa-persistent-networking.rules \
    file://72-fsl-dpaa-persistent-networking.rules \
    file://73-fsl-dpaa-persistent-networking.rules \
"
RULES = "71-fsl-dpaa-persistent-networking.rules"
RULES_e6500 = "72-fsl-dpaa-persistent-networking.rules"
RULES_e6500-64b = "72-fsl-dpaa-persistent-networking.rules"
RULES_t1024 = "72-fsl-dpaa-persistent-networking.rules"
RULES_qoriq-arm64 = "73-fsl-dpaa-persistent-networking.rules"

do_install () {
    install -d ${D}${sysconfdir}/udev/rules.d/
    for r in ${RULES};do
        install -m 0644 ${WORKDIR}/${r} ${D}${sysconfdir}/udev/rules.d/
    done
}

