IMX_LIBDRM_SRC = "git://bitbucket.sw.nxp.com/gtec/libdrm-imx.git;protocol=ssh;nobranch=1"

PACKAGES:prepend:ls1028a = "${PN}-vivante "
RRECOMMENDS:${PN}-drivers:append:ls1028a = " ${PN}-vivante"
PACKAGECONFIG:append:ls1028a = " vivante"

COMPATIBLE_MACHINE:append = "|(ls1028a)"
