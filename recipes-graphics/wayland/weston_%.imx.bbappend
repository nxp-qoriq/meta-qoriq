WESTON_SRC = "git://bitbucket.sw.nxp.com/gtec/weston-imx.git;protocol=ssh"

PACKAGECONFIG_OPENGL_ls1028a   = "opengl"
PACKAGECONFIG_append_ls1028a   = " imxgpu"
COMPATIBLE_MACHINE = "(imxfbdev|imxgpu|qoriq)"
