PACKAGECONFIG += "${@bb.utils.contains('ARCH', 'arm', '', 'numactl', d)} "
PACKAGECONFIG_remove_arm = "numactl"
