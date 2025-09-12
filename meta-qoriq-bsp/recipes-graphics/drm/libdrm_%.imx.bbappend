
SRCREV = "fb9b92bc6d4bde6ef2b59a96e5256780cc7634c2"
PACKAGES:prepend:ls1028a = "${PN}-vivante "
RRECOMMENDS:${PN}-drivers:append:ls1028a = " ${PN}-vivante"
PACKAGECONFIG:append:ls1028a = " vivante"

COMPATIBLE_MACHINE:append = "|(ls1028a)"
