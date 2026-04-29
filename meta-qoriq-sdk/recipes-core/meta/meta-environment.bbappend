# Add custom SDK_SYSROOT_HOST export to environment-setup script

toolchain_create_sdk_env_script:append() {
    # Add SDK_SYSROOT_HOST to environment-setup script
    echo 'export SDK_SYSROOT_HOST="${SDKPATH}/sysroots/x86_64-fslsdk-linux"' >> $script
    echo 'export HOSTCFLAGS="-I${SDK_SYSROOT_HOST}/usr/include"' >> $script

    # FIXME: uboot tools/asn1_compiler need GLIBC VERSION >= 2.38, fix dynamic-link searth path to SDK_SYSROOT_HOST instead of host OS.
    # common error shows: undefined reference to `__isoc23_strtoul@GLIBC_2.38'
    echo 'export HOSTLDFLAGS="-L${SDK_SYSROOT_HOST}/usr/lib -Wl,--dynamic-linker=${SDK_SYSROOT_HOST}/lib/ld-linux-x86-64.so.2 -Wl,--rpath=${SDK_SYSROOT_HOST}/lib -Wl,--rpath-link=${SDK_SYSROOT_HOST}/usr/lib"' >> $script

    # PKG_CONFIG for host tools
    echo 'export PKG_CONFIG_PATH="${SDK_SYSROOT_HOST}/usr/lib/pkgconfig:${SDK_SYSROOT_HOST}/usr/share/pkgconfig:${SDKTARGETSYSROOT}/usr/lib/pkgconfig:${SDKTARGETSYSROOT}/usr/share/pkgconfig"' >> $script
    echo 'export PKG_CONFIG_SYSROOT_DIR="${SDK_SYSROOT_HOST}"' >> $script
}
