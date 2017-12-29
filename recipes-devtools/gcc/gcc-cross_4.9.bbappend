require gcc-fsl.inc

EXTRA_OECONF_PATHS_append_qoriq-ppc = " --with-isl=${STAGING_DIR_NATIVE}${prefix_native} \
                       --with-cloog=${STAGING_DIR_NATIVE}${prefix_native} \
"
