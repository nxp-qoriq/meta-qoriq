PACKAGECONFIG:append = " json-c"
PACKAGECONFIG:append:qoriq-arm64 = " numactl"
PACKAGECONFIG:remove:ls1021atwr = "yajl qemu"
PACKAGECONFIG:remove:ls1012ardb = "yajl qemu"
PACKAGECONFIG[json-c] = ",,json-c"

do_compile:prepend() {
    if [ -e "${BP}/${BPN}_python-${LIBVIRT_VERSION}" ]; then
        mv ${UNPACKDIR}/${BP}/${BPN}_python-${LIBVIRT_VERSION} ${UNPACKDIR}/${BP}/${BPN}-python-${LIBVIRT_VERSION}
    fi
}
