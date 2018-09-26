SRC_URI = "git://source.codeaurora.org/external/qoriq/qoriq-components/optee_os;protocol=https;nobranch=1 \
    file://0001-allow-setting-sysroot-for-libgcc-lookup.patch \
"

SRCREV = "5f6c95c085bb0dd750a7f5c464e5ebe6de0a99bd"
COMPATIBLE_MACHINE = "(ls1043ardb|ls1046ardb|ls1012ardb|ls1088ardb|ls1088ardb_pb)"
