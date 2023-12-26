# Copyright 2022-2023 NXP
require optee-test-qoriq.inc

OPTEE_TEST_BRANCH = "lf-6.6.3_1.0.0"
SRCREV = "${AUTOREV}"

PV:append = "+git${SRCPV}"
