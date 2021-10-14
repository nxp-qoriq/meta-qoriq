FILESEXTRAPATHS:prepend:qoriq := "${THISDIR}/${BPN}:"

SRC_URI:append:qoriq = "\
    file://pip_stress-De-constify-prio_min.patch \
"

