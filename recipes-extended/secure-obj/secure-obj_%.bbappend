# Copyright 2022 NXP

SRC_URI = "git://github.com/nxp-qoriq/secure_obj;protocol=https;nobranch=1"

DEPENDS:remove = " python3-pycryptodomex-native"
DEPENDS:append = " python3-cryptography-native"

OPTEE_ARCH:arm = "arm32"
OPTEE_ARCH:aarch64 = "arm64"

do_compile() {
        unset LDFLAGS
        export TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-user_ta_${OPTEE_ARCH}/
        export CROSS_COMPILE="${WRAP_TARGET_PREFIX}"
        export OPENSSL_PATH="${RECIPE_SYSROOT}/usr"
        export OPENSSL_MODULES=${STAGING_LIBDIR_NATIVE}/ossl-modules
        for APP in  secure_storage_ta securekey_lib secure_obj-openssl-engine; do
            cd  ${APP}
            oe_runmake
	    cd ..
        done
}
