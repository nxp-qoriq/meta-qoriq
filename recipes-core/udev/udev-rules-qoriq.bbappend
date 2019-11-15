FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://73-fsl-enetc-networking.rules"

RULE_qoriq-arm64 = "73-fsl-dpaa-persistent-networking.rules \
              73-fsl-enetc-networking.rules "
