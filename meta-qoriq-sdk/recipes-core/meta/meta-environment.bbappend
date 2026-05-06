# Add custom SDK_SYSROOT_HOST export to environment-setup script

toolchain_create_sdk_env_script:append() {
    # Add host path to environment-setup script
    echo 'export HOSTCFLAGS="-I${OECORE_NATIVE_SYSROOT}/usr/include"' >> $script

    # FIXME: uboot tools/asn1_compiler need GLIBC VERSION >= 2.38, fix dynamic-link searth path to OECORE_NATIVE_SYSROOT instead of host OS.
    # common error shows: undefined reference to `__isoc23_strtoul@GLIBC_2.38'
    echo 'export HOSTLDFLAGS="-L${OECORE_NATIVE_SYSROOT}/usr/lib -Wl,--dynamic-linker=${OECORE_NATIVE_SYSROOT}/lib/ld-linux-x86-64.so.2 -Wl,--rpath=${OECORE_NATIVE_SYSROOT}/lib -Wl,--rpath-link=${OECORE_NATIVE_SYSROOT}/usr/lib"' >> $script
}
