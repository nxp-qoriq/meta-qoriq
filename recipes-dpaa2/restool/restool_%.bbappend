FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI = "git://bitbucket.sw.nxp.com/dpaa2/restool;protocol=ssh;nobranch=1 \
    file://0001-restool-fix-build-error-with-gcc7.patch \
    file://gcc10.patch \
"
SRCREV= "d002c5ff28212469b884535c5f64fa7da7731bef"
