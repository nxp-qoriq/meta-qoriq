# Copyright 2022-2023 NXP

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=751419260aa954499f7abaabaa882bbe"

DEPENDS:remove = " python3-pycryptodomex-native"
DEPENDS:append = " python3-cryptography-native optee-os-qoriq-tadevkit"

SRCREV = "df2648932f1933f64dd377e9ec58c4162177561a"
do_compile() {
        unset LDFLAGS
        export TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-user_ta/
        export CROSS_COMPILE="${WRAP_TARGET_PREFIX}"
        export OPENSSL_PATH="${RECIPE_SYSROOT}/usr"
        export OPENSSL_MODULES=${STAGING_LIBDIR_NATIVE}/ossl-modules
        for APP in  secure_storage_ta securekey_lib secure_obj-openssl-engine; do
            cd  ${APP}
            oe_runmake
	    cd ..
        done
}
