SRCREV = "a36da6a94243015b228c15b8b9aa1e650fd4b96d"

TLSDIALECT_aarch64 = "-ftls-model=local-dynamic"
do_compile () {
        unset LDFLAGS TARGET_LDFLAGS BUILD_LDFLAGS

        cd ${S}/${RTE_TARGET}
        oe_runmake  CONFIG_RTE_EAL_IGB_UIO=n CONFIG_RTE_KNI_KMOD=y \
                    CONFIG_RTE_LIBRTE_PMD_OPENSSL=y \
                   EXTRA_LDFLAGS="-L${STAGING_LIBDIR} --hash-style=gnu" \
                   EXTRA_CFLAGS="${HOST_CC_ARCH} ${TOOLCHAIN_OPTIONS} -I${STAGING_INCDIR} -Ofast -fPIC ${TLSDIALECT}" \
                   CROSS="${TARGET_PREFIX}" \
                   prefix=""  LDFLAGS="${TUNE_LDARGS}"  WERROR_FLAGS="-w" V=1

        cd ${S}/examples/
        for APP in l2fwd l3fwd cmdif l2fwd-qdma l2fwd-crypto ipsec-secgw vhost kni ip_fragmentation ip_reassembly; do
            temp=`basename ${APP}`
            if [ ${temp} = "ipsec-secgw" ] || [ ${temp} = "l2fwd-crypto" ]; then
                oe_runmake EXTRA_LDFLAGS="-L${STAGING_LIBDIR} --hash-style=gnu -fuse-ld=bfd" \
                       EXTRA_CFLAGS="${HOST_CC_ARCH} ${TOOLCHAIN_OPTIONS} -I${STAGING_INCDIR}" \
                       CROSS="${TARGET_PREFIX}" -C ${APP} CONFIG_RTE_LIBRTE_PMD_OPENSSL=y O="${S}/examples/${temp}"
            else
                oe_runmake EXTRA_LDFLAGS="-L${STAGING_LIBDIR} --hash-style=gnu -fuse-ld=bfd" \
                       EXTRA_CFLAGS="${HOST_CC_ARCH} ${TOOLCHAIN_OPTIONS} -I${STAGING_INCDIR}" \
                       CROSS="${TARGET_PREFIX}" -C ${APP} CONFIG_RTE_LIBRTE_PMD_OPENSSL=y O="${S}/examples/${temp}/"
            fi
        done

}

