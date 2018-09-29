do_configure() {
        export SYSROOT_DPDK=${PKG_CONFIG_SYSROOT_DIR}
        ${S}/boot.sh
        ${S}/configure --host aarch64-fsl-linux --with-dpdk=${SYSROOT_DPDK}/usr/share/${RTE_TARGET} --with-openssl=${SYSROOT_DPDK}/usr CFLAGS="-g -Wno-cast-align -Wno-unused-function -Wno-traditional -Wno-missing-field-initializers -Wno-format-truncation -Ofast"
}

