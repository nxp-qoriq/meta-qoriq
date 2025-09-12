SRCBRANCH = "lf-5.10.y_2.0.0"
KEYCTL_CAAM_SRC ?= "git://github.com/nxp-imx/keyctl_caam.git;protocol=https"
SRC_URI = "${KEYCTL_CAAM_SRC};branch=${SRCBRANCH}"
SRCREV = "6b80882e3d5bc986a1f2f9512845170658ba9ea2"

