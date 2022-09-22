# Switch code fetch from CAF to github.com
SRC_URI:remove = "gitsm://source.codeaurora.org/external/qoriq/qoriq-components/qemu;nobranch=1"
SRC_URI += "gitsm://github.com/nxp-qoriq/qemu.git;protocol=https;nobranch=1"

