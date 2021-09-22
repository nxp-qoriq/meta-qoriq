IMX_LIBDRM_SRC = "git://bitbucket.sw.nxp.com/gtec/libdrm-imx.git;protocol=ssh;nobranch=1"

PACKAGES_prepend_ls1028a = "${PN}-vivante "
RRECOMMENDS_${PN}-drivers_append_ls1028a = " ${PN}-vivante"
PACKAGECONFIG_append_ls1028a = " vivante"

