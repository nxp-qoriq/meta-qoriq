# Copyright 2022-2024 NXP
require optee-test-qoriq.inc

OPTEE_TEST_BRANCH = "lf_4.2.y"
SRCREV = "${AUTOREV}"

PV:append = "+git${SRCPV}"
