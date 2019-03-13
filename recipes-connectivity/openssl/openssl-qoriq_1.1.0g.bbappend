SRC_URI = "git://bitbucket.sw.nxp.com/sdk/openssl.git;protocol=ssh;nobranch=1 \ 
           file://run-ptest \
           file://openssl-c_rehash.sh \
           file://0001-Take-linking-flags-from-LDFLAGS-env-var.patch \
           file://0001-Remove-test-that-requires-running-as-non-root.patch \
           file://0001-aes-asm-aes-armv4-bsaes-armv7-.pl-make-it-work-with-.patch \
"

SRCREV = "472c9c380669eb7a26819a52598632f257b3e72b"
